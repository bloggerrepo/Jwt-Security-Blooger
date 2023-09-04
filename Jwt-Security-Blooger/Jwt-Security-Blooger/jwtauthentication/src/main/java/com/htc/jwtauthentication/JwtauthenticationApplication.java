package com.htc.jwtauthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.htc.jwtauthentication.controller.AuthenticationService;
import com.htc.jwtauthentication.controller.RegisterRequest;
import com.htc.jwtauthentication.user.Role;

@SpringBootApplication
public class JwtauthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtauthenticationApplication.class, args);
	}
	
	@Autowired
	RegisterRequest admin, manager ;	
	
	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service) 
	{
		
		return args -> { 
			//RegisterRequest admin ;
			admin.setFirstName("Admin");
			admin.setLastName("Admin");
			admin.setEmailId("admin@gmail.com");
			admin.setPassword("password");
			admin.setRole(Role.ADMIN);
	
			System.out.println("Admin token: " + service.register(admin).getToken());
			
			//RegisterRequest manager = null ;
			manager.setFirstName("Admin");
			manager.setLastName("Admin");
			manager.setEmailId("manager@gmail.com");
			manager.setPassword("password");
			manager.setRole(Role.ADMIN);
			System.out.println("Manager token: " + service.register(manager).getToken());
			};
	}
}
