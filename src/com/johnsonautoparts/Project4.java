package com.johnsonautoparts;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

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
	
	public Project4(Connection connection, HttpSession httpSession) {
		super(connection, httpSession);
	}
	
	public enum Status {
		OK,
		ERROR
	};
	
	Status status;
	String error;
	
	public void setStatus(Status status) {
		this.status=status;
	}
	
	public void setError(String error) {
		this.error=error;
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
}
