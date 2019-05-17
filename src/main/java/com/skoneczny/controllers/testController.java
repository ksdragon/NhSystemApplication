package com.skoneczny.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {

	@GetMapping("test")
	public String test() {
		
		return "views/test";
	}
}
