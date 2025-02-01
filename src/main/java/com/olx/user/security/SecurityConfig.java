package com.olx.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.olx.user.exceptionfilters.CustomAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	JwtAuthFilter jwtAuthFilter;
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
		httpSecurity.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth-> {
			auth
			//.requestMatchers("/zensar/create").hasAnyRole("ADMIN")
			//.requestMatchers("/zensar/").permitAll()
			.requestMatchers("/olx/user/authenticate").permitAll()
			.requestMatchers("/olx/user/register").hasAnyRole("ADMIN")
			.requestMatchers("/olx/user/forgetpassword/**").permitAll()
			.requestMatchers("/olx/user/verifyotp/**").permitAll()
			.requestMatchers("/olx/user/token/validate").permitAll()
			.requestMatchers("/olxlogin/**").permitAll()
			.requestMatchers(
					"/swagger-ui/**",   // Swagger UI resources
		            "/v3/api-docs/**",  // OpenAPI docs
		            "/swagger-ui.html" 					).permitAll()
			.anyRequest().authenticated();
		})
		//.formLogin(Customizer.withDefaults())
		//AuthenticationEntryPoint
		.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(customAuthenticationEntryPoint)
            )

		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	@Bean
	public SecurityFilterChain authorize2(HttpSecurity httpSecurity)throws Exception {
		httpSecurity.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth-> {
			auth
			.requestMatchers("/olx/user/logout").permitAll();
		});
		
		return httpSecurity.build();

	}

 
	/*@Bean
	public UserDetailsService userss() {
		UserBuilder users = User.builder();
		UserDetails habiUser= users.username("Habi").password(passwordEncoder.encode("786786")).roles("USER").build();
		UserDetails abdullaUser= users.username("Abdulla").password(passwordEncoder.encode("786786")).roles("USER").build();
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(habiUser,abdullaUser);
        return inMemoryUserDetailsManager;
	
	}*/
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurer() {
	        @Override
	        public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/**").allowedOrigins("*");
	        }
	    };
	}

	

}
