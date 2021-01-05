package com.johnsonautoparts;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.johnsonautoparts.exception.AppException;
import com.johnsonautoparts.logger.AppLogger;
import com.johnsonautoparts.servlet.ServletUtilities;



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
	private static final String REFERER_COMMENTS = "comments.jsp";
	
	
	public Project4(Connection connection, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		super(connection, httpRequest, httpResponse);
	}
	
	
	/**
	 * Project 4, Milestone 1, Task 1
	 * 
	 * TITLE: Do not trust hidden forms
	 * 
	 * RISK: While hidden forms are not displayed in the web browser, they can still be manipulated by
	 *       the user and forged. Trusting data from hidden fields to make security decisions is not
	 *       allowed.
	 * 
	 * REF: CMU Software Engineering Institute IDS14-J
	 * 
	 * @param username
	 * @param password
	 * @param secureForm
	 * @return boolean
	 */
	public boolean login(String username, String password, String secureForm) throws AppException {
		//validate secureForm is boolean
		if(! Boolean.parseBoolean(secureForm)) {
			throw new AppException("Login did not originate from secure form", "application error");
		}
		
		//Project2 object for xPath login
		Project2 project2 = new Project2(connection, httpRequest, httpResponse);
		String userPass = username + ":" + password;
		
		//process login
		return project2.xpathLogin(userPass);
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
	public void fileUpload(String str) throws AppException {
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

            }
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
			//write the printstack to a string for better debugging
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ise.printStackTrace(pw);
			String error = sw.toString();
			
			return(error);
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
	 * 
	 * IMPORTANT: For the following task you will be working on a JSP form at:
	 *            WebContent/jsp/blog.jsp
	 *            
	 *            The sanitization is applicable in Java as well if you are returning data which needs to
	 *            be encoded.
	 */
	public String postBlog(String blog) {
		return("Blog entry accepted");
	}

	
	/**
	 * Project 4, Milestone 2, Task 1
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
	 * Project 4, Milestone 2, Task 2
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
	 * Project 4, Milestone 2, Task 3
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
	 * Project 4, Milestone 2, Task 4
	 * 
	 * TITLE: Do not trust referer header for security decisions
	 * 
	 * RISK: The referer header can be manipulated by a user and should be assumed to be tainted data.
	 *       Since the header is untrusted, it should not be used as a reference source for making
	 *       security decisions.
	 * 
	 * REF: SonarSource RSPEC-2089
	 * 
	 * @param comments
	 * @return int
	 */
	public int comments(String comments) throws AppException {
		String referer = httpRequest.getHeader("referer");
		if(referer == null) {
			throw new AppException("commets() cannot retrieve referer header", "application error");
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
				return stmt.executeUpdate();
			}
	   
		} catch (SQLException se) {
			throw new AppException("comments caught SQLException: " + se.getMessage(), "application error");
		} 
		finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} 
			catch (SQLException se) {
				AppLogger.log("comments failed to close connection: " + se.getMessage());
			}
		}
	}
	
	
	/**
	 * Project 4, Milestone 2, Task 5
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
	 * Project 4, Milestone 2, Task 6
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
	public void httpServletData(String email) {
		
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
	
}
