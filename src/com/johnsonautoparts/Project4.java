package com.johnsonautoparts;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	private static final String REFERER_LOGIN = "login.jsp";
	private static final String REFERER_COMMENTS = "comments.jsp";
	
	
	public Project4(Connection connection, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		super(connection, httpRequest, httpResponse);
	}
	
	
	/**
	 * Project 4, Milestone 1, Task #
	 * 
	 * TITLE: Suppressing exceptions
	 * 
	 * RISK: Developers sometimes catch exceptions and only print the stack or simply ignore. A program
	 *       can become unstable if execution is allowed to pass an unexpected state. Malicious users
	 *       could also leverage the logic error to bypass controls.
	 * 
	 * REF: CMU Software Engineering Institute ERR00-J
	 * 
	 * @param query
	 * @return String
	 */
	public boolean login(String username, String password, String secureForm) throws AppException {
		String referer = httpRequest.getHeader("referer");
		if(referer == null) {
			throw new AppException("login() cannot retrieve referer header", "application error");
		}

		//check all whitelist referer login form
		if(!referer.contains(REFERER_LOGIN)) {
			throw new AppException("login() cannot validate referer header", "application error");
		}
				
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
	
	/*
	 * Leaking info in HttpServlet vs HttpSession MSC11-J
	 */
	public void httpServletData(String email) {
		
	}
	
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

	
	/*
	 * 
	 * Title: Avoid arbitrary file uploads
	 * 
	 * No restriction place on file upload
	 * HINTS: remove - content-type, upload size, 
	 * 
	 * REF: CMU SEI IDS56-J
	 * CODE: https://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
	 * 
	 */
	public void fileUpload(String str) throws AppException {
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
                
                // Write the file
                if( fileName.lastIndexOf("\\") >= 0 ) {
                	file = new File( "upload" + File.separator + fileName.substring( fileName.lastIndexOf("\\"))) ;
                }
                else {
                	file = new File( "upload" + File.separator + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                }
                
                fi.write( file ) ;
            }
		}
		catch(FileUploadException fue) {
            throw new AppException("Upload exception: " + fue.getMessage(), "Application error");
		}
		catch(NoSuchElementException nsee) {
            throw new AppException("Iterator caused exception: " + nsee.getMessage(), "Application error");
		}
		catch(Exception e) {
			throw new AppException("FileWrite caused exception: " + e.getMessage(), "Application error");
		}
	}
	
	
	/**
	 * Project 4, Milestone 2, Task #
	 * 
	 * TITLE: Do not trust referer header for security decisions
	 * 
	 * RISK: The referer header can be manipulated by a user and should be assumed to be tainted data.
	 *       Since the header is untrusted, it should not be used as a reference source for making
	 *       security deisions.
	 * 
	 * REF: SonarSource RSPEC-2089
	 * 
	 * @param comments
	 */
	public int comments(String comments) throws AppException {
		String referer = httpRequest.getHeader("referer");
		if(referer == null) {
			throw new AppException("commets() cannot retrieve referer header", "application error");
		}

		//check all whitelist referer login form
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
	
}
