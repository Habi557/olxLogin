package com.olx.user.dto;

public class UsersDto {
	
	/*
	 * id: string,
    name: string,
      password: string,
      email: string,
      gender: string,
      role: string,
      isactive: boolean
	 */
	private int id;
	private String name;
	private String password;
	private String gender;
	private String role;
	private boolean isactive;
	private String token;
	public UsersDto(int id, String name, String password, String gender, String role, boolean isactive,String token) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.gender = gender;
		this.role = role;
		this.isactive = isactive;
		this.token=token;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isIsactive() {
		return isactive;
	}
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	

}
