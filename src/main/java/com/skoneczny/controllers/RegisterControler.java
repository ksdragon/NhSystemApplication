package com.skoneczny.controllers;


import java.util.Calendar;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.skoneczny.entites.User;
import com.skoneczny.entites.VerificationToken;
import com.skoneczny.event.OnRegistrationCompleteEvent;
import com.skoneczny.services.IUserService;

@Controller
public class RegisterControler {
	
	// Activate a New Account by Email
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired
    private MessageSource messages;
	
	@Autowired
	private IUserService userService;
	
	
	
	@GetMapping("/register")
	public String registerForm(Model model, WebRequest request) {
//		Locale locale = request.getLocale();
		model.addAttribute("user", new User());
		return "views/registerForm"; // ?lang=" + locale.getLanguage() ;		
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid User user, BindingResult bindingResult, Model model, WebRequest request) {
//		 Locale locale = request.getLocale();
		
		if (bindingResult.hasErrors()){
			return "views/registerForm"; // ?lang=" + locale.getLanguage() ;
		}
		if(userService.isUserPresent(user.getEmail())) {
			model.addAttribute("exist", true);
			return "views/registerForm";  // ?lang=" + locale.getLanguage() ;
		}		
		User registered = userService.createUser(user);
		
		// Activate a New Account by Email
		try {
	        String appUrl = request.getContextPath();
	        eventPublisher.publishEvent(new OnRegistrationCompleteEvent
	          (registered, request.getLocale(), appUrl));
	    } catch (Exception me) {
	        return "views/registerForm";  // ?lang=\" + locale.getLanguage() ;
	        		//new ModelAndView("emailError", "user", accountDto);
	    }
		
		return "views/success";  // ?lang=\" + locale.getLanguage() ;
	}
	
	@GetMapping("/registrationConfirm")
	public String confirmRegistration
	  (WebRequest request, Model model, @RequestParam("token") String token) {
	  
	    Locale locale = request.getLocale();
	     
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
	        String message = messages.getMessage("auth.message.invalidToken", null, locale);
	        model.addAttribute("message", message);
	        return "views/badUser.html"; // ?lang=" + locale.getLanguage();
	    }
	     
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
	        model.addAttribute("message", messageValue);
	        return "views/badUser.html";			//  "redirect:/badUser.html?lang=" + locale.getLanguage();
	    } 
	     
	    user.setEnabled(true); 
	    userService.saveRegisteredUser(user); 
	    return  "redirect:/login";// "redirect:/loginForm.html?lang=" + request.getLocale().getLanguage(); 
	}

}
