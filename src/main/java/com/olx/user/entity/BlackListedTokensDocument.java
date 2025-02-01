package com.olx.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.GeneratedValue;

@Document(value="blacklisted_tokens")
public class BlackListedTokensDocument {
	@Id
	private int id;
    private String token;
    private LocalDateTime naturaExpiredDate;
    public BlackListedTokensDocument() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BlackListedTokensDocument(int id,String token, LocalDateTime naturaExpiredDate) {
		super();
		this.id=id;
		this.token = token;
		this.naturaExpiredDate = naturaExpiredDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getNaturaExpiredDate() {
		return naturaExpiredDate;
	}
	public void setNaturaExpiredDate(LocalDateTime naturaExpiredDate) {
		this.naturaExpiredDate = naturaExpiredDate;
	}
	
	
	
	
	
    
}
