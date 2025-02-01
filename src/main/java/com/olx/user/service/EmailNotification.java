package com.olx.user.service;

import java.util.List;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.olx.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Component
public class EmailNotification {

	private JavaMailSender javaMailSender;
	
	
public EmailNotification(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}


public boolean sendEmail(List<UserEntity> userEntity,int otp,String subject,String text) {
	// of course you would use DI in any real-world cases
	SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	simpleMailMessage.setTo(userEntity.get(0).getEmail());
	simpleMailMessage.setSubject(subject);
	simpleMailMessage.setText(text + otp);
	javaMailSender.send(simpleMailMessage);
	return true;
}
	

	

	
}
