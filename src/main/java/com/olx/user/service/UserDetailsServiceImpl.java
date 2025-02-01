package com.olx.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.olx.user.entity.UserEntity;
import com.olx.user.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	UserRepository  userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		 List<UserEntity> findByUserName = userRepository.findByUserNameCaseSensitive(username);
         if(findByUserName == null || findByUserName.size()==0) {
        	 throw new UsernameNotFoundException(username);
         }
         UserEntity userEntity = findByUserName.get(0);
         Collection<GrantedAuthority> authorities = new ArrayList();
         String roles =userEntity.getRoles();
         String[] rolesArray = roles.split(",");
         for(String role: rolesArray) {
        	 GrantedAuthority authority= new SimpleGrantedAuthority(roles.trim());
        	 authorities.add(authority);
         }
		 UserDetails user = new User(userEntity.getUserName(),passwordEncoder.encode(userEntity.getPassword()),authorities);
		return user;
	}

}
