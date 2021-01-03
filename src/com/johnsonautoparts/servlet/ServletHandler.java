package com.johnsonautoparts.servlet;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.johnsonautoparts.Project;
import com.johnsonautoparts.Project4;
import com.johnsonautoparts.db.DB;
import com.johnsonautoparts.exception.AppException;
import com.johnsonautoparts.exception.DBException;
import com.johnsonautoparts.logger.AppLogger;


/**
 * Servlet Class registered via the web.xml as the primary class for handling
 * calls for the webapp. The doGet() and doPost() are called in Tomcat and
 * registerd as the handlers in this class.
 * 
 */
public class ServletHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
  	private String project=null;
  	private String task=null;
  	private String paramType=null;
  	private Map<String,String[]> params;

  	/**
  	 * @see HttpServlet#HttpServlet()
  	 */
  	public ServletHandler() {
  		super();
  	}
  	
  	
  	/**
  	 * Out of band used test functions of WAR
  	 */
  	public static void main(String[] args) {
  		if(! (args.length > 0) ) {
  			System.err.println("Missing argument");
  			System.exit(1);
  		}
  		
  		switch(args[0]) {
  		case "database":
  			try {
  				Connection connection = DB.getDbConnection(null);
  				try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM tasks") ) {
  					//do nothing
  				}
  			}
  			catch(DBException dbe) {
  				dbe.printStackTrace();
  			}
  			catch(SQLException sqe) {
  				sqe.printStackTrace();
  			}
  			
  			break;
  		
  		default:
  			System.err.println("Function " + args[0] + " not implemented");
  			System.exit(1);
  		}
  	}
  	
  	
	/**
  	 * Handle POST request
  	 */
  	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		AppLogger.log("Processing POST request");
  		
  		//set the default response and content-type
  		String responseContent="<html><body>All Good</body></html>";
  		response.setContentType("text/html");

  		
  		// check if the task param was sent
  		String action=null;
  		if(request.getParameter("action") == null || request.getParameter("action").isEmpty()) {
  			action="";
  		}
  		else {
  			action = request.getParameter("action");
  		}

  		switch(action) {
  		case "login":
  			try {
  				Map<String,String> loginParams = parseLoginParams(request);

  				Connection connection = getConnection(request);
  				Project4 project4 = new Project4(connection, request, response);
  			}
  			catch(AppException ae) {
  	  			AppLogger.log("POST caught AppException: " + ae.getPrivateMessage());
  	  			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
  	  			ServletUtilities.sendError(response, ae.getMessage());
  	  			return;
  			}
  		
  			//send successful response
  			PrintWriter out = response.getWriter();
  			out.println(responseContent);
  			
  			break;
  			
  		default:
  			//nothing matched so process as a GET
  			AppLogger.log("Cannot process POST request, forwarding to GET");
  			doGet(request, response);
  			
  			break;
  		}
  		
  		//done processing POST so return
  		return;
  	}
  	

  	/**
  	 * Handle GET request
  	 */
  	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 		AppLogger.log("Processing GET request");

  		//set the default response and content-type
  		String responseContent="<html><body>All Good</body></html>";
  		response.setContentType("text/html");
  		
 		/**
 		 * Make sure everything is good before proceeding or throw an exception
 		 */
 		if(! isRequestValid(request) ) {
 			throw new ServletException("Invalid request sent to " + request.getServletPath());
 		}
 		
 		
 		//main logic to parse action
  		try {
  			//minimize code by using reflection to discover classes and methods
			Project projectClass = getProjectClass(project, getConnection(request), request, response);
			Method method = getProjectMethod(projectClass.getClass());
			//ProjectCaller projectCaller = getProjectCaller(project, connection, request);

			try {
				String[] paramStr=params.get("param1");
				if( (paramStr==null) || (paramStr[0]==null) || (paramStr[0].trim().isEmpty())) {
					throw new AppException("param 1 is empty", "param1 incorrect");
				}
					
				//String for the content
				Object responseData=null;
				
				//handle methods with a string parameter
				if(paramType.equals("String")) {					
					String param1 = params.get("param1")[0];

					responseData=method.invoke(projectClass, param1);					
				}

				// this is a bad idea to just attempt to convert a string to an integer
				// even when catching NumberFormatException but we use it here to simply
				// the code base since this portion of the code is not reviewed in the project
				else if(paramType.equals("Integer")) {
					int param1 = Integer.parseInt(params.get("param1")[0]);
										
					responseData=method.invoke(projectClass, param1);
				}
				
				else {
					AppLogger.log(request.getSession().getId() + " cannot parse paramtype and invoke method");
					ServletUtilities.sendError(response, "incorrect parameters");
					throw new AppException("Cannot parse paramtype and invoke method", "application error");
				}
				
				//update the responseData
				if(response.getContentType() != null) {
					if(response.getContentType().contains("html") ) {
						responseContent = "<html><body>" + responseData + "</body></html>";
					}
					else if(response.getContentType().contains("xml")) {
						responseContent = responseData.toString();
					}
					//all other responses use the default message
				}

			}
			catch(NumberFormatException nfe) {
				throw new AppException("caught NumFormatException: " + nfe.getMessage(), "application error");
			}
			catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException iiie) {
				throw new AppException("caught illegal exception: " + iiie.getMessage(), "application error");
			}
  			
  			//send successful response
  			PrintWriter out = response.getWriter();
  			out.println(responseContent);
  			return;
  		}
  		
  		/**
  		 * Application exception errors are caught
  		 * Private message is logged and the public message is returned to the request
  		 */
  		catch (AppException ae) {
  			AppLogger.log("AppException: " + ae.getPrivateMessage());
  			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
  			ServletUtilities.sendError(response, ae.getMessage());
  			return;
  		}

  	}

  	
  	/**
  	 * 
  	 * NOTHING BELOW THIS POINT NEEDS TO BE EDITED FOR THE liveProject
  	 *
  	 */
  
  	/**
  	 * Check the request to make sure everything is valid including parameters
  	 * 
  	 * @param request
  	 * @param response
  	 * @return boolean if no errors were detected
  	 */
  	public boolean isRequestValid(HttpServletRequest request) {
 
 		//parse params
  		try {
  			parseParams(request);
  			return true;
  		}
  		catch(AppException ae) {
  			AppLogger.log("parseParams caught exception: " + ae.getPrivateMessage());
  			return false;
  		}
  	}
  	
  	
  	/**
  	 * Verify the required parameters where passed 
  	 * 
  	 * @param request
  	 * @param response
  	 */
  	private void parseParams(HttpServletRequest request) throws AppException {
 		params = request.getParameterMap(); 

  		// return an error if the Map is null or empty
  		if(params == null || params.isEmpty()) {
  			throw new AppException("no params sent", "missing request parameters");
  		}
  		
  		
  		/**
  		 * 
  		 * check if the action param was sent
  		 * 
  		 * SpotBugs tags this get() request as a possible flaw since it did not see the
  		 * The params Map is populated above with request.getParameterMap()
  		 */
  		
  		if(params.get("project") == null) {
  			throw new AppException("project param not sent", "missing request parameters");
  		}
  		else {
  			// avoid parameter overloading attack and only select the first item in the array
  			this.project = params.get("project")[0];
  		}

  		// check if the task param was sent
  		if(params.get("task") == null) {
  			throw new AppException("task param not sent", "missing request parameters");
  		}
  		else {
  			// avoid parameter overloading attack and only select the first item in the array
  			this.task = params.get("task")[0];
  		}
  		
  	}//end parseParams


  	/**
  	 * Verify the required parameters where passed 
  	 * 
  	 * @param request
  	 * @param response
  	 */
  	private Map<String,String> parseLoginParams(HttpServletRequest request) throws AppException {
 		params = request.getParameterMap(); 

 		Map<String,String> loginParams = new HashMap<>();
 		
  		// return an error if the Map is null or empty
  		if(params == null || params.isEmpty()) {
  			throw new AppException("no params sent", "missing request parameters");
  		}

  		//username
  		if(params.get("username") == null) {
  			throw new AppException("username param not sent", "missing request parameters");
  		}
  		else {
  			// avoid parameter overloading attack and only select the first item in the array
  			loginParams.put("username", params.get("username")[0]);
  		}

  		//password
  		if(params.get("password") == null) {
  			throw new AppException("password param not sent", "missing request parameters");
  		}
  		else {
  			// avoid parameter overloading attack and only select the first item in the array
  			loginParams.put("password", params.get("password")[0]);
  		}
  		
  		//secure_form flag
  		if(params.get("secure_form") == null) {
  			throw new AppException("secure_form param not sent", "missing request parameters");
  		}
  		else {
  			// avoid parameter overloading attack and only select the first item in the array
  			loginParams.put("secure_form", params.get("secure_form")[0]);
  		}
  		
  		return loginParams;
  	}//end parseLoginParams

  	
  	/**
  	 * Internal method to discover the proper method to call by using reflection
  	 * 
  	 * IMPORTANT: THIS CODE IS NOT PART OF THE EXERCISES TO REVIEW
  	 * The method is only here to simplify the code base and dynamically call
  	 * the tasks since there are so many in the projects.
  	 * 
  	 * @param requestClass The Project class requested
  	 * @return Method discovered based on the string of the task name and a valid method which doesn't cause an Exception
  	 * @throws AppException
  	 */
    private Method getProjectMethod(Class<?> requestClass) throws AppException {
        Method method=null;
        Class<?>[] classTypes = {Integer.class, String.class};

        for(int i=0; i < classTypes.length; i++ ) {
        	try {
        		method = requestClass.getDeclaredMethod(task, classTypes[i]);
        		
        		AppLogger.log("Used getProjectMethod() to discover task: " + task + " with param type: " + classTypes[i].getCanonicalName());
        		this.paramType = classTypes[i].getSimpleName();
                        
        		return(method);
        	}
        	catch(NoSuchMethodException constructorEx) {
        		//ignore exception since we are trying to find the constructor
        	}
        }
        
        //throw exception if reaching this point and method was not discovered
        throw new AppException("getProjectMethod() caught exception for invalid Project class: " + requestClass.getSimpleName() +
                " with task: " + task, "application error");
    }

 
  	
  	/**
  	 * Internal method to discover the proper Project to use via reflection
  	 * 
  	 * IMPORTANT: THIS CODE IS NOT PART OF THE EXERCISES TO REVIEW
  	 * The method is only here to simplify the code base and dynamically call
  	 * the tasks since there are so many in the projects.
  	 * 
  	 * @param String of the Project name to discover
  	 * @return Project class discovered based on the string of the name
  	 * @throws AppException
  	 */
  	private Project getProjectClass(String projectName, Connection connection, HttpServletRequest request, HttpServletResponse response) throws AppException{
		if(projectName == null || connection == null || request == null || response == null) {
			throw new AppException("getProjectObject() was passed a null variable", "application error");
		}
  		
		//capitalize the first letter of the project name to match the class
		String className = "com.johnsonautoparts." + projectName.substring(0, 1).toUpperCase(Locale.ENGLISH) + projectName.substring(1);
		
		Class<?> reflectedClass = null;
		
		try {
			reflectedClass = Class.forName(className);
			Constructor<?> projectConstructor = reflectedClass.getConstructor(new Class[] {Connection.class, HttpServletRequest.class, HttpServletResponse.class});
						
			Project reflectedProject = (Project) projectConstructor.newInstance(connection, request, response);
			
			return(reflectedProject);
		}
		catch(ClassNotFoundException cnfe) {
			throw new AppException("getProjectObject() caught exception of ClassNotFound for project name: " + className, "application error");
		}
		catch(NoSuchMethodException constructorEx) {
			throw new AppException("getProjectObject caught exception for invalid Project class: " + reflectedClass.getClass().toString(), "application error");
		}
		catch(InvocationTargetException | IllegalAccessException | InstantiationException instanceEx) {
			throw new AppException("getProjetObject caught exception for invalid constuctor: " + instanceEx.getMessage(), "application error");
		}
		
  	}

  	
  	/**
  	 * Internal method to resolve the DB conneciton
  	 * 
  	 * IMPORTANT: THIS CODE IS NOT PART OF THE EXERCISES TO REVIEW
  	 * 
  	 */
  	private Connection getConnection(HttpServletRequest request) throws AppException {
  			HttpSession session = request.getSession();
  			Object connectionObj = request.getAttribute("connection");
  			
			if(connectionObj != null && connectionObj instanceof Connection) {
				return (Connection) connectionObj;
			}
			//no Connection so try to get one
			else {
				try {
					return DB.getDbConnection(session);
				}
				catch(DBException dbe) {
					throw new AppException("getConnection could not establish a DB connection: " + dbe.getMessage(), "application error");
				}
			}
			
  	}
  	
}
