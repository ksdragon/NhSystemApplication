package com.skoneczny.controllers;

import java.security.Principal;
import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skoneczny.entites.User;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

@Controller
public class profileController {

	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/profile")
	public String showProfilePage(Model model, Principal principal) {
		String year = Integer.toString(LocalDate.now().getYear());		
		String email = principal.getName();
		User user = userService.findOne(email);
		String emptyYear = null;
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
		model.addAttribute("emptyYear", emptyYear);
//		model.addAttribute("tasks", taskService.findUserTask(user));
		model.addAttribute("tasks", taskService.findUserTasksYear(user, year));
		
		return "views/profile";
	}
	
	@GetMapping("/profileYear")
	public String showProfilePageByYears(Model model,@RequestParam("selectedYear") String selectedYear, Principal principal) {
		String email = principal.getName();
		User user = userService.findOne(email);
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
		model.addAttribute("tasks", taskService.findUserTasksYear(user, selectedYear));
		
		return "views/profileTableData";
	}
	
	
	@PostMapping("/profile")
	public String showProfilePageByYear(Model model,@RequestParam("selectedYear") String selectedYear, Principal principal) {
		String email = principal.getName();
		User user = userService.findOne(email);
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
		model.addAttribute("tasks", taskService.findUserTasksYear(user, selectedYear));
		
		return "views/profile";
	}
	
	@GetMapping("/deleteTask")
	public String taskFrom(Long id, Model model, HttpSession session) {
		taskService.deleteTask(id);
		return "redirect:/profile";
	}
}
