package com.htc.jwtauthentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final  JwtApplicationFilter jwtAppFilter;
	private  final AuthenticationProvider authenticationProvider;
 

	public SecurityConfig(JwtApplicationFilter jwtAppFilter, AuthenticationProvider authenticationProvider) {
		super();
		this.jwtAppFilter = jwtAppFilter;
		this.authenticationProvider = authenticationProvider;
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{						//bean responsible for configuring all http security of our requests
//		http
//		.csrf().disable().authorizeHttpRequests()
//		.requestMatchers("/api/v1/auth", "/api/v1/jwt-controller").permitAll()
//		.anyRequest().authenticated()
//		.and()
//		.sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//		.and()
//		.authenticationProvider(authenticationProvider)
//		.addFilterBefore(jwtAppFilter, UsernamePasswordAuthenticationFilter.class);
		
		http
		.csrf().disable()
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**")).permitAll()
            .anyRequest().authenticated())
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAppFilter, UsernamePasswordAuthenticationFilter.class) ; //other URLs are only allowed authenticated users.
    
    
		return http.build();
	}
}
