package com.johnsonautoparts.exception;

/*
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * A class to distinguish database exceptions from common exceptions from common
 * application exceptions.
 * 
 */
public class DBException extends Exception {
	private static final long serialVersionUID = 1L;

	public DBException() {
	}

	public DBException(String message) {
		super(message);
	}

	public DBException(Throwable cause) {
		super(cause);
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}
}
