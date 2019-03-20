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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.skoneczny.api.ITaskService;
import com.skoneczny.entites.CategoryTask;
import com.skoneczny.entites.Task;
import com.skoneczny.repositories.CategoryTaskRepository;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

@Controller
public class TaskController {
		
	private final DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final DateTimeFormatter formatterTimeHourMinute = DateTimeFormatter.ofPattern("HH:mm");
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryTaskRepository categoryTask;
	
	@GetMapping("/addTask")
	public String taskFrom(String email, Model model) {
		
		LocalDateTime localDateTime = LocalDateTime.now();
						
		model.addAttribute("email", email);		
		Task newTask = new Task();		
		newTask.setStartDate(localDateTime.format(formatterDay));
		newTask.setStartTime(localDateTime.format(formatterTimeHourMinute));
		model.addAttribute("categoryTask",categoryTask.findAll());
		model.addAttribute("task", newTask);
		return "views/taskForm";
	}
	
	@PostMapping("/addTask")
	public String addTask(@Valid Task task,String email, BindingResult bindingResult) {
		
		boolean checkTimeStopIsCorrect = taskService.checkTimeStopIsCorrect(task);
		if(bindingResult.hasErrors()) {
			return "views/taskForm";
		}
		taskService.addTask(task, userService.findOne(email));
		return "redirect:/profile?email=" + email;
	}
}
