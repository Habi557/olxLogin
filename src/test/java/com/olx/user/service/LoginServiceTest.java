package com.olx.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.olx.user.dto.UserDto;
import com.olx.user.dto.UsersDto;
import com.olx.user.exception.InvalidUser;
import com.olx.user.security.JwtUtils;
import org.springframework.security.core.Authentication;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
  
  @Mock
  AuthenticationManager authenticatonManager;
  @Mock
  JwtUtils jwtUtils;
  @InjectMocks
  LoginServiceImpl loginServiceImpl;
  UsersDto userDto;
  Authentication usernamePasswordAuthenticationToken;
  @BeforeEach
  public void setUp() {
	   userDto = new UsersDto(1, "Habibulla", "786", "Male", "Admin", false,"abcdefgh");
		usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDto.getId(),userDto.getPassword());
  }
	@Test
	public void authenticateTest() {
		// Arrange
		
		//when(loginServiceImpl.authenticate(userDto)).thenReturn(userDto);
		when(authenticatonManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(usernamePasswordAuthenticationToken);
		when(jwtUtils.generateToken(userDto.getName())).thenReturn(userDto.getToken());
		//Act
		UsersDto loginUser = loginServiceImpl.authenticate(userDto);
		//Assertions
		assertEquals(loginUser.getId(), userDto.getId());
		
	
		
	}
	@ParameterizedTest
	@ValueSource(strings  = {"1","2","3"})
	public void AuthenticationExceptionTest(String userId) {
		// Arrange
		 when(authenticatonManager.authenticate(usernamePasswordAuthenticationToken)).thenThrow(new InvalidUser(userId));
		//doThrow(new InvalidUser(userDto.getId())).when(authenticatonManager.authenticate(usernamePasswordAuthenticationToken));
		//Act
		
		InvalidUser  invalidUserException = assertThrows(InvalidUser.class, ()->{
			loginServiceImpl.authenticate(userDto);
		});
		System.out.println("InvalidException :"+ invalidUserException.getMessage());
		System.out.println("userId: "+ userId);
		assertEquals(userId, invalidUserException.getMessage());
	}
	

}
