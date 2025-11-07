package com.olx.user.dto;

import lombok.Data;


public class UserDto {
private int id;
private String firstName;
private String lastName;
private String userName;
private String password;
private String email;
private String roles;
private String token;
private int otp;
public UserDto() {
	super();
	// TODO Auto-generated constructor stub
}

public UserDto(String firstName, String lastName, String userName, String password, String email, String roles,
		String token) {
	super();
	this.firstName = firstName;
	this.lastName = lastName;
	this.userName = userName;
	this.password = password;
	this.email = email;
	this.roles = roles;
	this.token = token;
}

public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}

public String getRoles() {
	return roles;
}

public void setRoles(String roles) {
	this.roles = roles;
}

public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}

public int getOtp() {
	return otp;
}

public void setOtp(int otp) {
	this.otp = otp;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}





}
