package com.johnsonautoparts.exception;

/*
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * Common class for handling exceptions in the application.
 * 
 * The class extends the normal Exception by adding a private message which can
 * be contained in the object to maintain a separation between public messages
 * returned to users and sensitive internal information used for debugging or
 * logging
 *
 */
public class AppException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String PUBLIC_MESSAGE = "application error";
	private final String privateMessage;

	public AppException(String privateMessage) {
		super(PUBLIC_MESSAGE);
		this.privateMessage = privateMessage;
	}

	public AppException(String privateMessage, String publicMessage) {
		super(publicMessage);
		this.privateMessage = privateMessage;
	}

	public String getPrivateMessage() {
		return privateMessage;
	}
}
