package com.olx.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTokenException extends RuntimeException{

	
	
	private String message;
    public InvalidTokenException() {
    	this.message="";
    }
	public InvalidTokenException(String message) {
		super();
		this.message = message;
	}
	@Override
	public String toString() {
		return "InvalidTokenException [message=" + message + "]";
	}
}
