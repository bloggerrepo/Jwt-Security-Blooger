package com.htc.jwtauthentication.config;

import java.io.Serializable;
import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
@Component
public class JwtService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SECRET_KEY = "a9d5utWQEBtbAxTVWPlajlAgNuY6+/yjVHS6QNepQxFT7FApRsxabLBdwybH41Ty";


	public String extractUserEmail(String token) {
		// TODO Auto-generated method stub
		return extractClaims(token, Claims::getSubject);
	}
	
	public<T> T extractClaims(String token, Function<Claims, T> claimsResolver)
	{
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(UserDetails userDetails)
	{
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken( Map<String,Object> extraclaims, UserDetails userDetails) //generating token using extra claims
		{
			return Jwts.builder()
					.setClaims(extraclaims)
					.setSubject(userDetails.getUsername())
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis()+1000*60*24)) // token valid for 24hrs
					.signWith(getSignInKey(),SignatureAlgorithm.HS256)
					.compact() ;
		}
	
	public boolean isTokenValid(String token, UserDetails userDetails)
	{
		final String username = extractUserEmail(token);
		return (username.equals(userDetails.getUsername()))&& !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		
		return extractClaims(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token)
	{
	return Jwts
			.parserBuilder()
			.setSigningKey(getSignInKey()) //signing key required on every deserialized or decoded token to imply data is not compromised
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private Key getSignInKey() {
		// TODO Auto-generated method stub
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
