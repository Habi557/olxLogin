package com.olx.user.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.olx.user.entity.UserEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Component
public class EmailNotification {

	private JavaMailSender javaMailSender;
	private TemplateEngine templateEngine;
	
public EmailNotification(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
		super();
		this.javaMailSender = javaMailSender;
		this.templateEngine=templateEngine;
	}


public boolean sendEmail(List<UserEntity> userEntity,int otp,String subject,String text, String url) {
	// of course you would use DI in any real-world cases
	SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	simpleMailMessage.setTo(userEntity.get(0).getEmail());
	simpleMailMessage.setSubject(subject);
	//simpleMailMessage.setText(text);
    try {
		String content = new String(Files.readAllBytes(Paths.get(new ClassPathResource("templates/email-template.html").getURI())));
	     content.replace("{{resetLink}}", url);
	     simpleMailMessage.setText(content);

    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		javaMailSender.send(simpleMailMessage);
		return true;

	}catch(MailAuthenticationException ex) {
     System.out.println(ex);
     throw new MailAuthenticationException("Invalid email Id");
	}
}
public boolean sendResetPasswordEmail(String to, String resetLink, String token) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    resetLink=resetLink+"?token="+token;

    // Prepare email content using Thymeleaf template
    Context context = new Context();
    context.setVariable("resetLink", resetLink);
    String htmlContent = templateEngine.process("email", context);

    // Configure email
    helper.setTo(to);
    helper.setSubject("Password Reset Request");
    helper.setText(htmlContent, true); // Enable HTML

    // Send email
        javaMailSender.send(message);
        return true;

     }

	

	

	
}
