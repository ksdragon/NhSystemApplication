package com.skoneczny.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.skoneczny.api.IPasswordService;
import com.skoneczny.api.IUserService;
import com.skoneczny.entites.Password;

@Controller
public class changePasswordController {
	
	@Autowired
	private IUserService userService;
	
	@GetMapping("/changePassword")
	public String changePassword(Model model,Principal principal) {
		String email = principal.getName();		
		model.addAttribute("password", new Password());
		return "views/changePasswordForm";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(Principal principal) {
		return "views/changePasswordForm";
	}

}
