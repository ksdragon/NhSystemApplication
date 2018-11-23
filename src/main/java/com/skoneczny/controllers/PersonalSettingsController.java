package com.skoneczny.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.skoneczny.services.UserService;

@Controller
public class PersonalSettingsController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/personalSettings")
	public String savePersonalSettings(Model model, Principal principal) {
		
		String email = principal.getName();
		model.addAttribute("personalSettings", userService.findOne(email));
		return ("views/personalSettingsForm");
	}
}
