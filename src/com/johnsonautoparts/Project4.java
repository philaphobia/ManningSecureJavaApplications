package com.johnsonautoparts;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.johnsonautoparts.exception.AppException;
import com.johnsonautoparts.logger.AppLogger;


/**
 * 
 * Project4 class which contains all the method for the milestones. The task number 
 * represents the steps within a milestone.
 * 
 * Each method has a name which denotes the type of security check we will be fixing.
 * There are several fields in the notes of each method:
 * 
 * TITLE - this is a description of code we are trying to fix
 * RISK - Some explanation of the security risk we are trying to avoid
 * ADDITIONAL - Further help or explanation about work to try
 * REF - An ID to an external reference which is used in the help of the liveProject
 * 
 */
public class Project4 extends Project {

	public Project4(Connection connection, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		super(connection, httpRequest, httpResponse);
	}
	
	
	/**
	 * Project 4, Milestone 1, Task 1
	 * 
	 * TITLE: Do not trust hidden forms
	 * 
	 * RISK: While hidden forms are not displayed in the web browser, they can still be manipulated by
	 *       the user and forged. Hidden forms should be sanitized just like all other data.
	 * 
	 * REF: CMU Software Engineering Institute IDS14-J
	 * 
	 * @param username
	 * @param password
	 * @param secureForm
	 * @return boolean
	 */
	public String login(String username, String password, String secureForm) throws AppException {		
		//Project2 object for xPath login
		Project2 project2 = new Project2(connection, httpRequest, httpResponse);
		String userPass = username + ":" + password;
		
		//process login
		boolean loginSuccess = project2.xpathLogin(userPass);
		
		if(loginSuccess) {
			return(secureForm);
		}
		else {
			return(secureForm + " unsuccessful");
		}
	}
	
	
	/**
	 * Project 4, Milestone 1, Task 2
	 * 
	 * TITLE: Encoding data and escaping output for display
	 * 
	 * RISK: Untrusted data must not be included in the web browser since it may contain unsafe code.
	 *       In a more complex attack, a malicious user may include JavaScript and HTML. types of attacks.
	 *       Untrusted data displayed to the user should neutralize JavaScript and HTML. Use the OWASP
	 *       Enocder protect to filter both.
	 * 
	 * REF: CMU Software Engineering Institute IDS14-J
	 * 
	 * IMPORTANT: For the following task you will be working on a JSP form at:
	 *            WebContent/jsp/comments.jsp
	 *            
	 *            The encoding is applicable in Java as well if you are returning data which needs to
	 *            be encoded.
	 */

	//END Project 4, Milestone 1, Task 2
	
	
	/**
	 * Project 4, Milestone 1, Task 3
	 * 
	 * TITLE: Avoid arbitrary file uploads
	 * 
	 * RISK: The referer header can be manipulated by a user and should be assumed to be tainted data.
	 *       Since the header is untrusted, it should not be used as a reference source for making
	 *       security deisions.
	 * 
	 * REF: CMU Software Engineering Institute IDS56-J
	 * CODE: https://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
	 * 
	 */
	public boolean fileUpload(int numFiles) throws AppException {
		final String[] ACCEPTED_CONTENT = {"application/pdf"};
		DiskFileItemFactory factory = new DiskFileItemFactory();
		   
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("upload"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try { 
            // Parse the request to get file items.
            List<FileItem> fileItems = upload.parseRequest(httpRequest);
   	
            // Process the uploaded file items
            Iterator<FileItem> i = fileItems.iterator();
            File file=null;
            
            while ( i.hasNext () ) {
                FileItem fi = (FileItem)i.next();
                
        		String fileName = fi.getName();
                String contentType = fi.getContentType();
                
                //check if the contentType is accepted
                boolean contentTypeOk = false;
                for(String contentTypeCheck : ACCEPTED_CONTENT) {
                	if(contentTypeCheck == contentType) {
                		contentTypeOk = true;
                	}
                }
                //throw an exception if one of the accepted content-type was not found
                if(! contentTypeOk) {
                	throw new AppException("File was uploaded with a type that is not accepted", "application error");
                }
                
                // Write the file
                if( fileName.lastIndexOf(File.separator) >= 0 ) {
                	file = new File( "upload" + File.separator + fileName.substring( fileName.lastIndexOf(File.separator))) ;
                }
                else {
                	file = new File( "upload" + File.separator + fileName.substring(fileName.lastIndexOf(File.separator)+1)) ;
                }
                
                fi.write( file );
            }//end while to process FileItems
            
            //all files uploaded successfully
            return(true);
		}
		catch(FileUploadException fue) {
            throw new AppException("Upload exception: " + fue.getMessage(), "Application error");
		}
		catch(NoSuchElementException nsee) {
            throw new AppException("Iterator caused exception: " + nsee.getMessage(), "Application error");
		}
		//catch general Exception breaks the rules but this is the only exception thrown by
		//FileItem.write() method
		catch(Exception e) {
			throw new AppException("FileWrite caused exception: " + e.getMessage(), "Application error");
		}
	}
	

	/**
	 * Project 4, Milestone 1, Task 4
	 * 
	 * TITLE: Do not use printStackTrace
	 * 
	 * RISK: PrintStackTrace() contains sensitive data about the application and could provide information
	 *       to a malicious user that would help in an attack. GetMessage() can be used and sent to the
	 *       audit data with a general message to the user.
	 * 
	 * REF: RSPEC-1148
	 * 
	 * @param field
	 * @return String
	 */
	public String getAttribute(String field) {
		try {
			String normField = Normalizer.normalize(field, Form.NFKC);
			String txtField = normField.replaceAll("\\^[0-9A-Za-z_]","");
			
			Object val = httpRequest.getSession().getAttribute(txtField);
			if(val != null && val instanceof String) {
				return (String)val;
			}
			else {
				throw new IllegalStateException("getAttribute did not retrieved data for field " + txtField);
			}
		}
		catch(IllegalStateException ise) {
			//convert the printstack to a string for better debugging
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			
			//call printStackTrace to the print/string
			ise.printStackTrace(pw);

			//return the error as the content
			return(sw.toString());
		}

	}
	
	
	/**
	 * Project 4, Milestone 1, Task 5
	 * 
	 * TITLE: Sanitize HTML when tags are needed
	 * 
	 * RISK: If the application allows untrusted data to include HTML, then a whitelist of accepted tags
	 *       should be enforced. Blacklisting will not help and the tags allowed should be very limited
	 *       to avoid tricky malicious users from bypassing the expected controls.
	 * 
	 * REF: OWASP XSS Cheat Sheet Rule #6
	 *      https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html
	 * 
	 * IMPORTANT: For the following task you will be working on a JSP form and the current method in Project4:
	 *            WebContent/jsp/blog.jsp
	 *            
	 *            Since blog.jsp is taking a parameter and displaying it to the user, the data must be
	 *            sanitized. The data is then sent to this method and should be sanitized again before
	 *            putting it into the database.
	 *            
	 *            For the JSP, imagine the user sent the following as the blog parameter:
	 *            close the real text area</textarea><script>alert('XSS from closed TextArea');</script><textarea>new text
	 *            
	 *            Notice how the textarea tag is closed, then JavaScript is entered, and the textarea
	 *            is then closed. This creates a valid HTML with two textareas and JavaScript tags in
	 *            the middle which executes in the target users browser.
	 *            
	 */
	public String postBlog(String blogEntry) throws AppException {
		try {
			String sql = "INSERT INTO blog(blog) VALUES (?)";
			
			try (PreparedStatement stmt = connection.prepareStatement(sql) ) {
				stmt.setString(1, blogEntry);
				
				//execute the insert
				int rows = stmt.executeUpdate();
				
				//verify the insert worked based on the number of rows returned
				if(rows > 0) {
					return("Blog entry accepted");
				}
				else {
					throw new AppException("postBlog() did not insert to table correctly", "application error");
				}
			}
	   
		} catch (SQLException se) {
			throw new AppException("postBlog() caught SQLException: " + se.getMessage(), "application error");
		} 
		finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} 
			catch (SQLException se) {
				AppLogger.log("postBlog() failed to close connection: " + se.getMessage());
			}
		}
	}

	
	/**
	 * Project 4, Milestone 2, Task 1
	 * 
	 * TITLE: HTTP verb (method) security
	 * 
	 * RISK: The webapp should make a clear distinction between how requests are process such as
	 *       by POST or GET. Unclear application flow may occur if GET and POST requests are accepted
	 *       for the same type of request. Also, GET requests include the parameter data into the web
	 *       request log which could allow sensitive information such as password if for example a
	 *       login request is processed as a GET. If the login goes through a proxy server or other
	 *       service, the data could also be leaked.
	 *
	 * IMPORTANT: No changes are made in this mile for the current task. The changes are made in the
	 *            ServletHandler by reviewing the doPost() and doGet() methods.
	 */
	//END Project 4, Milestone 2, Task 1
	
	
	/**
	 * Project 4, Milestone 2, Task 2
	 * 
	 * TITLE: Avoid header injection
	 * 
	 * RISK: Allowing untrusted data to be injected in response headers can open the webapp up to many
	 *       attack vectors. All untrusted data should be sanitized before returning it to the user.
	 *       An attacker could overwrite security headers if allowed or other attacks which use
	 *       end of line characters (called a split response header) cause the browser to receive and
	 *       process two different responses. The normal sanitization and filtering is usually insufficient,
	 *       and a whitelist of acceptable values would be the best solution.
	 * 
	 * REF: SonarSource RSPEC-
	 * 
	 * @param str
	 * @return String
	 */
	public String addHeader(String header) {
		httpResponse.addHeader("X-Header", header);
  		
		return(header);
	}
	
	
	/**
	 * Project 4, Milestone 2, Task 3
	 * 
	 * TITLE: Servlet must not throw errors
	 * 
	 * RISK: If the servlet of the webapp throws an error it may not be processed in the expected
	 *       fashion. This could include causing the webapp to crash or become unstable. If the exception
	 *       is handled, the application server may report the entire exception stack back to the user
	 *       which could include sensitive information.
	 *       
	 * IMPORTANT: No changes are made in this file. The changes are made in the ServletHandler class
	 *            where an ServletException is thrown.
	 * 
	 * REF: SonarSource RSPEC-1989
	 */
	//END Project 4, Milestone 2, Task 3
	
	
	/**
	 * Project 4, Milestone 2, Task 4
	 * 
	 * TITLE: Do not trust referer header for security decisions
	 * 
	 * RISK: The referer header can be manipulated by a user and should be assumed to be tainted data.
	 *       Since the header is untrusted, it should not be used as a reference source for making
	 *       security decisions.
	 * 
	 * NOTES: In a previous milestone you added security controls to filter the data in the comments.jsp
	 *        form so no further changes are required in the JSP.
	 *        
	 *        Develop a different security control than the header here (such as a CSRF token). 
	 *        
	 *        You will also want to duplicate filtering on the data from the form here before it is entered 
	 *        into the database.  Event though the data was filtered before displaying in the form, a 
	 *        malicious user could still change it before re-submitting to this method.
	 *        
	 * REF: SonarSource RSPEC-2089
	 * 
	 * @param comments
	 * @return int
	 */
	public String postComments(String comments) throws AppException {
		final String REFERER_COMMENTS = "comments.jsp";

		String referer = httpRequest.getHeader("referer");
		if(referer == null) {
			throw new AppException("comments() cannot retrieve referer header", "application error");
		}

		//check whitelist referer comments form
		if(!referer.contains(REFERER_COMMENTS)) {
			throw new AppException("comments() cannot validate referer header", "application error");
		}

		try {
			String sql = "INSERT INTO COMMENTS(comments) VALUES (?)";
			
			try (PreparedStatement stmt = connection.prepareStatement(sql) ) {
				stmt.setString(1, comments);
				
				//execute the insert and return the number of rows
				int rows = stmt.executeUpdate();
				
				//verify the insert worked based on the number of rows returned
				if(rows > 0) {
					return("Comments accepted");
				}
				else {
					throw new AppException("postComments() did not insert to table correctly", "application error");
				}
			}
	   
		} catch (SQLException se) {
			throw new AppException("postComments() caught SQLException: " + se.getMessage(), "application error");
		} 
		finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} 
			catch (SQLException se) {
				AppLogger.log("postComments() failed to close connection: " + se.getMessage());
			}
		}
	}
	
	
	/**
	 * Project 4, Milestone 2, Task 5
	 * 
	 * TITLE: Do not redirect to URL from untrusted source
	 * 
	 * RISK: Never redirect users to a URL which contains unsanitized data:
	 *       "User provided data, such as URL parameters, POST data payloads, or cookies, 
	 *       should always be considered untrusted and tainted. "
	 * 
	 * REF: SonarSource RSPEC-5146
	 * 
	 * @param str
	 * @return String
	 */
	public void redirectUser(String location) throws AppException {
		try {
			httpResponse.sendRedirect(location);
		} 
		catch (IOException e) {
			throw new AppException("redirectUser caught exception for location: " + e.getMessage(), "application error");
		}
	}
			
	
	/**
	 * Project 4, Milestone 2, Task 6
	 * 
	 * TITLE: Protect the webapp with security headers
	 * 
	 * RISK: Certain headers can help the browser protect the user from attacks:
	 *       - Limiting the ability to embed the site into a hidden frame (click-jacking)
	 *       - Change the content-type (MIME Type sniffing)
	 *       - XSS and data injection attacks
	 *
	 * IMPORTANT: No changes are made in this file. The headers can be injected anywhere the response
	 *            is available, but care must be taken to account for every flow of the application.
	 *            Therefore, a class which is always executed, such as a SecurityFilter, is a good option
	 *            since it is processed before the ServletHandler is executed.
	 *            
	 *            The changes to add security headers will be performed in the SecurityFilter class.
	 */
	//END Project 4, Milestone 2, Task 6
	
	
	/**
	 * Project 4, Milestone 3, Task 1
	 * 
	 * TITLE: 
	 * 
	 * RISK: 
	 * 
	 * REF: SonarSource RSPEC-
	 * 
	 * @param str
	 * @return String
	 */
	
	
	/**
	 * Project 4, Milestone 3, Task 2
	 * 
	 * TITLE: 
	 * 
	 * RISK: 
	 * 
	 * REF: SonarSource RSPEC-
	 * 
	 * @param str
	 * @return String
	 */
	
	
	/**
	 * Project 4, Milestone 3, Task 3
	 * 
	 * TITLE: 
	 * 
	 * RISK: 
	 * 
	 * REF: SonarSource RSPEC-
	 * 
	 * @param str
	 * @return String
	 */
	
	
	/**
	 * Project 4, Milestone 3, Task 4
	 * 
	 * TITLE: 
	 * 
	 * RISK: 
	 * 
	 * REF: SonarSource RSPEC-
	 * 
	 * @param str
	 * @return String
	 */
	
	
	/**
	 * Project 4, Milestone 3, Task 5
	 * 
	 * TITLE: 
	 * 
	 * RISK: 
	 * 
	 * REF: SonarSource RSPEC-
	 * 
	 * @param str
	 * @return String
	 */
	
	
	/**
	 * Project 4, Milestone 3, Task 6
	 * 
	 * TITLE: 
	 * 
	 * RISK: 
	 * 
	 * REF: SonarSource RSPEC-
	 * 
	 * @param str
	 * @return String
	 */
	
	
	/**
	 * Project 4, Milestone 3, Task 7
	 * 
	 * TITLE: Avoid leaking session data across servlet sessions
	 * 
	 * RISK: 
	 * 
	 * REF: CMU Software Engineering Institute MSC11-J
	 * 
	 * @param email
	 */
	/**
	 * Project 4, Milestone 1, Task 1
	 * 
	 * TITLE: Do not trust hidden forms
	 * 
	 * RISK: While hidden forms are not displayed in the web browser, they can still be manipulated by
	 *       the user and forged. Hidden forms should be sanitized just like all other data.
	 * 
	 * REF: CMU Software Engineering Institute IDS14-J
	 * 
	 * @param username
	 * @param password
	 * @param secureForm
	 * @return boolean
	 */
	public String emailLogin(String email, String password) throws AppException {		
		StringBuilder webappPath = new StringBuilder();
		webappPath.append(System.getProperty( "catalina.base" ));
		webappPath.append(File.separator + "webapps" + File.separator + 
				httpRequest.getServletContext().getContextPath() + File.separator);
		
		//make sure the string is not null
		if(email == null || password == null) {
			throw new AppException("emailLogin given a null value", "application error");
		}

		try {
			String passHash = encryptPassword(password);
			
			String userDbPath = webappPath.toString() + "resources/users.xml";
			
			//load the users xml file
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(userDbPath);

			//create an XPath for the expression
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			
			//create an instance of our custom resolver to add variables and set it to the xpath
			MapVariableResolver resolver = new MapVariableResolver();
			xpath.setXPathVariableResolver(resolver);
			
			//create the xpath expression with variables and map variables
			XPathExpression expression = xpath.compile("//users/user[email/text()=$email and password/text()=$password]");
			resolver.addVariable(null, "email", email);
			resolver.addVariable(null, "password", passHash);

			//login failed if no element was found
			if( expression.evaluate(doc, XPathConstants.NODE) == null) {
            	throw new AppException("logon failed with email: " + email, "application error");
            }
			else {
				return("logon success with email: " + email);
			}
		}
		catch(ParserConfigurationException | SAXException | XPathException xmle) {
			throw new AppException("emailLogin caught exception: " + xmle.getMessage(), "application error");
		}
		catch(IOException ioe) {
			throw new AppException("emailLogin caught IO exception: " + ioe.getMessage(), "application error");
		}

	}
	
	
	/**
	 * Project 4, Milestone 3, Task 8
	 * 
	 * TITLE: Securing Java Web Tokens (JWT)
	 * 
	 * RISK: JWT act as an authorization token guaranteeing the users access rights
	 *       such as an active session and the role. The JWT needs to have a strong
	 *       signature verification so the rights can be guaranteed
	 * 
	 * @param username
	 */

	public String createJwt(String username) throws AppException {
		final String SECRET="secret";
		
		try {
			//expire token in 15 minutes
			Calendar date = Calendar.getInstance();
			long t= date.getTimeInMillis();
			Date expirary=new Date(t + (15 * 60000));

			//create JWT header
			JsonObject jsonHeader = Json.createObjectBuilder()
					.add("alg", "none")
					.add("type", "JWT")
				.build();
			
			String jwtHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(
					jsonHeader.toString().getBytes(StandardCharsets.UTF_8)
				);
			
			//create JWT body
			JsonObject jsonBody = Json.createObjectBuilder()
					.add("sub", username)
					.add("exp", expirary.getTime())
					.add("attrs",Json.createObjectBuilder().build())
				.build();

			String jwtBody = Base64.getUrlEncoder().withoutPadding().encodeToString(
					jsonBody.toString().getBytes(StandardCharsets.UTF_8)
				);
			
			//build the header and body for signing
			//build the JWT
			StringBuffer sbJWT = new StringBuffer();
			sbJWT.append(jwtHeader);
			sbJWT.append(".");
			sbJWT.append(jwtBody);
			
			//create the HMAC signature
			byte[] hmacMessage = null;
		    try {		    	
		    	Mac mac = Mac.getInstance("HmacSHA256");
		        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
		        mac.init(secretKeySpec);
		        hmacMessage = mac.doFinal(sbJWT.toString().getBytes());
		    } 
		    catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException e) {
		    	throw new AppException("Failed to calculate hmac-sha256", e);
		    }

			String jwtSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(hmacMessage);
			
		    
			//add the signature to the string buffer
			sbJWT.append(".");
			sbJWT.append(jwtSignature);
			
			return sbJWT.toString();
		}
		catch(JsonException je) {
			je.printStackTrace();
			throw new AppException(je);
		}
	}
	
	
	/**
	 * Project 4, Milestone 4, Task 1
	 * 
	 * TITLE: Manage 3rd party libraries with Software Composition Analysis (SCA)
	 * 
	 * RISK: Including 3rd party libraries in a webapp may make it vulnerable especially if
	 *       the library can be accessed or caused to be used with the existing webapp.
	 *       
	 * IMPORTANT: No changes are required in this file. The SCA analysis reviews the 3rd 
	 *            party JAR files included.
	 */
	//END Project 4, Milestone 4, Task 1
	
	
	
	/**
	 * IMPORTANT: NO CODE NEEDS TO BE CHANGED BELOW THIS POINT
	 */
	
	
	/** The following method does not need to be assessed in the project and is only here as a helper function
	 * 
	 * Code copied from: https://rgagnon.com/javadetails/java-0596.html
	 * 
	 */
	private static class MapVariableResolver implements XPathVariableResolver {
		private Hashtable variables = new Hashtable();

		public void addVariable(String namespaceURI, String localName, Object value) {
			addVariable(new QName(namespaceURI, localName), value);
		}

		public void addVariable(QName name, Object value) {
			variables.put(name, value);
		}

		public Object resolveVariable(QName name) {
			Object retval = variables.get(name);
			return retval;
		}
	}
	
	/**
	 * The following method does not need to be assessed in the project and is only here as a helper function
	 * 
	 * Code copied from: https://rgagnon.com/javadetails/java-0596.html
	 * 
	 * @param b
	 * @return String
	 */
	private static String encryptPassword(String password) throws AppException {
		
	    try {
	    	//get an instance of the SHA-1 algo
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(password.getBytes(StandardCharsets.UTF_8));
	        
	        byte[] b = crypt.digest();
	        
			String sha1 = "";
			for (int i=0; i < b.length; i++) {
				sha1 += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
			}
			
	        return sha1;
	    }
	    catch(NoSuchAlgorithmException nse) {
	        throw new AppException("encryptPassword got algo exception: " + nse.getMessage(), "application error");
	    }

	}
}
