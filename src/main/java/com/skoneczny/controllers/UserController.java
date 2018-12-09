package com.skoneczny.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.skoneczny.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public String listUsers(Model model, @RequestParam(defaultValue="") String name) {
		model.addAttribute("users",userService.findByName(name));
		return "views/list";
	}
	
	
	@GetMapping("/deleteUser")
	public String taskFrom(String email, Model model, HttpSession session) {
		userService.deleteUser(email);
//		session.setAttribute("email", email);
//		model.addAttribute("task", new Task());
		return "redirect:/list";
	}
	
	
}
