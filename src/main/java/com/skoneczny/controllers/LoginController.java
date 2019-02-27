package com.skoneczny.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.skoneczny.api.IUserService;
import com.skoneczny.api.IVerificationToken;
import com.skoneczny.entites.User;
import com.skoneczny.event.OnRegistrationCompleteEvent;

@Controller
public class LoginController {

	private final Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IVerificationToken iVerificationToken;
	
	@GetMapping("/resetToken")
	public String resetToken(@RequestParam String email, WebRequest request) {
		
		logger.info("Send activate link for : " + email);
		
		if(userService.isUserPresent(email)) {
			User getRegistered = userService.findOne(email);
			iVerificationToken.deleteVerificationTokenByEmail(email);
			try {
		        String appUrl = request.getContextPath();
		        eventPublisher.publishEvent(new OnRegistrationCompleteEvent
		          (getRegistered, request.getLocale(), appUrl));
		    } catch (Exception me) {
		        return "views/login";  
		        }		
			return "redirect:/login";
			
		}else
		{
			return "views/login";
		}	
		
	}
}
