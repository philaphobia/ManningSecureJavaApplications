package com.johnsonautoparts.donotuse;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.johnsonautoparts.logger.AppLogger;

@WebServlet(name = "AdminServletHandler", urlPatterns = {"/admin"})
/**
@ServletSecurity(
        value = @HttpConstraint(
                rolesAllowed = {
                        "manager"
                }),
        httpMethodConstraints = {
                @HttpMethodConstraint(value = "POST", rolesAllowed = {
                        "manager"
                })
        })
*/
public class AdminServletHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public AdminServletHandler() {
		super();
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config); 
	}
	
 	@Override
  	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 		AppLogger.log("doPost from admin server");
 		
 		/**
 		// Get Authorization header  
        String auth = req.getHeader("Authorization");  
        // Do we allow that user?  
        if (!allowUser(auth)) {
        	...process below...
        }
        
 		if (auth == null) {  
 			System.out.println("No Auth");
 			return false;    
 		}  
 		if (!auth.toUpperCase().startsWith("BASIC ")) { 
 			System.out.println("Only Accept Basic Auth");
 			return false;   
 		}  
        
 		// Get encoded user and password, comes after "BASIC "  
 		String userpassEncoded = auth.substring(6);  
 		// Decode it, using any base 64 decoder  
 		sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();  
 		String userpassDecoded = new String(dec.decodeBuffer(userpassEncoded));  
      
 		String account[] = userpassDecoded.split(":");
 		System.out.println("User = "+account[0]);
 		System.out.println("Pass = "+account[1]);
        
 		// Check our user list to see if that user and password are "allowed"  
 		if ("authorized".equals(validUsers.get(userpassDecoded))) {  
 			return true;  
 		} 
 		else {  
 			return false;  
 		}
        */
        
 	}
}
