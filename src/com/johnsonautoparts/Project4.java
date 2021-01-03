package com.johnsonautoparts;

import java.io.File;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.johnsonautoparts.exception.AppException;


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
	 * Project 4 - Task #
	 * 
	 * More regex bypassing <sSCRIPTcript>
	 * CMU Software Engineering Institute STR02-J
	 * 
	 * @param str
	 * @return String
	 */
	
	public String project4TaskNNN(String str) {
		//TODO
		return str;
	}
	
	/*
	 * Leaking info in HttpServlet vs HttpSession MSC11-J
	 */
	public void httpServletData(HttpServlet httpServlet) {
		//TODO
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
}
