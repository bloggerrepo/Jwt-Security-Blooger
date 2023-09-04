package com.htc.jwtauthentication.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class JwtApplicationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;
	
	@Autowired
	public JwtApplicationFilter(UserDetailsService userDetailsService, JwtService jwtService) {
		super();
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}
	@Override
	protected void doFilterInternal(
			@NonNull		HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain) 
			throws ServletException, IOException {
		 
		if (request.getServletPath().contains("/api/v1/auth")) {
		      filterChain.doFilter(request, response);
		      return;
		    }
		// TODO Auto-generated method stub
		final String authHeader = request.getHeader("Authorization"); //Jwt token is passed in a header
		final String jwt;
		final String userEmail;
		//check Token
		if(authHeader ==  null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response); //request and response passed to next filter
		return ;
		}
		jwt = authHeader.substring(7);
		userEmail = jwtService.extractUserEmail(jwt) ; //extract the email from Jwt token 
		if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
			 if(jwtService.isTokenValid(jwt, userDetails))
			 {
				 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			 SecurityContextHolder.getContext().setAuthentication(authToken);
			 }
		}
		filterChain.doFilter(request, response);
	}
}
