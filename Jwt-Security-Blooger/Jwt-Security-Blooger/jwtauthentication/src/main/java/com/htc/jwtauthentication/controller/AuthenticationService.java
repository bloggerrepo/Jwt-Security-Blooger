package com.htc.jwtauthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.htc.jwtauthentication.config.JwtService;
import com.htc.jwtauthentication.user.Role;
import com.htc.jwtauthentication.user.User;
import com.htc.jwtauthentication.user.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

public class AuthenticationService {
	
	private final UserRepository repository ;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder ;
	private final User user ;
	private final JwtService jwtService ;
	private final AuthenticationResponse authenticationResponse ;
	
	@Autowired
	public AuthenticationService(UserRepository repository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder, User user, JwtService jwtService,
			AuthenticationResponse authenticationResponse) {
		super();
		this.repository = repository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.user = user;
		this.jwtService = jwtService;
		this.authenticationResponse = authenticationResponse;
	}


	public AuthenticationResponse register(RegisterRequest request) {
		
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmailId(request.getEmailId());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		repository.save(user);
		
		authenticationResponse.setToken(jwtService.generateToken(user)) ;
		
		return authenticationResponse ;
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmailId(), request.getPassword()));
		User user = repository.findByEmailId(request.getEmailId()).orElseThrow() ;
		authenticationResponse.setToken(jwtService.generateToken(user)) ;
		return authenticationResponse ;
	}
}
