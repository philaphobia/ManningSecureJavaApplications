package com.johnsonautoparts;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.johnsonautoparts.exception.AppException;
import com.johnsonautoparts.logger.AppLogger;

/**
 * 
 * Project2 class which contains all the method for the milestones. The task number 
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
public class Project2 extends Project {
	
	public Project2(Connection connection, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		super(connection, httpRequest, httpResponse);
	}
	
	
	/**
	 * Project 2, Milestone 1, Task 1
	 * 
	 * TITLE: Protect the database from SQL injection
	 * 
	 * RISK: The id is received as a parameter from the website without any sanitization and placed
	 *       directly into a SQL query. This opens the method up to SQL injection if the user includes
	 *       a single quote to terminate the id and then adds their own clauses after.
	 * 
	 * REF: CMU Software Engineering Institute IDS00-J
	 * 
	 * @param query
	 * @return String
	 */
	public int dbInventory(String id) throws AppException {
		if (connection == null) {
	        throw new AppException("dbQuery had stale connection","application error");
		}

		//execute the SQL and return the count of the inventory
		try {
			String sql = "SELECT COUNT(id) FROM inventory WHERE id = " + id;
			
			try( Statement stmt = connection.createStatement() ) {
				try( ResultSet rs = stmt.executeQuery(sql) ) {
	   
					if (rs.next()) {
						return rs.getInt(1);
					}
					else {
						throw new AppException("dbInventory did not return any results", "application error");
					}
				}//end resultset
			}//end stmt
	   
		} catch (SQLException se) {
			throw new AppException("dbInventory caught SQLException: " + se.getMessage(), "application error");
		} 
		finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} 
			catch (SQLException se) {
				AppLogger.log("dbInventory failed to close connection: " + se.getMessage());
			}
		}
	}
	
	
	/**
	 * Project 2, Milestone 1, Task 2
	 * 
	 * TITLE: Avoid SQL injection protection errors
	 * 
	 * RISK: The id is received as a parameter from the website without any sanitization and placed
	 *       directly into a SQL query. The developer attempted to protect from SQL injection by using a
	 *       PreparedStatement which adds additional security compared to the previous task, but it is
	 *       still not correct.
	 * 
	 * REF: CMU Software Engineering Institute IDS00-J
	 * 
	 * @param taskName
	 * @return String
	 */
	public int dbTasks(String taskName) throws AppException {
		if (connection == null) {
	        throw new AppException("dbTasks had stale connection","application error");
		}
		
		//execute the SQL and return the count of the tasks
		try {
			String sql = "SELECT COUNT(task_name) FROM schedule WHERE task_name = '" + taskName + "'";
			try( PreparedStatement stmt = connection.prepareStatement(sql) ) {
				try ( ResultSet rs = stmt.executeQuery() ) {
	   
					if (rs.next()) {
						return rs.getInt(1);
					}
					else {
						throw new AppException("dbTasks did not return any results", "application error");
					}
				}//end resultset
			}//end preparedstatement
	   
		} catch (SQLException se) {
			throw new AppException("dbTasks caught SQLException: " + se.getMessage(), "application error");
		} 
		finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} 
			catch (SQLException se) {
				AppLogger.log("dbTasks failed to close connection: " + se.getMessage());
			}
		}
	}
	
	
	/**
	 * Project 2, Milestone 1, Task 3
	 * 
	 * TITLE: Safe naming for files
	 * 
	 * RISK: Filenames accepted from user input could allow for inject attacks and read/writing
	 *       arbitrary files. For the existing step we will work on the filename and in the next
	 *       task you will work on securing the path to a file.
	 * 
	 * REF: CMU Software Engineering Institute IDS50-J
	 * 
	 * @param str
	 * @return String
	 */
	public void createFile(String fileName) throws AppException {
		final String tempPath = "temp" + File.pathSeparator + "upload" + File.pathSeparator;
		
		HttpSession session = httpRequest.getSession();
		String content = null;
		
		//make sure session_data contains data
		if( session.getAttribute("session_data") == null) {
			throw new AppException("session_data is empty", "application error");
		}
		
		//make sure session_data is text
		if(session.getAttribute("session_data") instanceof String) {
			content = (String)session.getAttribute("session_data");
		}
		else {
			throw new AppException("session_data does not contain text", "application error");
		}
		

		/**
		 * For the current task, do not worry about fixing makeSafePath()
		 * This is an exercise for the next task. The current task is to only focus
		 * on creating a safe filename
		 */
		//full path to file to be created
		String filePath = null;
		try {
			//check the path
			filePath = makeSafePath(tempPath + fileName);
		}
		catch(IOException ioe) {
			throw new AppException("makeSafePath threw an IO error: " + ioe.getMessage(), "application error");
		}

		
		//write the session_data content to the file
		File f = new File(filePath);
		try (OutputStream out = new FileOutputStream(f) ) {
			out.write(content.getBytes(StandardCharsets.UTF_8));
		}
		catch(FileNotFoundException fnfe) {
			throw new AppException("createFile caught file not found: " + fnfe.getMessage(), "application error");
		}
		catch(IOException ioe) {
			throw new AppException("createFile caught IO error: " + ioe.getMessage(), "application error");
		}

	}
	
	
	/**
	 * Project 2, Milestone 1, Task 4
	 * 
	 * TITLE: Protecting file paths
	 * 
	 * RISK: A file path which includes input from a user can also contains malicious characters to perform
	 *       a bypass of file checks. The attacker could point to special files on the operating system
	 *       which would leak sensitive information.
	 * 
	 * REF: CMU Software Engineering Institute FIO16-J
	 * 
	 * @param str
	 * @return String 
	 */
	public String makeSafePath(String dirty) throws IOException {
		return dirty.replaceAll("\\.\\." + File.separator,"_");
	}
	
	
	/**
	 * Project 2, Milestone 1, Task 5
	 * 
	 * TITLE: Safe extraction of compressed files
	 * 
	 * RISK: Zip files can be used as an attack vector to overcome resources on a system and create
	 *       a denial of service. An example is a zip bomb which contains recursive files which when
	 *       extracted can fill up almost any modern disk storage. The size of entries need to checked
	 *       against a pre-established maximum size that the system will accept
	 * 
	 * REF: CMU Software Engineering Institute IDS04-J
	 * 
	 * @param str
	 * @return String
	 */
	public String unzip(String fileName) throws AppException {
		final int BUFFER = 512;
		final int OVERFLOW = 0x1600000; // 25MB
		final String tempPath = "temp" + File.pathSeparator + "zip";
		String zipPath = tempPath + File.pathSeparator + fileName + File.pathSeparator;
		
		//open a stream to the file
		try (FileInputStream fis = new FileInputStream(fileName)) {
			try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis)) ) {
				ZipEntry entry;

				//go through each entry in the file
				while ((entry = zis.getNextEntry()) != null) {
					AppLogger.log("Extracting zip from filename: " + fileName);
					int count;
					byte data[] = new byte[BUFFER];
					//avoid zip bombs by only allows reasonable size files
					if (entry.getSize() > OVERFLOW ) {
						throw new IllegalStateException("zip file exceed max limit");
					}
					//look for illegal size which may be a hint something is wrong
					if (entry.getSize() == -1) {
						throw new IllegalStateException("zip file entry returned inconsistent size and may be a zip bomb");
					}
					
					//output file is path plus entry
					try (FileOutputStream fos = new FileOutputStream(zipPath + entry.getName()) ) {
						try(BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER) ) {
							while ((count = zis.read(data, 0, BUFFER)) != -1) {
								dest.write(data, 0, count);
							}
							dest.flush();

							zis.closeEntry();
						}//end bufferedoutputstream
					}//end fileoutputstream
					
				} //end while entry
				
			}//end try zis
			catch(IllegalStateException ise) {
				throw new AppException("unzip caught strange behavior on zip file: " + ise.getMessage(), "application error");
			}
			catch(IOException ioe) {
				throw new AppException("unzip caught IO error: " + ioe.getMessage(), "application error");
			}
			
		}//end fis
		catch (FileNotFoundException fnfe) {
			throw new AppException("unzip caught file not found exception: " + fnfe.getMessage(), "application error");
		}
		catch (IOException ioe) {
			throw new AppException("unzip caught IO error: " + ioe.getMessage(), "application error");
		}

		//diretory to the extracted zip
		return zipPath;
	}

	  
	/**
	 * Project 2, Milestone 1, Task 6
	 * 
	 * TITLE: Sanitize data used in exec()
	 * 
	 * RISK: You should avoid using exec() unless no other alternatives are possible because injection
	 *       attacks allow code execution.
	 *       
	 * REF: CMU Software Engineering Institute IDS07-J
	 * 
	 * @param cmd
	 * @return String
	 */
	public String exec(String cmd) throws AppException {
		//execute the OS command
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(new String[] {"sh", "-c", cmd + " "});
			int result = proc.waitFor();
	    
			//throw error if any return code other than zero
			if (result != 0) {
				throw new AppException("process error: " + result, "application error");
			}
			InputStream in = proc.getInputStream();
	    	
			StringBuilder strBuilder = new StringBuilder();
			int i;

			//build string of the return data
			while ((i = in.read()) != -1) {
				strBuilder.append( (char) i );
			}
	    
			return strBuilder.toString();
		}
		catch(IOException ioe) {
			throw new AppException("exec caught IO error: " + ioe.getMessage(), "application error");
		}
		catch(InterruptedException ie) {
			throw new AppException("exec caught interupted error: " + ie.getMessage(), "application error");
		}
	}
	
	
	/**
	 * Project 2, Milestone 1, Task 7
	 * 
	 * TITLE: Sanitize data used in JavaScript engine
	 * 
	 * RISK: The ScriptEnginer in Java provides a JavaScript engine for interpreting code and executing.
	 *       Passing untrusted text with sanitization could allow and attacker to run code which executes
	 *       on the operating system in the internal network.
	 *       
	 * REF: CMU Software Engineering Institute IDS52-J
	 * 
	 * @param cmd
	 * @return String
	 */
	public String evalScript(String printMessage) throws AppException {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("javascript");
			Object ret = engine.eval("print('<tag>"+ printMessage + "</tag>')");
			
			//make sure data was returned
			if(ret == null) {
				throw new AppException("ScriptEngine in evalScript returned null", "application error");
			}
			
			//return the data but only if the contents are a string
			else if(ret instanceof String) {
				return (String)ret;
			}
			
			else {
				throw new AppException("Unknown object returned from evalScript: " + ret.getClass().toString(), "application error");
			}
		}
		catch(ScriptException se) {
			throw new AppException("evalScript caugth ScriptException: " + se.getMessage(), "application error");
		}
	}
	
	
	/**
	 * Project 2, Milestone 2, Task 1
	 * 
	 * TITLE: Prevent XML injection attacks
	 * 
	 * RISK: If a user can inject unchecked text which is processed by an XML parser
	 *       they can overwrite text or possibly gain unauthorized access to data fields.
	 *       The content placed into an XML document needs to be validated
	 * 
	 * REF: CMU Software Engineering Institute IDS16-J
	 * 
	 * @param str
	 * @return String
	 */
	public String createXML(String partQuantity) throws AppException {
		//build the XML document
		String xmlContent = "<?xml version=\"1.0\"?>"
				    + "<item>\n"
					+ "<title>Widget</title>\n"
			        + "<price>500</price>\n" 
					+ "<quantity>" + partQuantity + "</quantity>"
					+ "</item>";

		//build the XML document from the string content
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(xmlContent);
			doc = builder.parse(is);
		}
		catch(SAXException se) {
			throw new AppException("createXML could not validate XML: " + se.getMessage(), "application error");
		}
		catch(ParserConfigurationException pce) {
			throw new AppException("createXML caught parser exception: " + pce.getMessage(), "application error");
		}
		catch(IOException ioe) {
			throw new AppException("createXML caught IO exception: " + ioe.getMessage(), "application error");
		}
		
		//set the response header and return the XML
		httpResponse.setContentType("application/xml");
		return(doc.toString());
	}
	
	
	/**
	 * Project 2, Milestone 2, Task 2
	 * 
	 * TITLE: Validate with XML schema
	 * 
	 * RISK: For more complex XML documents or when adding multiple fields, an XML schema
	 *       should be used to validate all of the content.
	 * 
	 * REF: CMU Software Engineering Institute IDS16-J
	 * 
	 * @param str
	 * @return String
	 */
	public Document validateXML(String xml) throws AppException {
		final String xsdPath = "resources/schema.xsd";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		//the code for this XML parse is very rudimentary but is here for demonstration
		//purposes to work with XML schema validation
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new String(xml));
			return builder.parse(is);
		}
		catch(ParserConfigurationException | SAXException xmle) {
			throw new AppException("validateXML caught exception: " + xmle.getMessage(), "application error");
		}
		catch(IOException ioe) {
			throw new AppException("validateXML caught IO exception: " + ioe.getMessage(), "application error");
		}
	}
	
	
	/**
	 * Project 2, Milestone 2, Task 3
	 * 
	 * TITLE: Project against XML External Entity (XEE) attacks
	 * 
	 * RISK: If a user can add external entities to an XML document they could possibly execute
	 *       code on the operating system which opens the application to a critical risk.
	 * 
	 * REF: CMU Software Engineering Institute IDS17-J
	 * 
	 * @param str
	 * @return String
	 */
	public Document parseXML(String xml) throws AppException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		//the code for this XML parse is very rudimentary but is here for demonstration
		//purposes to configure the parse to avoid XEE attacks
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new String(xml));
			return builder.parse(is);
		}
		catch(ParserConfigurationException | SAXException xmle) {
			throw new AppException("validateXML caught exception: " + xmle.getMessage(), "application error");
		}
		catch(IOException ioe) {
			throw new AppException("validateXML caught IO exception: " + ioe.getMessage(), "application error");
		}
	}
	
	
	/**
	 * Project 2, Milestone 2, Task 4
	 * 
	 * TITLE: Avoid XPath injection
	 * 
	 * RISK: XPath queries can be used similar to SQL injection to force untrusted text into a query which is
	 *       parsed dynamically and can be used to bypass authentication or gain unauthorized access to data
	 * 
	 * REF: CMU Software Engineering Institute IDS53-J
	 * 
	 * Source code from: https://wiki.sei.cmu.edu/confluence/display/java/IDS53-J.+Prevent+XPath+Injection
	 * 
	 * @param str
	 * @return boolean
	 */
	public boolean xpathLogin(String userPass) throws AppException {
		//create a path to the webapp
		StringBuilder webappPath = new StringBuilder();
		webappPath.append(System.getProperty( "catalina.base" ));
		webappPath.append(File.separator + "webapps" + File.separator + "SecureCoding" + File.separator);

		if(userPass == null) {
			throw new AppException("parseXPath given a null value", "application error");
		}
		try {
			//split the user and password string which was concatenated with a colon
			//we would normally do further checks on the values but are limiting check here to reduce the code
			String[] args = userPass.split(":");
			String username = args[0];
			String passHash = encryptPassword(args[1]);
			String userDbPath = webappPath.toString() + "resources/users.xml";
			
			//load the users xml files
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(userDbPath);
			
			//create an XPath query
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//users/user[username/text()='" +
			       username + "' and password/text()='" + passHash + "' ]");
			
			//obtain the results
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			//return boolean of the test
			return (nodes.getLength() >= 1);
		}
		catch(ParserConfigurationException | SAXException | XPathException xmle) {
			throw new AppException("xpathLogin caught exception: " + xmle.getMessage(), "application error");
		}
		catch(IOException ioe) {
			throw new AppException("xpathLogin caught IO exception: " + ioe.getMessage(), "application error");
		}
	}
	
	/**
	 * Project 2, Milestone 2, Task 5
	 * 
	 * 
	 * TITLE: Serialized object safety
	 * 
	 * RISK: Recently exploits have leveraged Java's automatic triggering of readObject to inject code execution
	 *       of a serialized object which usess another class with an exploit. Java objects should take care
	 *       when deserializing to understand the actual content before it is serialized into a Java object.
	 *       The exploit can allow code execution on the Java application server which can lead to total 
	 *       compromise.
	 * 
	 * REF: CMU Software Engineering Institute SER12-J
	 * 
	 * @param str
	 * @return String
	 */
	public Object deserializedObject(String base64Str) throws AppException {
		if(base64Str == null) {
			throw new AppException("deserializeObject received null base64 string", "application error");
		}
		
		//decode the base64 string
		byte[]decodedBytes = null;
		try {
			decodedBytes = Base64.getDecoder().decode(base64Str);
		}
		catch(IllegalArgumentException iae) {
			throw new AppException("deserializeObject caught exception decoding base64: " + iae.getMessage(), "application error");
		}
		
	    //deserialize the object
	    try (ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes)) {
	    	
	    	//wrap the OIS in the try to autoclose
	    	try (ObjectInputStream ois = new ObjectInputStream(bais)) {
	    		return ois.readObject();
	    	}
	    	catch(StreamCorruptedException sce) {
	    		throw new AppException("deserializedObject caugh stream exception: " + sce.getMessage(), "application error");
	    	}
	    	catch(ClassNotFoundException | InvalidClassException ce) {
	    		throw new AppException("deserializedObject caugh class exception: " + ce.getMessage(), "application error");
	    	}

	    }
	    catch(IOException ioe) {
	    	throw new AppException("deserializedObject caugh IO exception: " + ioe.getMessage(), "application error");
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