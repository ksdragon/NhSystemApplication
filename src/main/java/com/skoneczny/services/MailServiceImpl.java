/**
 * 
 */
package com.skoneczny.services;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.skoneczny.api.IMailService;
import com.skoneczny.controllers.LoginController;

/**
 * @author HP ProDesk
 *
 */
@Service
public class MailServiceImpl implements IMailService {
	
	private final Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	@Autowired
	private MessageSource messageSource;
		
	/* (non-Javadoc)
	 * @see com.skoneczny.api.IMailService#mailServerIsAvailable(javax.mail.Session)
	 */
	@Override
	public boolean defaultMailServerIsAvailable() {	
		try {
			mailSender.testConnection();
			return true;
		} catch (MessagingException e) {
			logger.info("Server is not available : " + e);
			//e.printStackTrace();
		}
		return false;		
	}

	@Override
	public void send(SimpleMailMessage email) {
		mailSender.send(email);		
	}

	@Override
	public SimpleMailMessage isNotAvailableMessageForEmail(String eMail) {
		
		String recipientAddress = eMail;
		String subject = messageSource.getMessage("message.login.MailSubject", null, Locale.getDefault());
		String message = messageSource.getMessage("message.login.isNotAvailableMessageMailBody", new Object[] { eMail },
				Locale.getDefault());
		message += messageSource.getMessage("message.contact.mailRequest", new Object[] { "admin@admin.pl" },
				Locale.getDefault());
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message);
		return email;		
	}

}
