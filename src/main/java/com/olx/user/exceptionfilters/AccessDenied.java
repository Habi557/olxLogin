package com.olx.user.exceptionfilters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class AccessDenied implements AccessDeniedHandler {

	public AccessDenied() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");

	        // Create custom error message
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("error", "No Permission to access the resourse");
	        errorResponse.put("path", request.getRequestURI());
	        errorResponse.put("timestamp", System.currentTimeMillis());

	        // Write the JSON response
	        ObjectMapper objectMapper = new ObjectMapper();
	        response.getWriter().write(objectMapper.writeValueAsString(errorResponse)); 
		
	}

}
