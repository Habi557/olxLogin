package com.olx.user.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.olx.user.dto.UserDto;
import com.olx.user.dto.UsersDto;
import com.olx.user.entity.TestEntity;
import com.olx.user.security.JwtUtils;
import com.olx.user.service.LoginService;
import com.olx.user.service.LoginServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/olx/user")
@CrossOrigin
public class UserController {
	 
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	AuthenticationManager authenticatonManager;
	
	@Autowired
	LoginService loginServiceImpl;
	 Logger logger = LoggerFactory.getLogger(UserController.class);

	
	@PostMapping(value="/authenticate" , consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
	public ResponseEntity<UsersDto> authenticate(@RequestBody UsersDto userDto){
		System.out.println("test for angular application");
		UsersDto dto=loginServiceImpl.authenticate(userDto);
		if(dto.getToken() !=null || dto.getToken()!="") {
			 return new ResponseEntity<UsersDto>(dto, HttpStatus.OK);

		}
		 return new ResponseEntity<UsersDto>(HttpStatus.BAD_REQUEST);
		
	}
	/*@PostMapping(value="/authenticate")
	public ResponseEntity<UserDto> authenticatre(@RequestBody UserDto userDto){
		try {
			authenticatonManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(),userDto.getPassword()));
			String generateToken = jwtUtils.generateToken(userDto.getUserName());
			userDto.setToken(generateToken);
			return new ResponseEntity<UserDto>(userDto,HttpStatus.OK);

		}
		catch(AuthenticationException e) {
			return new ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST);

		}
	}*/
	
	@DeleteMapping(value="/logout")
	public ResponseEntity<Boolean> logout(@RequestHeader("Authorization") String token){
		Boolean tokenValidity = loginServiceImpl.isTokenValid(token, "ROLE_ADMIN");
	 return new ResponseEntity<Boolean>(loginServiceImpl.logout(tokenValidity,token), HttpStatus.ACCEPTED);
	}
	
	//@PostMapping(value="/register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
	@PostMapping(value="/register")
	public ResponseEntity<UserDto> register(@RequestBody UserDto userDto){
	 return new ResponseEntity<UserDto>(loginServiceImpl.register(userDto), HttpStatus.CREATED);
	}
	@GetMapping(value="/user")
	public ResponseEntity<List<UserDto>> getUser(@RequestHeader("Authorization") String token){
		List<UserDto> userDtoList = loginServiceImpl.getUser(token);
		if(userDtoList.size()>0) {
			 return new ResponseEntity<List<UserDto>>(userDtoList, HttpStatus.ACCEPTED);
		}
		 return new ResponseEntity<List<UserDto>>(userDtoList, HttpStatus.BAD_REQUEST);


	}
	@GetMapping("/token/validate")
	public ResponseEntity<Boolean> isTokenValid(@RequestHeader("Authorization") String authHeader,@RequestHeader("EXPECTED_ROLES") String expectedRoles){
		 logger.info("authHeader",authHeader);
		 return new ResponseEntity<Boolean>(loginServiceImpl.isTokenValid(authHeader,expectedRoles), HttpStatus.ACCEPTED);

	}
		/*@GetMapping(value="/token/validate")
		public ResponseEntity<Boolean> isTokenValid(@RequestHeader("Authorization") String authHeader,
				@RequestHeader("EXPECTED_ROLES") String expectedRoles) {
	 
	        String token = null;
	        String username = null;
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            token = authHeader.substring(7);
	            username = jwtUtils.extractUsername(token);
	        }
			if(username!=null) {
	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	            if (jwtUtils.validateToken(token, userDetails)) {
	            	String authorities = userDetails.getAuthorities().toString();
	            	String roleArray[] = expectedRoles.split(",");
	            	for(String role: roleArray) {
	            		if(authorities.contains(role)) {
	            			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	            		}
	            	}
	            }
			}
			return new ResponseEntity<Boolean>(false, HttpStatus.FORBIDDEN);
			
		}*/
	
	@GetMapping("/forgetpassword/{email}")
	public ResponseEntity<Boolean> validateUserName(@PathVariable("email") String email){
		Boolean validateEmail = loginServiceImpl.validateUserName(email);
		System.out.println("Test");
		if(validateEmail) {
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
	
		
	}
	
	// verifying the otp
	@GetMapping("/verifyotp/{otp}/{email}")
	public ResponseEntity<Boolean> validateOtp(@PathVariable("otp") int otp,@PathVariable("email") String email){
		
		Boolean validateOtp = loginServiceImpl.validateOtp(otp,email);
		
		
		if(validateOtp) {
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);		
	}
	
	 

}
