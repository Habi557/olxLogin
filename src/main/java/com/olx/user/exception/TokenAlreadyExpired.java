package com.olx.user.exception;

public class TokenAlreadyExpired extends RuntimeException{

	private String message;
	public TokenAlreadyExpired() {}
	public TokenAlreadyExpired(String message) {
		super(message);
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "TokenAlreadyExpired [message=" + message + "]";
	};
	
	
}
