package com.olx.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.olx.user.dto.UserDto;
import com.olx.user.dto.UsersDto;
import com.olx.user.entity.BlackListedTokensDocument;
import com.olx.user.entity.UserEntity;
import com.olx.user.exception.InvalidOtp;
import com.olx.user.exception.InvalidUser;
import com.olx.user.exception.TokenAlreadyExpired;
import com.olx.user.repo.BlackListedTokens;
import com.olx.user.repo.UserRepository;
import com.olx.user.security.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class LoginServiceImpl implements LoginService {

//	@Autowired
//	AuthenticationManager authenticatonManager;
//	@Autowired
//	JwtUtils jwtUtils;
//	@Autowired
//	UserDetailsService userDetailsService;
//	@Autowired
//	UserRepository userRepository;
//	@Autowired
//	ModelMapper modelMapper;
//	@Autowired
//	BlackListedTokens blackListedTokens;
//	@Autowired
//	EmailNotification emailNotification;
	////////////////////////////////
	AuthenticationManager authenticatonManager;
	JwtUtils jwtUtils;
	UserDetailsService userDetailsService;
	UserRepository userRepository;
	ModelMapper modelMapper;
	BlackListedTokens blackListedTokens;
	EmailNotification emailNotification;
	PasswordEncoder passwordEncoder;
	
	public LoginServiceImpl(AuthenticationManager authenticatonManager, JwtUtils jwtUtils,
			UserDetailsService userDetailsService, UserRepository userRepository, ModelMapper modelMapper,
			BlackListedTokens blackListedTokens, EmailNotification emailNotification,PasswordEncoder passwordEncoder) {
		super();
		this.authenticatonManager = authenticatonManager;
		this.jwtUtils = jwtUtils;
		this.userDetailsService = userDetailsService;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.blackListedTokens = blackListedTokens;
		this.emailNotification = emailNotification;
		this.passwordEncoder=passwordEncoder;
	}

	Random random = new Random(1000);
	@Value("${frontend-url}")
	private String url;

	@Override
	public UsersDto authenticate(UsersDto userDto) {
		// TODO Auto-generated method stub
		try {
			Authentication authenticate = authenticatonManager
					.authenticate(new UsernamePasswordAuthenticationToken(userDto.getName(), userDto.getPassword()));
			String generateToken = jwtUtils.generateToken(userDto.getName());
			userDto.setToken(generateToken);
			userDto.setRole(authenticate.getAuthorities().toString());
			UserEntity byUserName = userRepository.findByUserName(userDto.getName());
			userDto.setId(byUserName.getId());
			return userDto;
		} catch (AuthenticationException e) {
			throw new InvalidUser(userDto.getName());
		}
	}

	@Override
	public Boolean logout(Boolean tokenValidity, String token) {
		// TODO Auto-generated method stub
		// checking token is valid or not
		if (!tokenValidity) {
			throw new TokenAlreadyExpired("Your token is expired");
		} else {
			// Token is active
			// LocalDateTime date = LocalDateTime.now().plusHours(24);
			LocalDateTime date = LocalDateTime.now().plusMinutes(2);
			// date.plus(9,)
			// date.getd
			// Date date2 = new Date();
			List<BlackListedTokensDocument> findAll = blackListedTokens.findAll();
			int size = findAll.size();
			blackListedTokens.save(new BlackListedTokensDocument(size + 1, token, date));
			return true;

		}
	}

	@Override
	public UserDto register(UserDto userDto) {
		// TODO Auto-generated method stub
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		userRepository.save(userEntity);
		return userDto;
	}

	@Override
	public UserDto getUser(String authHeader) {
		// TODO Auto-generated method stub
		String token = null;
		String userName = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			userName = jwtUtils.extractUsername(token);

		}
		if (userName != null) {

			UserEntity findByUserName = userRepository.findByUserName(userName);
			List<UserDto> userDtoList = new ArrayList();
//			for (UserEntity userEntity : findByUserName) {
//				UserDto userDto = modelMapper.map(userEntity, UserDto.class);
//				userDto.setId(userEntity.getId());
//				userDtoList.add(userDto);
//
//			}
//			return userDtoList;
			UserDto userDto = modelMapper.map(findByUserName, UserDto.class);
			userDto.setId(findByUserName.getId());
			return userDto;

		}

		return null;
	}

	@Override
	public Boolean isTokenValid(String authHeader, String expectedRoles) {
		// TODO Auto-generated method stub
		String token = null;
		String username = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtUtils.extractUsername(token);
		} else {
			return false;
		}
		try {
			System.out.println("Token validation");
			if (username != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtUtils.validateToken(token, userDetails)) {
					BlackListedTokensDocument findByToken = blackListedTokens.findByToken(authHeader);
					if (findByToken == null) {
//						String authorities = userDetails.getAuthorities().toString();
//						String roleArray[] = expectedRoles.split(",");
//						for (String role : roleArray) {
//							if (authorities.contains(role)) {
//								return true;
//							}
//						}
						return true;

					} else {
						return false;
					}

				}
			}
		} catch (ExpiredJwtException e) {
			throw new TokenAlreadyExpired("Your token has expired. Please log in again.");

		} catch (Exception e) {
			throw new RuntimeException("request forget");
		}

		// return new ResponseEntity<Boolean>(false, HttpStatus.FORBIDDEN);
		return false;

	}

	@Override
	public Boolean validateUserName(String email) {
		// TODO Auto-generated method stub
		List<UserEntity> findByEmail = userRepository.findByEmail(email);
		if (findByEmail.size() > 0) {
			int otp = random.nextInt(999999);
			String subject = "OTP for Verification";
			// String resetLink = "http://localhost:4200/resetpassword";
			String text = "Hi " + findByEmail.get(0).getFirstName() + ",\n" + "\n"
					+ "We sent a link to reset your password for your account. If this was you, please click the link below to create a new password:\n"
					+ "\n" + "Please click the following link to reset your password: <a href=\"" + url
					+ "/resetpassword" + "\">Reset Password</a>.\n" + "\n"
					+ "This link will expire in 1 hours for security reasons. If you don’t reset your password within this time, you’ll need to submit another request.\n"
					+ "\n"
					+ "If you didn’t request a password reset, no worries! Your account is still secure, and you can ignore this email.\n"
					+ "\n"
					+ "For any questions or concerns, feel free to contact our support team at [Support Email] or visit our Help Center at [Help Center Link].\n"
					+ "\n" + "Thanks,\n" + "The OLX Login Team";
			// Boolean sendEmail = emailNotification.sendEmail(findByEmail, otp, subject,
			// text, url);
			try {
				String token = jwtUtils.generateToken(findByEmail.get(0).getUserName());
				boolean sendEmailStatus = emailNotification.sendResetPasswordEmail(findByEmail.get(0).getEmail(), url, token);
				if (sendEmailStatus) {
					findByEmail.get(0).setOtp(otp);
					userRepository.save(findByEmail.get(0));

				}
				return true;
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;

	}

	@Override
	public Boolean validateOtp(int otp, String email) {
		// TODO Auto-generated method stub
		List<UserEntity> findByEmail = userRepository.findByEmail(email);
		if (findByEmail.size() > 0) {
			int otp2 = findByEmail.get(0).getOtp();
			if (otp == otp2) {
				return true;
			}
		}
		throw new InvalidOtp(otp);
		// return false;
	}

	@Override
	public Boolean resetPassword(String token, String password) {
		// TODO Auto-generated method stub
		String username = jwtUtils.extractUsername(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		try {
        if (token == null || !jwtUtils.validateToken(token, userDetails)) {
            return false;
        }
		UserEntity userNameEntity = userRepository.findByUserName(username);
		//userNameEntity.setPassword(passwordEncoder.encode(password));
		userNameEntity.setPassword(password);

		userRepository.save(userNameEntity);
		return true;
		} catch (ExpiredJwtException e) {
			throw new TokenAlreadyExpired("Your token has expired. Please reset password Again.");

		}
		
	}

}
