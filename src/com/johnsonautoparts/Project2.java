
package com.johnsonautoparts;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import javax.servlet.http.HttpSession;

/**
 * 
 * Project2 class which contains all the method for the milestones. The task number 
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
public class Project2 extends Project {
	
	public Project2(Connection connection, HttpSession httpSession) {
		super(connection, httpSession);
	}
	
	
	/**
	 * Project 2 - Task 1
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
	 * Project 2 - Task 2
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
	 * Project 2 - Task 3
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
	 * Project 2 - Task 4
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
	 * Project 2 - Task 5
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
	 * Project 2 - Task 6
	 * Avoid passing untrusted data to exec
	 * Avoid using exec unless no other alternative
	 * CMU Software Engineering Institute IDS07-J
	 * 
	 * @param str
	 * @return String
	 * @throws IOException, InterruptedException 
	 */
	public String exec(String str) throws IOException, InterruptedException {
		//TODO
	    String dir = System.getProperty("dir");
	    Runtime rt = Runtime.getRuntime();
	    Process proc = rt.exec(new String[] {"sh", "-c", "ls " + dir});
	    int result = proc.waitFor();
	    
	    if (result != 0) {
	    	System.out.println("process error: " + result);
	    }
	    InputStream in = (result == 0) ? proc.getInputStream() : proc.getErrorStream();
	    
	    StringBuilder strBuilder = new StringBuilder();
	    int i;

	    while ((i = in.read()) != -1) {
	    	strBuilder.append( (char) i );
	    }
	    
	    return strBuilder.toString();
	}
	
	
	/**
	 * Project 2 - Task 6
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
	 * Project 2 - Task 7
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
	 * Project 2 - Task 8
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
	 * Project 2 - Task 9
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
	 * Project 2 - Task 10
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
	
	
	/**
	 * Project 2 - Task 11
	 * 
	 * 
	 * Serialized object safety
	 * CMU Software Engineering Institute SER02-J, 03-J, 04-J, 12-J, 58-J
	 * Use SerialKiller to protect?
	 * 
	 * @param str
	 * @return String
	 */
	public String task11(String str) {
		//TODO
		return str;
	}
	
}
