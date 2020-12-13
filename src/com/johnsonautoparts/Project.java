package com.johnsonautoparts;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

/**
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * This is an abstract class for all of the Project classes used during the
 * reflection call to have a common object for casting.
 *
 */
public abstract class Project {
	protected HttpSession httpSession=null;
	protected Connection connection=null;
	
	public Project(Connection connection, HttpSession httpSession) {
		this.httpSession = httpSession;
		this.connection = connection;
	}
}
