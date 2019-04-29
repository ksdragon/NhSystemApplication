package com.skoneczny.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.skoneczny.entites.Task;
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
			@RequestParam(defaultValue="listUsers") String returnPage,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("sort") Optional<String> sort,
			Pageable pageable,
			HttpSession session,
			HttpServletRequest request) {
		String currentPageSessionName = "userListCurrentPageSessionAttribute";
		int clikedPage;
		int currentPage = page.orElse(0);
        int pageSize = size.orElse(2);
        if(session.getAttribute(currentPageSessionName) != null) {
			clikedPage = page.isPresent()? currentPage : (int) session.getAttribute(currentPageSessionName) ;
			currentPage = clikedPage;
		}else
		{
			clikedPage = currentPage;
		}
        Sort sortP = pageable.getSort();     
        
//        List<Task> findUserTasksYear = taskService.findUserTasksYear(user, year,sortP);
//		Page<Task> listPaged = (Page<Task>) taskService.findPaginated(PageRequest.of(currentPage /*-1*/ , pageSize, sortP /*Sort.by(sortParam)*/),findUserTasksYear);	
        
		List<User> listOfUser = userService.findByName(name,sortP);		
		Page<User> listOfUserPaged = (Page<User>) taskService.findPaginated(PageRequest.of(currentPage, pageSize,sortP), listOfUser);
		model.addAttribute("users",listOfUserPaged);
		model.addAttribute("years", taskService.getAllYeas(userService.findAll()));
		session.setAttribute(currentPageSessionName, clikedPage);

		return "views/" + returnPage;
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
	public String taskFrom(String email, Model model, HttpSession session) {
		userService.deleteUser(email);
//		session.setAttribute("email", email);
//		model.addAttribute("task", new Task());
		return "redirect:/users";
	}
	
	
}
