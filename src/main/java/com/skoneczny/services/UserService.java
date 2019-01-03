package com.skoneczny.services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.skoneczny.api.IUserService;
import com.skoneczny.entites.Address;
import com.skoneczny.entites.Role;
import com.skoneczny.entites.User;
import com.skoneczny.entites.VerificationToken;
import com.skoneczny.repositories.UserRepository;
import com.skoneczny.repositories.VerificationTokenRepository;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private VerificationTokenRepository tokenRepository;
	
	@Override
	public User createUser(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		Role userRole = new Role("USER");
		List<Role> roles = new ArrayList<>();
		roles.add(userRole);
		Address address = new Address();
		address.setUser(user);		
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		user.setAddresses(addresses);	
		user.setRoles(roles);
		return userRepository.save(user);
		
	}

	public void createAdmin(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		Role userRole = new Role("ADMIN");
		List<Role> roles = new ArrayList<>();
		roles.add(userRole);
		user.setRoles(roles);
//		Address address = new Address();
//		address.setUser(user);		
//		List<Address> addresses = new ArrayList<Address>();
//		addresses.add(address);
//		user.setAddresses(addresses);
		userRepository.save(user);		
	}
	
	public User findOne(String email) {
		return userRepository.findById(email).get();		
	}
	@Override
	public boolean isUserPresent(String email) {		
		if(userRepository.existsById(email)) return true;
		return false;
	}

	public List<User> findAll() {		
		return userRepository.findAll();
	}

	public List<User> findByName(String name) {
//		List<String> namesList = new ArrayList<>(Arrays.asList(name.split(" ")));
//		userRepository.findAll().forEach(x -> x.getName());
		return userRepository.findByNameLike("%"+name+"%");
	}

	public void updateUser(@Valid User user) {
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		user.setPassword(encoder.encode(user.getPassword()));
		Role userRole = new Role("USER");
		List<Role> roles = new ArrayList<>();
		roles.add(userRole);
		user.setRoles(roles);
		userRepository.save(user);		
	}
	
	 @Override
	public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }
    
	 @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
	 @Override 
    public void saveRegisteredUser(User user) {
    	userRepository.save(user);
    }
	 @Override 
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }
	 
	 public void deleteUserEntity(User user) {
		 userRepository.delete(user); 
	 } 
	 
	//@Transactional
	public void deleteUser1(String email) {
		Role userRole = new Role();
		List<User> users = new ArrayList<>();
		User user = findOne(email);
		users.add(user);
//		user.setAdress(address);
		userRole.setUsers(users);		
//		user.setRoles(null);
//		userRepository.save(user);
		VerificationToken vtUser = new VerificationToken();
		vtUser = tokenRepository.findByUser(user);
		vtUser.setUser(null);		
		tokenRepository.delete(vtUser);
		userRepository.delete(user);		
	}
	
	public void deleteUser(String email) {		
		User user = findOne(email);		
		user.setRoles(null);
		VerificationToken vtUser = new VerificationToken();
		vtUser = tokenRepository.findByUser(user);		
		tokenRepository.deleteById(vtUser.getId());
		userRepository.delete(user);		
	}
	
		
}
