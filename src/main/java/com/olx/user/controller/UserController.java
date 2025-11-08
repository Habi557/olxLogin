package com.olx.user.controller;

import java.util.List;
import java.util.Map;

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
import com.olx.user.exception.TokenAlreadyExpired;
import com.olx.user.security.JwtUtils;
import com.olx.user.service.LoginService;
import com.olx.user.service.LoginServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/olx/user")
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
	    private int requestCount = 0;


	
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
	@GetMapping(value="/getuser")
	public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token){
		UserDto userDtoList = loginServiceImpl.getUser(token);
		if(userDtoList != null) {
			 return new ResponseEntity<UserDto>(userDtoList, HttpStatus.ACCEPTED);
		}
		 return new ResponseEntity<UserDto>(userDtoList, HttpStatus.BAD_REQUEST);


	}
	@GetMapping("/token/validate")
	public ResponseEntity<Boolean> isTokenValid(@RequestHeader("Authorization") String authHeader,@RequestHeader("EXPECTED_ROLES") String expectedRoles){
		 logger.info("authHeader",authHeader);
		 return new ResponseEntity<Boolean>(loginServiceImpl.isTokenValid(authHeader,expectedRoles), HttpStatus.ACCEPTED);

	}
	
	
	@GetMapping("/forgetpassword/{email}")
	public ResponseEntity<Boolean> validateUserName(@PathVariable("email") String email){
		Boolean validateEmail = loginServiceImpl.validateUserName(email);
		System.out.println("Test123");
		if(validateEmail) {
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
	
		
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
	@PostMapping("/resetpassword")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String,String> request){
		String token = request.get("token");
		String password = request.get("password");
		Boolean resetPasswordStatus = loginServiceImpl.resetPassword(token,password);
		if(resetPasswordStatus) {
			return ResponseEntity.ok("Password reset successful!");
		}
		return ResponseEntity.ok("Password reset Unsuccessful!");
	}
	@GetMapping("/test-failure")
	public ResponseEntity<String> fail() {
        requestCount++;
        
        // Simulate failure 80% of the time
        if (requestCount % 5 != 0) { // Only every 5th request succeeds
            throw new RuntimeException("Simulated service failure - Request: " + requestCount);
        }
        
        return ResponseEntity.ok("Success! Request: " + requestCount);
	}
    @GetMapping("/test-slow")
    public String slowEndpoint() throws InterruptedException {
       // Thread.sleep(3000); // 3 second delay
        return "Slow response after 3 seconds";
    }


	
	 

}
