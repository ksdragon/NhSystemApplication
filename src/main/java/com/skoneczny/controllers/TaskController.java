package com.skoneczny.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	private final Logger logger = Logger.getLogger(TaskController.class);
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryTaskRepository categoryTask;
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/addTask")
	public String taskFrom(String email, Model model) {		
		LocalDateTime localDateTime = LocalDateTime.now();						
		model.addAttribute("email", email);		
		Task newTask = new Task();		
		newTask.setStartDate(localDateTime.format(formatterDay));
		newTask.setStopDate(localDateTime.format(formatterDay));
		newTask.setStartTime(localDateTime.format(formatterTimeHourMinute));
		newTask.setCategoryTasks(new CategoryTask());
		model.addAttribute("categoryTask",categoryTask.findAll());
		model.addAttribute("task", newTask);
		return "views/taskForm";
	}
	
	@PostMapping("/addTask")
	public String addTask(@Valid Task task, BindingResult bindingResult, String email, Model model) {
		model.addAttribute("categoryTask", categoryTask.findAll());
		model.addAttribute("email", email);
		if (task.getCategoryTasks() == null){
			task.setCategoryTasks(new CategoryTask());
		}
		boolean checkTimeStopIsCorrect = taskService.checkTimeStopIsCorrect(task);		
		if (bindingResult.hasErrors()) {
			if (!checkTimeStopIsCorrect & !task.getDuration().isEmpty()) {
				model.addAttribute("checkTimeStopIsCorrect", checkTimeStopIsCorrect);
			}
			return "views/taskForm";
		}
		
		if (!checkTimeStopIsCorrect) {
			if (!LocalDate.parse(task.getStartDate()).isBefore(LocalDate.parse(task.getStopDate()))) {
				//logger.warn("Date is equal or less than start Date");
				bindingResult.rejectValue("stopDate", "error.task",
						messageSource.getMessage("error.task.stopTime", null, Locale.getDefault()));
				model.addAttribute("checkTimeStopIsCorrect", checkTimeStopIsCorrect);
				return "views/taskForm";

			}
		}
		task.setStopTime(taskService.startTimePlusDuration(task));
		taskService.addTask(task, userService.findOne(email));
		return "redirect:/profile?email=" + email;
	}
	
	@GetMapping("/deleteTask")
	public String taskFrom(Long id, Model model, HttpSession session) {
		Task taskById = taskService.findTaskById(id);
		String email = taskById.getUser().getEmail();
		taskService.deleteTask(id);		
		return "redirect:/profile?email=" + email;
	}
	
	@GetMapping("/editTask")
	public String editTaskFrom(Long id,String email, Model model) {
		model.addAttribute("email", email);	
		Task taskById = taskService.findTaskById(id);		
		model.addAttribute("categoryTask",categoryTask.findAll());
		model.addAttribute("task", taskById);
		return "views/taskForm";
	}
	
	@GetMapping("/approvTask")
	public String approvTaskFrom(Long id, Model model, HttpSession session) {
		Task taskById = taskService.findTaskById(id);
		String email = taskById.getUser().getEmail();
		taskService.approvDeapprovTask(id);		
		return "redirect:/profile?email=" + email;
		}	
	
}
