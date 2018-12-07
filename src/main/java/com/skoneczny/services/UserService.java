package com.skoneczny.services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
	
}
