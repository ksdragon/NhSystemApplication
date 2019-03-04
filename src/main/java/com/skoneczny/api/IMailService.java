package com.skoneczny.api;

import org.springframework.mail.SimpleMailMessage;

public interface IMailService {
	
	boolean defaultMailServerIsAvailable();

	void send(SimpleMailMessage email);
	
	SimpleMailMessage isNotAvailableMessageForEmail(String email);
}
