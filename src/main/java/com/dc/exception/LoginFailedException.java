package com.dc.exception;

public class LoginFailedException extends RuntimeException {
	public LoginFailedException() {

	}

	public LoginFailedException(String message) {
		super(message);
	}

	public LoginFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
