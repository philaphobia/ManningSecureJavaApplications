
package com.johnsonautoparts;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.johnsonautoparts.exception.AppException;
import com.johnsonautoparts.logger.AppLogger;

/**
 * 
 * Project1 class which contains all the method for the milestones. The task number 
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
public class Project1 extends Project {	
	
	public Project1(Connection connection, HttpSession httpSession) {
		super(connection, httpSession);
	}


	/**
	 * Project 1 - Task 1
	 * 
	 * TITLE: Normalize strings before validation
	 * 
	 * RISK: Since the text is not normalized, the cleaning step
	 *       may not fixes and injections of invalid characters
	 * 
	 * REF: CMU Software Engineering Institute IDS01-J
	 * 
	 * @param str
	 * @return String
	**/
	public String normalizeString(String str) {
		//TODO
		Pattern pattern = Pattern.compile("[<&>]");
		Matcher matcher = pattern.matcher(str);
		String cleanStr = null;
		
		//variable str is potentially dirty with HTML or JavaScript tags
		if (matcher.find()) {
			cleanStr = str.replaceAll("<","&lt").replaceAll(">", "&rt").replaceAll("&","&amp");
			throw new IllegalStateException();
		}
		
		cleanStr = Normalizer.normalize(cleanStr, Form.NFKC);
		return cleanStr;
	}
	
	
	/**
	 * Project 1 - Task 2
	 * 
	 * TITLE: Avoid leaking data with Format string
	 * 
	 * RISK: Attackers could inject formatting strings into variable
	 *       which the application will process and leak
	 * 
	 * REF: CMU Software Engineering Institute IDS06-J
	 * 
	 * @param str
	 * @return String
	 * @throws IllegalStateException
	 */
	public void formatString(String str) throws IllegalStateException {
		System.out.format(str + " contains illegal content");
	}
	
	
	/**
	 * Project 1 - Task 3
	 * 
	 * TITLE: String modification before validation
	 * 
	 * RISK: An attacker may use Unicode or other special characters
	 *       which will help bypass filters, but are then removed later
	 *       and the attack succeeds. For example, text passed as "<scr!ipt>"
	 *       would bypass a check for "<script>" but a later step which then
	 *       removes the exclamation character would then enable the payload
	 *       as "<script">
	 * 
	 * REF: CMU Software Engineering Institute IDS11-J
	 * 
	 * @param str
	 * @return String
	 */
	public String validateString(String str) throws AppException {
	    // Simple pattern match to remove <script> tag
	    Pattern pattern = Pattern.compile("<script>");
	    Matcher matcher = pattern.matcher(str);
	    
	    if (matcher.find()) {
	      throw new AppException("validateString() identified script tag", "Invalid input");
	    }
	 
	    // Deletes noncharacter code points
	    String cleanStr = str.replaceAll("[\\p{Cn}]", "");
	    
		return cleanStr;
	}
	
	
	/**
	 * Project 1 - Task 4
	 * 
	 * TITLE: Sanitize data used in regular expressions
	 * 
	 * RISK: An attacker can inject regex into parameters if they know
	 *       the data is inserted in a regex expression. This may lead to leaking
	 *       of sensitive data or bypassing security checks.
	 * 
	 * REF: CMU Software Engineering Institute IDS08-J
	 * 
	 * @param str
	 * @return String
	 */
	public Boolean regularExpression(String str) {
		String regex = "(.* password\\[\\w+\\] .*)";
        Pattern searchPattern = Pattern.compile(regex);
        Matcher patternMatcher = searchPattern.matcher(str);
        
        //return boolean result of the find() operation
        return patternMatcher.find();
	}
	
	
	/**
	 * Project 1 - Task 5
	 * 
	 * TITLE: International string attacks
	 * 
	 * RISK: Use local when comparing strings for security checks to avoid attacks with
	 *       international characters.
	 * 
	 * ADDITIONAL: The error is in two places in this method
	 * 
	 * REF: CMU Software Engineering Institute STR02-J
	 * 
	 * @param str
	 * @return String
	 */
	public Boolean internationalization(String str) throws AppException {
		//check for script tag
		if (str.toLowerCase().contains("script")) {
		    throw new AppException("internationalization() found script tag", "application error");
		}
	
		//get the operating system
		String os = System.getProperty("os.name");
		String fileName="/dev/null";
		
		//select the correct file based on the operating system
		if(os.contains("indows")) {
			fileName = "NUL";
		}
		else if(os.contains("inux") || os.contains("ac")) {
			fileName = "/dev/null";
		}
		
		//write the text to file
		try (PrintWriter writer = new PrintWriter(new FileWriter(fileName)) ) {
			writer.println(str);
			return true;
		}
		catch (IOException ioe) {
			throw new AppException("IOException in internationaliation(): " + ioe.getMessage(), 
					"application error");
		}
	}
	
	
	/**
	 * Project 1 - Task 6
	 * 
	 * TITLE: Logging unsanitized input
	 * 
	 * RISK: A malicious user could inject multi-line text which could obfuscate
	         login or other errors and make them appear successful
	 * 
	 * ADDITIONAL: For bonus work, update the AppLogger class so developers do not have
		           to sanitize every log and it is done in the logging class
	 * 
	 * REF: CMU Software Engineering Institute IDS03-J
	 * 
	 * @param str
	 * @return String
	 */
	public void logUnsanitizedText(String unsanitizedText) {
		AppLogger.log("Error: " + unsanitizedText);

	}
	
	
	/**
	 * Project 1 - Task 7
	 * 
	 * Avoid regex bypasses
	 * TODO: WebGoat or JuiceShop?
	 * 
	 * @param str
	 * @return String
	 */
	public String regexBypass(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 1 - Task 9
	 * 
	 * Combining code with different encoding
	 * CMU Software Engineering Institute STR01-J
	 * 
	 * @param str
	 * @return String
	 */
	public String combineEncoding(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 1 - Task 10
	 * 
	 * Encoding non-character data as a string
	 * CMU Software Engineering Institute STR03-J
	 * 
	 * @param str
	 * @return String
	 */
	public String enocdeNonCharacter(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 1 - Task 11
	 * 
	 * Double encoding attacks
	 * 
	 * TODO: solution
	 * - https://owasp.org/www-community/Double_Encoding
	 * - https://github.com/OWASP/owasp-java-encoder/
	 * 
	 * @param str
	 * @return String
	 */
	public String doubleEncoding(String str) {
		//TODO
		return str;
	}
	
	

	
	
	/**
	 * Project 1 - Task 13
	 * 
	 * Preventing Integer overflow
	 * CMU Software Engineering Institute NUM00-J
	 * 
	 * @param num
	 * @return Integer
	 */
	public Integer task13(Integer num) {
		//TODO
		return num;
	}
	
	
	/**
	 * Project 1 - Task 14
	 * 
	 * Divide by zero errors
	 * CMU Software Engineering Institute NUM02-J
	 * 
	 * @param int
	 * @return Integer
	 */
	public Integer task14(Integer num) {
		//TODO
		return num;
	}
	
	
	/**
	 * Project 1 - Task 15
	 * 
	 * Avoid NaN calculations
	 * CMU Software Engineering Institute NUM07-J and NUM08-J
	 * 
	 * @param num
	 * @return Integer
	 */
	public String task15(String str) {
		//TODO
		return str;
	}
	
	
	/**
	 * Project 1 - Task 16
	 * 
	 * String representation of numbers
	 * CMU Software Engineering Institute NUM11-J
	 * 
	 * @param num
	 * @return Integer
	 */
	public String task16(String str) {
		//TODO
		return str;
	}
	
	
	//TODO
	/**
	 * Project 1 - Task 11?
	 * 
	 * Encode strings to avoid JavaScript and HTML tags
	 * https://github.com/OWASP/owasp-java-encoder/blob/main/core/src/main/java/org/owasp/encoder/Encode.java
	 * 
	 * @param str
	 * @return String
	 */
	public String TODO1(String str) {
		//TODO
		return str;
	}
	
	
}
