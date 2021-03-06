package com.johnsonautoparts.db;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
/**
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * Class for handling database connections and calls when needed
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.johnsonautoparts.exception.DBException;
import com.johnsonautoparts.logger.AppLogger;

public class DB {
	/*
	 * Method to instantiate a connection to the database and store the
	 * connection object with the session.
	 * 
	 * This class is not part of the course that needs to be reviewed or
	 * updated. It is only here to facilitate DB queries.
	 * 
	 * @param session
	 * @return Connection
	 * @throws DBException
	 **/
	public static Connection getDbConnection(HttpSession session)
			throws DBException {
		try {
			// get the context path for the webapp deployment name
			ServletContext context = session.getServletContext();

			// define the JDBC driver class
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver")
					.getDeclaredConstructor().newInstance();

			// build the connection string to the file in the webapp
			StringBuilder jdbcStrBuff = new StringBuilder();
			jdbcStrBuff.append("jdbc:derby:");
			jdbcStrBuff.append(System.getProperty("catalina.home")
					+ File.separator + "webapps" + context.getContextPath()
					+ File.separator + "db");

			AppLogger.log(
					"Initializing Derby DB at path: " + jdbcStrBuff.toString());
			// create a connection to the db
			Connection connection = DriverManager
					.getConnection(jdbcStrBuff.toString());

			// Store the connection in the session if needed
			session.setAttribute("connection", connection);

			return connection;
		} catch (ClassNotFoundException cnfe) {
			AppLogger.log("DB exception because class not found. ID: "
					+ session.getId() + " Error: " + cnfe.getMessage());

			throw new DBException(cnfe);
		} catch (InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException ce) {
			AppLogger.log("DB exception accessing class. ID: " + session.getId()
					+ " Error: " + ce.getMessage());

			throw new DBException(ce);
		}

		catch (SQLException se) {
			AppLogger.log("DB exception getting connection. ID: "
					+ session.getId() + " Error: " + se.getMessage());

			throw new DBException(se);
		}

	}

	/*
	 * Method to close the database connection in the session
	 * 
	 * @param session
	 * @throws DBException
	 */
	public static void closeDbConnection(HttpSession session)
			throws DBException {
		try {
			Connection connection = (Connection) session
					.getAttribute("connection");

			if (connection != null) {
				connection.close();
			}

			AppLogger.log("DB connection destroyed. ID: " + session.getId());
		} catch (SQLException se) {
			AppLogger.log("DB not destroyed with error. ID: " + session.getId()
					+ "Error: " + se.getMessage());

			throw new DBException("Caught exception closing the database: "
					+ se.getMessage());
		}
	}
}
