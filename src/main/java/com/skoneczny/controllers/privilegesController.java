package com.skoneczny.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.skoneczny.api.IUserService;
import com.skoneczny.entites.Role;
import com.skoneczny.entites.User;
import com.skoneczny.repositories.RoleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class privilegesController {
	
	private final Logger logger = Logger.getLogger(privilegesController.class);
	
	private IUserService userService;
	private RoleRepository roleRepository;
	
	@Autowired
	public privilegesController(IUserService userService, RoleRepository roleRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
	}
	
	@GetMapping("privileges")
	public String getPrivileges(@RequestParam String email, Model model) {
		User user = userService.findOne(email);
		List<Role> selectedUserRoles = user.getRoles();
		List<Role> roles = roleRepository.findAll();
		model.addAttribute(user);
		model.addAttribute("roles",roles);
		model.addAttribute("currentRole",selectedUserRoles);
		return "views/privilegesForm";
	}
	
	@PostMapping("privileges")
	public String postPrivileges(@RequestParam(defaultValue="USER") List<String> roles, @RequestParam String email, Model model) {
		logger.info(roles.toString());
		logger.info(email.toString());
		User user = userService.findOne(email);		
		List<Role> roleList = new ArrayList<Role>();
		roles.forEach(x -> roleList.add(new Role(x)));
		userService.setRole(roleList,user);
		return "redirect:/privileges?email=" + email;
	}
		
	
}
