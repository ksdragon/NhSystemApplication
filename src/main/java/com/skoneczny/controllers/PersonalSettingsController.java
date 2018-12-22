package com.skoneczny.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.skoneczny.entites.User;
import com.skoneczny.services.UserService;

@Controller
public class PersonalSettingsController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/personalSettings")
	public String personalSettingsForm(Model model, Principal principal,  HttpSession session) {
				
		String email = principal.getName();
		model.addAttribute("personalSettings", userService.findOne(email));
		return ("views/personalSettingsForm");
	}
	
	@PostMapping("/personalSettings")
	public String savePersonalSettings(@Valid User user, BindingResult bindingResult, HttpSession session) {
		if(bindingResult.hasErrors()) {
			return "views/personalSettingsForm";
		}
		userService.updateUser(user);
		return ("redirect:/personalSettings");
	}

}
