package com.skoneczny.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddressController {
	
	@GetMapping("/addresFragment")
	public String getAddress() {
		
		return "fragments/addressFragment";
	}

}
