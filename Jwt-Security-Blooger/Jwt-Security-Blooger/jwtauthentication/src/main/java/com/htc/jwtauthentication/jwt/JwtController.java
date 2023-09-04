package com.htc.jwtauthentication.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/jwt-controller")
public class JwtController {
	@GetMapping 
	public ResponseEntity<String> sayHello()
	{
		System.out.println("get method");
		return ResponseEntity.ok("Hello from secured endpoint");
		
	}
}
