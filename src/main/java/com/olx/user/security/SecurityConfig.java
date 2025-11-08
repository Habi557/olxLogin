package com.olx.user.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olx.user.dto.UserDto;
import com.olx.user.entity.UserEntity;
import com.olx.user.exceptionfilters.AccessDenied;
import com.olx.user.exceptionfilters.CustomAuthenticationEntryPoint;
import com.olx.user.repo.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	JwtAuthFilter jwtAuthFilter;
	@Autowired
	AccessDenied accessDenied;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserRepository userRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
		super();
		this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
	}

		//SecurityConfiguration.java
		@Bean
		public DaoAuthenticationProvider getAuthenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setPasswordEncoder(passwordEncoder);
			authProvider.setUserDetailsService(userDetailsService);
			return authProvider;
		}
		
		@Bean
		public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) throws Exception {
			return config.getAuthenticationManager();
		}
	 
	//Authorization
	@Bean
	public SecurityFilterChain authorize(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth-> {
			auth
			.requestMatchers("/olx/user/register").permitAll()
			//.hasAnyRole("ADMIN")
		.requestMatchers("/olx/user/**").permitAll()
		.requestMatchers("/olx/user/forgetpassword/").permitAll()
        .requestMatchers("/error").permitAll()
       // .requestMatchers("/olx/login/*").permitAll()
		.requestMatchers(HttpMethod.OPTIONS,"/login/oauth2/code/google").permitAll()
			//.requestMatchers("/**").permitAll()
		.requestMatchers("/olxloginActuator/*").permitAll()
			.requestMatchers(
					"/swagger-ui/**",  
		            "/v3/api-docs/**",  
		            "/swagger-ui.html" 					).permitAll()
			.anyRequest().authenticated();
		})
//		  .oauth2Login(oauth2 -> oauth2
//	                .userInfoEndpoint(userInfo -> userInfo
//	                    .oidcUserService(this.oidcUserService()) 
//	                )
//	                .successHandler((request, response, authentication) -> {
//	                    OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
//	                    String jwtToken = jwtUtils.generateToken(oidcUser.getFullName()); 
//	                    List<UserEntity> byEmail = userRepository.findByEmail(oidcUser.getEmail());
//	                  
//	                    if(byEmail.size()>0) {
//
//	                    	userRepository.delete(byEmail.get(0));
//	                    	
//	                    }
//                   	 UserEntity userEntity = new UserEntity(oidcUser.getName(),oidcUser.getFamilyName(),oidcUser.getFullName(),"Test",oidcUser.getEmail());
//                     userEntity.setToken(jwtToken);
//	                 userEntity.setRoles("ROLE_USER");
//                 	 userRepository.save(userEntity);
//
//	                   
//	                    response.sendRedirect("http://localhost:4200/dashboard?token=" + jwtToken); // Redirect with token
//	                })
//	            )
		.exceptionHandling(exceptions -> {exceptions
                .authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(accessDenied);
                }
            )

		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
//	@Bean
//	public SecurityFilterChain authorize2(HttpSecurity httpSecurity)throws Exception {
//		httpSecurity.csrf(csrf->csrf.disable())
//		.authorizeHttpRequests(auth-> {
//			auth
//			.requestMatchers("/olx/user/logout").permitAll();
//		});
//		
//		return httpSecurity.build();
//
//	}

 
	/*@Bean
	public UserDetailsService userss() {
		UserBuilder users = User.builder();
		UserDetails habiUser= users.username("Habi").password(passwordEncoder.encode("786786")).roles("USER").build();
		UserDetails abdullaUser= users.username("Abdulla").password(passwordEncoder.encode("786786")).roles("USER").build();
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(habiUser,abdullaUser);
        return inMemoryUserDetailsManager;
	
	}*/
	
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//	        @Override
//	        public void addCorsMappings(CorsRegistry registry) {
//	            registry.addMapping("/**")
//	            .allowedOrigins("http://localhost:4200") // Allow only one origin
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowCredentials(true)
//                .allowedHeaders("*")
//                .exposedHeaders("Authorization")
//                ;
//	            
//	        }
//	    };
//	}
//	 @Bean
//	    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
//	        return new OidcUserService(); // Default OIDC user service
//	    }
	

	

}
