package com.olx.user.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
 
	@ExceptionHandler(value = SessionAlreadyExpiredException.class)
	public ResponseEntity<Object> handleSessionAlreadyExpiredException(RuntimeException exception, WebRequest request) {
		String errorMessage = "{\"error\":" + exception.toString() + " }";
		ResponseEntity<Object> response = handleExceptionInternal(exception, errorMessage, new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
		return response;
	}
	@ExceptionHandler(value = InvalidTokenException.class)
	public ResponseEntity<Object> handleInvalidTokenException(RuntimeException exception, WebRequest request) {
		String errorMessage = "{\"error\":" + exception.toString() + " }";
		ResponseEntity<Object> response = handleExceptionInternal(exception, errorMessage, new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
		return response;
	}
	@ExceptionHandler(value = InvalidUser.class)
	public ResponseEntity<String> handleInvalidUserException(RuntimeException exception, WebRequest request) {
		String errorMessage = "{\"error\":" + exception.toString() + " }";
		//ResponseEntity<String> response = handleExceptionInternal(exception, errorMessage, new HttpHeaders(),
				//HttpStatus.BAD_REQUEST, request);
		return new ResponseEntity<String>(errorMessage,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(value = TokenAlreadyExpired.class)
	public ResponseEntity<Object> handleTokenAlreadyExpiredException(RuntimeException exception, WebRequest request) {
		String errorMessage = "{\"error\":" + exception.toString() + " }";
		ResponseEntity<Object> response = handleExceptionInternal(exception, errorMessage, new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
		return response;
	}
	@ExceptionHandler(value=InvalidOtp.class)
	public ResponseEntity<Object> handleInvlaidOtp(RuntimeException exception,WebRequest request){
		String errorMessage = "{\"error\":" + exception.toString() + " }";
		ResponseEntity<Object> response = handleExceptionInternal(exception, errorMessage, new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
		return response;
	}
	@ExceptionHandler(value=ExpiredJwtException.class)
	public Map<String,String> handleExpiredJwtException(ExpiredJwtException ex){
		Map<String,String> errorMap= new HashMap();
    	errorMap.put("message", "Token Expired Please Login again");
    	ObjectMapper objectMapper = new ObjectMapper();
        return errorMap;          		
		
	}
	
}
 
