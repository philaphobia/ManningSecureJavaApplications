package com.johnsonautoparts.servlet;

/**
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * Session Listener for for the start and ending of a user session.
 * This class registers into Tomcat via the web.xml and provides logs
 * to help in debugging the webapp.
 * 
 */ 
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.johnsonautoparts.logger.AppLogger;

public class SessionListener implements HttpSessionListener {
	
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		if(session != null) {
			try {
				//Connection connection = DB.getDbConnection(session);
				//session.setAttribute("connection", connection);
				AppLogger.log("Session Created.  ID: " + session.getId());
			}
			catch(Exception e) {
				AppLogger.log("Session destroyed with error.  ID: " + session.getId()
					+  "  Error: " + e.toString());
			}
		}
	}
 
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		if(session != null) {
			try {
				Object connObj = session.getAttribute("connection");
				if( connObj != null && connObj instanceof Connection ) {
					Connection connection = (Connection) connObj;
					
					if(connection != null) {
						connection.close();
					}
				}
				
				AppLogger.log("Session destroyed.  ID:"+session.getId());
			}
			catch(SQLException e) {
				AppLogger.log("Session destroyed with error.  ID: " +session.getId()
					+  "  Error: " + e.toString());
			}
		}
	}
     
}
