package com.skoneczny.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.skoneczny.entites.User;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/users")
	public String listUsers(Model model,
			@RequestParam(defaultValue="") String name,
			@RequestParam("selectedYear") Optional<String> selectedYear,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);        
		List<User> listOfUser = userService.findByName(name);
		Page<?> listOfUserPaged = taskService.findPaginated(PageRequest.of(currentPage -1 , pageSize), listOfUser);
		model.addAttribute("users",listOfUserPaged);
		model.addAttribute("years", taskService.getAllYeas(userService.findAll()));
		
		int totalPages = listOfUserPaged.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }		
		return "views/listUsers";
	}
	
	
	@GetMapping("/deleteUser")
	public String taskFrom(String email, Model model, HttpSession session) {
		userService.deleteUser(email);
//		session.setAttribute("email", email);
//		model.addAttribute("task", new Task());
		return "redirect:/users";
	}
	
	
}
