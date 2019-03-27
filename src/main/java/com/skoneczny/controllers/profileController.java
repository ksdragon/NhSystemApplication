package com.skoneczny.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.resource.HttpResource;

import com.skoneczny.entites.Task;
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
	public String showProfilePage(Model model,
			@RequestParam(defaultValue="") String email,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			Principal principal,
			@SortDefault("startDate") Pageable pageable) {
		int currentPage = page.orElse(0/*1*/);
        int pageSize = size.orElse(5);
		
        String year = Integer.toString(LocalDate.now().getYear());		
		if(email.isEmpty()) email = principal.getName();
		User user = userService.findOne(email);
		String allYear = "All";
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
		model.addAttribute("allYear", allYear);
		List<Task> findUserTasksYear = taskService.findUserTasksYear(user, year);
		Page<Task> listPaged = (Page<Task>) taskService.findPaginated(PageRequest.of(currentPage /*-1*/ , pageSize),findUserTasksYear);
		model.addAttribute("tasks", listPaged);
		
		int totalPages = listPaged.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		return "views/profile";
	
	}
	
	@GetMapping("/profileYear")
	public String showProfilePageByYears(
			Model model,
			@RequestParam("selectedYear") String selectedYear,
			@RequestParam(defaultValue="") String email,
			HttpServletResponse response,
			Principal principal,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@SortDefault("startDate") Pageable pageable
			) {
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
		if(email.isEmpty()) email = principal.getName();
		User user = userService.findOne(email);
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());		
		List<Task> findUserTasksYear = taskService.findUserTasksYear(user, selectedYear);
		Page<Task> listPaged = (Page<Task>) taskService.findPaginated(PageRequest.of(currentPage -1 , pageSize),findUserTasksYear);
		model.addAttribute("tasks", listPaged);
				
		int totalPages = listPaged.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		
		
		return "views/profileTableData";
	}
	
	
	/*@PostMapping("/profile")
	public String showProfilePageByYear(Model model,@RequestParam("selectedYear") String selectedYear, Principal principal) {
		String email = principal.getName();
		User user = userService.findOne(email);
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
		model.addAttribute("tasks", taskService.findUserTasksYear(user, selectedYear));
		
		return "views/profile";
	}*/	
	
}
