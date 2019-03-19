package com.skoneczny.initiate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.skoneczny.entites.Address;
import com.skoneczny.entites.Role;
import com.skoneczny.entites.User;


@Service
public class GenerateUser {

	private static final String FIRST_NAMES_FILE_PATH = "firstNames";
	private static final String LAST_NAMES_FILE_PATH = "lastNames";
	private static final int AGE_IN_YEARS = 30;	
	private List<String> firstNames;
	private List<String> lastNames;
	private List<String> emails;
	private final Random random = new Random(); 	
	
	public User generate() {
		User user = new User();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode("123456"));		
		user.setName(getRandomFirstName());
		user.setLastName(getRandomLastName());
		int r = random.nextInt(9);
		String email = r + getNextEmail();
		user.setEmail(email);		
		user.setBirthday(getRandomBirthtDate());
		Role userRole = new Role("USER");
		List<Role> roles = new ArrayList<>();
		roles.add(userRole);
		Address address = new Address();
		address.setZipCode("00-000");
		address.setUser(user);		
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		user.setAddresses(addresses);	
		user.setRoles(roles);
		user.setEnabled(true);
		return user;
	}
	
		
	private String getNextEmail() {		
		return getRandom(getEmails());
	}
	

	public List<String> getEmails() {
		if(emails == null) {
			emails = loadLines("emails");
		}
		return emails;
	}

	private String getRandomLastName() {
		return getRandom(getLastNames());		
	}
	
	public List<String> getLastNames() {
		if(lastNames ==null) {
			lastNames = loadLines(LAST_NAMES_FILE_PATH);
		}
		return lastNames;
	}
	
	private String getRandomFirstName() {		
		return getRandom(getFirstNames());
	}	

	public List<String> getFirstNames() {
		if (firstNames == null) {
            firstNames = loadLines(FIRST_NAMES_FILE_PATH);
        }
        return firstNames;
	}



	/*
	 * Load list form file 
	 * @param file path 
	 * @return List of Strings form file.
	 */
	private List<String> loadLines(String filePath) {
		 try {
	            return Files.readAllLines(
	                    Paths.get(
	                            new ClassPathResource(
	                                    filePath)
	                                    .getURI()));
	        } catch (IOException e) {
	            throw new RuntimeException(
	                    String.format(
	                            "Error while reading lines from file %s",
	                            filePath),
	                    e);
	        }
	}
	
	/*	  
	 * @param list of elements 
	 * @return one element from list 
	 */
	private String getRandom(List<String> elements) {
		return elements.get(
                random.nextInt(
                        elements.size()));
	}
	
	private String getNext(List<String> elements) {
			return elements.iterator().next();	
	}
	
	 private Date getRandomBirthtDate() {
	        return Date.valueOf(LocalDate
	                .now()
	                .minus(
	                        getRandomNumberOfDays()));
	    }

	    private Period getRandomNumberOfDays() {
	        return Period.ofYears(
	                random.nextInt(
	                        AGE_IN_YEARS));
	    }

}
