package com.johnsonautoparts.exception;

/**
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * Common class for handling exceptions in the application.
 * 
 * The class extends the normal Exception by adding a private message which
 * can be contained in the object to maintain a separation between public messages
 * returned to users and sensitive internal information used for debugging or logging
 *
 */
public class AppException extends Exception {
	private static final long serialVersionUID = 1L;
	private String privateMessage;
	
	public AppException() {}
	
	public AppException (String privateMessage, String publicMessage) {
		super(publicMessage);
		this.privateMessage = privateMessage;
	}
	
	public AppException (Throwable cause) {
		super(cause);
	}
	
	public AppException (String message, Throwable cause) {
		super(message, cause);
	}
	
	public String getPrivateMessage() {
		return privateMessage;
	}
}
