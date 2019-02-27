package com.skoneczny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.skoneczny.entites.User;
import com.skoneczny.services.UserService;

@EnableJpaAuditing
@SpringBootApplication
public class NhSystemApplication implements CommandLineRunner{

	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(NhSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {		
		{	
			if(!userService.isUserPresent("admin@mail.com")) {
			User newAdmin = new User("admin@mail.com","Admin","123456");
			userService.createAdmin(newAdmin);}
		}
	}
	
	
	
	
}
