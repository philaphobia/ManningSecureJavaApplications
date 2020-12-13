
package com.johnsonautoparts;

/**
 * 
 * Project3 class which contains all the method for the milestones. The task number 
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
import java.sql.Connection;

import javax.servlet.http.HttpSession;

import com.johnsonautoparts.db.DB;


public class Project3 extends Project {

	public Project3(Connection connection, HttpSession httpSession) {
		super(connection, httpSession);
	}
	
	
	/**
	 * Project 3 - Task 1
	 * 
	 * First defense of SQL injection
	 * PreparedStatement and Parameterized queries
	 * CMU Software Engineering Institute IDS00-J
	 * 
	 * @param str
	 * @return String
	**/
	public String task1(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 3 - Task 2
	 * 
	 * Additional SQL injection protection?
	 * 
	 * @param str
	 * @return String
	 */
	public String task2(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 3 - Task 3
	 * 
	 * SQL injection protection for output
	 * 
	 * @param str
	 * @return String
	 */
	public String task3(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 3 - Task 4
	 * 
	 * Protecting file paths
	 * CMU Software Engineering Institute FIO16-J
	 * 
	 * @param str
	 * @return String 
	 */
	public String task4(String str) {
		//TODO
	    return str;
	}
	
	
	/**
	 * Project 3 - Task 5
	 * 
	 * Safe extraction of compressed files
	 * CMU Software Engineering Institute IDS04-J
	 * 
	 * @param str
	 * @return String
	 */
	public String task5(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 3 - Task 6
	 * 
	 * Safe naming for files
	 * CMU Software Engineering Institute FIO99-J
	 * 
	 * @param str
	 * @return String
	 */
	public String task6(String str) {
		//TODO
		return str;
	}
	
	
	
	/**
	 * Project 3 - Task 7
	 * 
	 * Maintain encoding on file input and output
	 * CMU Software Engineering Institute STR04-J
	 * 
	 * @param str
	 * @return String
	 */
	public String task7(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 3 - Task 8
	 * 
	 * Prevent XML injection attacks
	 * CMU Software Engineering Institute IDS16-J
	 * 
	 * @param str
	 * @return String
	 */
	public String task8(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 3 - Task 9
	 * 
	 * Logging unsanitized input
	 * CMU Software Engineering Institute IDS03-J
	 * 
	 * @param str
	 * @return String
	 */
	public String task9(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 3 - Task 10
	 * 
	 * XML External Entity (XEE) attacks
	 * CMU Software Engineering Institute IDS17-J
	 * 
	 * @param str
	 * @return String
	 */
	public String task10(String str) {
		//TODO
		return str;
	}
	
	
}
