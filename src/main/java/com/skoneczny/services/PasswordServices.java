package com.skoneczny.services;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.skoneczny.api.IPasswordService;
import com.skoneczny.api.IUserService;
import com.skoneczny.entites.Password;
import com.skoneczny.entites.User;
import com.skoneczny.repositories.PasswordRepository;

@Service
public class PasswordServices implements IPasswordService {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private PasswordRepository passwordRepository;
	
	@Override
	public Password createPassword(@Valid Password password) {
		return passwordRepository.save(password);
	}
	
	@Override
	public boolean isCorrectPassword(String email, String oldPassword) {
		User user = userService.findOne(email);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();		
		return encoder.matches(oldPassword,	user.getPassword());
	}

	@Override
	public boolean isCorrectNewPassword(String newPassword, String repeatPassword) {		
		return newPassword.equals(repeatPassword);
	}
}
