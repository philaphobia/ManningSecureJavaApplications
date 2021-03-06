package com.johnsonautoparts.servlet;

/*
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

import com.johnsonautoparts.db.DB;
import com.johnsonautoparts.exception.DBException;
import com.johnsonautoparts.logger.AppLogger;

public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();

		if (session != null) {
			// Generate random 256-bit (32-byte) shared secret and add to
			// session
			session.setAttribute("secret", ServletUtilities.createSecret());

			// add a connection to the database to session attribute
			try {
				Connection connection = DB.getDbConnection(session);
				session.setAttribute("connection", connection);
				AppLogger.log("Session Created.  ID: " + session.getId());
			} catch (IllegalStateException ise) {
				AppLogger.log("Session created with error.  ID: "
						+ session.getId() + "  Error: " + ise.toString());
			} catch (DBException dbe) {
				AppLogger.log("Session create exception ID: " + session.getId()
						+ "  Error: " + dbe.toString());
			}
		}
	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();

		if (session != null) {
			try {
				// delete the secret key
				session.setAttribute("secret", "");

				// destroy the db connection
				Object connObj = session.getAttribute("connection");
				if (connObj instanceof Connection) {
					Connection connection = (Connection) connObj;
					AppLogger.log(
							"Closing DB connection for ID: " + session.getId());

					if (connection != null) {
						connection.close();
					}
				}

				AppLogger.log("Session destroyed.  ID: " + session.getId());
			} catch (IllegalStateException ise) {
				AppLogger.log("Session destroy with error.  ID: "
						+ session.getId() + "  Error: " + ise.toString());
			} catch (SQLException e) {
				AppLogger.log("Session destroy exception ID: " + session.getId()
						+ "  Error: " + e.toString());
			}
		}
	}

}
