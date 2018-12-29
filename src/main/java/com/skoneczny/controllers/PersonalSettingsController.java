package com.skoneczny.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.skoneczny.api.IUserService;
import com.skoneczny.entites.Address;
import com.skoneczny.entites.User;
import com.skoneczny.services.AddressService;
import com.skoneczny.services.UserService;

@Controller
public class PersonalSettingsController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;
	
	@GetMapping("/personalSettings")
	public String personalSettingsForm(Model model, Principal principal,  HttpSession session) {
				
		String email = principal.getName();
		List<Address> addresses = addressService.getAddress(userService.findOne(email));
		Address address = addresses.get(0);
		model.addAttribute("personalSettings", userService.findOne(email));
		model.addAttribute("addresses", address);
		return ("views/personalSettingsForm");
	}
	
	@PostMapping("/personalSettings")
	public String savePersonalSettings(@Valid User user,@Valid Address addresses, BindingResult bindingResult, HttpSession session) {
		if(bindingResult.hasErrors()) {
			return "views/personalSettingsForm";
			}
//		List<Address> addresses = new ArrayList<>();
		addressService.saveAdress(addresses);
//		user.setAdress(addresses);
		userService.updateUser(user);		
//		addressService.saveAdress(address);
		
		return ("redirect:/personalSettings");
	}

}
