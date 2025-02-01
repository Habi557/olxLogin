package com.olx.user.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		String logout=request.getHeader("logout");
		try {
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				username = jwtService.extractUsername(token);
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtService.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException e) {
			System.out.println("token val");
			handleExpiredToken(response, e);

		} catch (Exception e) {
			System.out.println("token val2");

			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}

	}

	// Custom method to handle expired tokens
	private void handleExpiredToken(HttpServletResponse response, ExpiredJwtException e) throws IOException {
		//response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		response.setContentType("application/json");
//		String jsonResponse ="Token has expired message Please log in again";
//		response.getWriter().write(jsonResponse);
		 response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write(" Token Expired Login again");
	}

}