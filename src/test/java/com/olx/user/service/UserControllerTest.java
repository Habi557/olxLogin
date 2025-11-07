package com.olx.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olx.user.controller.UserController;
import com.olx.user.dto.UserDto;
import com.olx.user.dto.UsersDto;
import com.olx.user.security.JwtUtils;

@WebMvcTest(UserController.class)
public class UserControllerTest {
  @Autowired
  MockMvc mockMvc;
  @MockBean
  LoginService loginService;
  @MockBean
  JwtUtils jwtUtils;
  @MockBean
  UserDetailsService userDetailsService;
  @MockBean
  AuthenticationManager authenticatonManager;


  

  
  @Test
  public void authenticateTest() throws Exception {
	  UsersDto userDto = new UsersDto(1, "Habibulla", "786", "Male", "Admin", false,"abcdefgh");
	 // RequestBuilder requestBuilder =  post()
	   // Convert DTO to JSON string for the request body
      ObjectMapper objectMapper = new ObjectMapper();
      String userDtoJson = objectMapper.writeValueAsString(userDto);
	  when(loginService.authenticate(any(UsersDto.class))).thenReturn(userDto);
	  MockHttpServletResponse response = mockMvc.perform(post("/authenticate")
			  .contentType(MediaType.APPLICATION_JSON)
			  .header("Authorization", "Bearer abcdefgh")
              .content(userDtoJson))
	   .andReturn().getResponse();
	  System.out.println("test "+ response.getContentAsString());
      UsersDto resultValue = objectMapper.readValue(response.getContentAsString(), UsersDto.class);

	  assertEquals("Names are not equal","Habibulla",resultValue.getName());
	  assertEquals("Tokens are not equal","abcdefgh",resultValue.getToken());
	  
  }


}
