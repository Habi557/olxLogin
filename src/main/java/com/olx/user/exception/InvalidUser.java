package com.olx.user.exception;

public class InvalidUser extends RuntimeException {

	private String message;
	public InvalidUser() {}
	public InvalidUser(String message) {
		super(message);
		this.message = message;
	}
	@Override
	public String toString() {
		return "InvalidUser [message=" + message + "]";
	};
	
	
}
