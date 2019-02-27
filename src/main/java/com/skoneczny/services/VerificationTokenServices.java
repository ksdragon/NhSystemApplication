package com.skoneczny.services;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.skoneczny.api.IUserService;
import com.skoneczny.api.IVerificationToken;
import com.skoneczny.entites.User;
import com.skoneczny.entites.VerificationToken;
import com.skoneczny.repositories.VerificationTokenRepository;

@Service
public class VerificationTokenServices implements IVerificationToken {

	@Autowired
	private MessageSource messages;

	@Autowired
	private IUserService userService;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public VerificationToken gettVerificationTokenByEmail(String email) {
		
			User user = userService.findOne(email);
			return verificationTokenRepository.findByUser(user);
		
	}

	@Override
	public boolean isExpiryDate(VerificationToken verificationToken) {
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return true;
		}
		return false;
	}

	@Override
	public void deleteVerificationTokenByEmail(String email) {
		verificationTokenRepository.delete(gettVerificationTokenByEmail(email));		
	}

}
