package com.skoneczny;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.skoneczny.api.InitiateDataApi;
import com.skoneczny.entites.Role;
import com.skoneczny.entites.User;
import com.skoneczny.repositories.RoleRepository;
import com.skoneczny.services.UserService;

@EnableJpaAuditing
@SpringBootApplication
public class NhSystemApplication implements CommandLineRunner{

	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private InitiateDataApi intiateDate;
	
	public static void main(String[] args) {
		SpringApplication.run(NhSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {		
		{	
			if(!userService.isUserPresent("admin@mail.com")) {
			Role roleAdmin = new Role("ADMIN");
			Role roleUser = new Role("USER");
			Role menagerUser = new Role("MENAGER");
			List<Role> roles = new ArrayList<>();
			roles.add(roleAdmin);
			roles.add(roleUser);
			roles.add(menagerUser);
			roleRepository.saveAll(roles);
			User newAdmin = new User("admin@mail.com","Admin","123456");
			userService.createAdmin(newAdmin);
			List<User> generateManyUser = intiateDate.generateManyUser();			
			generateManyUser.forEach(x ->  intiateDate.generateManyTasks(x));
			}
		}
	}
	
	
	
	
}
