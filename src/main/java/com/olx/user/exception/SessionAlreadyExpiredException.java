package com.olx.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessionAlreadyExpiredException extends RuntimeException{
	
	private String message;
    public SessionAlreadyExpiredException() {
    	this.message="";
    }
	public SessionAlreadyExpiredException(String message) {
		super();
		this.message = message;
	}
	@Override
	public String toString() {
		return "SessionAlreadyExpiredException [message=" + message + "]";
	}

}
