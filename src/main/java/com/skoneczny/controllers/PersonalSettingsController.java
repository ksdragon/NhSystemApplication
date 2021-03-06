package com.skoneczny.controllers;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.skoneczny.entites.Address;
import com.skoneczny.entites.User;
import com.skoneczny.services.AddressService;
import com.skoneczny.services.UserService;

@Controller
public class PersonalSettingsController {

	@Autowired
	private UserService userService;
	
	//@Autowired
	//private AddressService addressService;
	
	@GetMapping("/personalSettings")
	public String personalSettingsForm(Model model, Principal principal,  HttpSession session) {
				
		String email = principal.getName();
		//List<Address> addresses = addressService.getAddress(userService.findOne(email));
		//Address address = addresses.get(0);
		model.addAttribute("user", userService.findOne(email));
		//model.addAttribute("addresses", address);
		return ("views/personalSettingsForm");
	}
	
	@PostMapping("/personalSettings")
	public String savePersonalSettings(@Valid User user, BindingResult bindingResult, HttpSession session) {
		if(bindingResult.hasErrors()) {
			bindingResult
			.getFieldErrors()
			.stream()
			.forEach(f -> System.out.println(f.getField() + ": " + f.getDefaultMessage()));
			return "views/personalSettingsForm";
			}
		//@Valid Address addresses,
		
//		List<Address> addresses = new ArrayList<>();
//		addressService.saveAdress(addresses);
//		user.setAdress(addresses);
		userService.updateUser(user);		
//		addressService.saveAdress(address);
		
		return ("redirect:/personalSettings");
	}

}
