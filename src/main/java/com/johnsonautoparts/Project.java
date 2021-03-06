package com.johnsonautoparts;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * This is an abstract class for all of the Project classes used during the
 * reflection call to have a common object for casting.
 *
 */
public abstract class Project {
	protected HttpServletRequest httpRequest = null;
	protected HttpServletResponse httpResponse = null;
	protected Connection connection = null;

	protected Project(Connection connection, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		this.httpRequest = httpRequest;
		this.httpResponse = httpResponse;
		this.connection = connection;
	}
}
