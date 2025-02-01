package com.olx.user.service;

import java.util.List;

import com.olx.user.dto.UserDto;
import com.olx.user.dto.UsersDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
	public UsersDto authenticate(UsersDto userDto);
	public Boolean logout(Boolean tokenValidity,String token);
	public UserDto register(UserDto userDto);
	public List<UserDto> getUser(String token);
	public Boolean isTokenValid(String token,String expectedRoles);
	public Boolean validateUserName(String userName);
	public Boolean validateOtp(int otp,String email);





}
