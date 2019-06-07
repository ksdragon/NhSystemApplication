package com.skoneczny.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.skoneczny.entites.User;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

@Controller
public class UserController {
	
	private final Logger logger = Logger.getLogger(UserController.class);
	@Autowired	private UserService userService;
	@Autowired	private TaskService taskService;
	@Autowired	private ServletContext context;	
	
	@GetMapping("/users")
	public String listUsers(Model model,
			@RequestParam(defaultValue="") String name,
			@RequestParam("selectedYear") Optional<String> selectedYear,
			@RequestParam(defaultValue="views/listUsers") String returnPage,
			Pageable pageable,
			HttpSession session,
			HttpServletRequest request) {
		String pageableSessionAtrrtibute = "pageableSessionAtrrtibute";	
		String year = Integer.toString(LocalDate.now().getYear());	
        if(!request.getHeader("referer").contains("users")) {
        	pageable =  (Pageable) session.getAttribute(pageableSessionAtrrtibute);
        }        
        Page<User> listOfUserPageable = userService.findByName(name,pageable);		
        model.addAttribute("users",listOfUserPageable);
		model.addAttribute("years", taskService.getAllYeas(userService.findAll()));
		session.setAttribute(pageableSessionAtrrtibute, pageable);
		model.addAttribute("selectedYear", selectedYear.orElse(year));
		return returnPage;
	}
	
	
	@GetMapping("/createExcelAllUsersTasks")
	public void createExcelAll(
			@RequestParam Optional<List<User>> users,
			@RequestParam("selectedYear") Optional<String> selectedYear,
			HttpServletRequest request, 
			HttpServletResponse response			
			) {
			logger.info("createExcelAllUsersTasks");
			Sort sortP = new Sort(Direction.ASC,"startDate");
			List<User> usersList = new ArrayList<User>(userService.findAll());
			String year = Integer.toString(LocalDate.now().getYear());
			boolean isFlag = taskService.createExcelAllUsersTasks(usersList,selectedYear.orElse(year),sortP,context);
			if(isFlag) {
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+"userAllTasks"+".xls");
				taskService.filedownload(fullPath,response,"userAllTasks.xls");
			}
			
	}
	
	@GetMapping("/deleteUser")
	public String taskFrom(String email, @RequestParam(defaultValue="views/listUsers") String returnPage) {
		userService.deleteUser(email);
		return "redirect:/users?returnPage=" + returnPage;		
	}
	
}
