package com.olx.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="USERS")
public class UserEntity {
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
private int id;	
@Column(name = "firstname")
private String firstName;
@Column(name = "lastname")
private String lastName;
@Column(name = "username", unique = true, nullable = false)
private String userName;
@Column(name = "password")
private String password;
@Column(name = "email")
private String email;
@Column(name="roles")
private String roles;
private String token;
@Column(name="otp")
private int otp;
public UserEntity() {
	super();
	// TODO Auto-generated constructor stub
}
public UserEntity(String firstName, String lastName, String userName, String password, String email) {
	super();
	this.firstName = firstName;
	this.lastName = lastName;
	this.userName = userName;
	this.password = password;
	this.email = email;
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
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getRoles() {
	return roles;
}
public void setRoles(String roles) {
	this.roles = roles;
}
public int getOtp() {
	return otp;
}
public void setOtp(int otp) {
	this.otp = otp;
}





}
