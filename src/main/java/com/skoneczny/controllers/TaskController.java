package com.skoneczny.controllers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.skoneczny.entites.CategoryTask;
import com.skoneczny.entites.Task;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

@Controller
public class TaskController {
		
	private final DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final DateTimeFormatter formatterTimeHourMinute = DateTimeFormatter.ofPattern("HH:mm");
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/addTask")
	public String taskFrom(String email, Model model, HttpSession session) {
		
		LocalDateTime localDateTime = LocalDateTime.now();
						
		model.addAttribute("email", email);		
		Task newTask = new Task();		
		newTask.setStartDate(localDateTime.format(formatterDay));
		newTask.setStartTime(localDateTime.format(formatterTimeHourMinute));
		
		model.addAttribute("task", newTask);
		return "views/taskForm";
	}
	
	@PostMapping("/addTask")
	public String addTask(@Valid Task task, BindingResult bindingResult, HttpSession session) {
		if(bindingResult.hasErrors()) {
			return "views/taskForm";
		}
		String email = (String)session.getAttribute("email");
		taskService.addTask(task, userService.findOne(email));
		return "redirect:/profile?email=" + email;
	}
}
