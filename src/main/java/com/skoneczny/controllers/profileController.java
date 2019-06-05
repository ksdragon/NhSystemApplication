package com.skoneczny.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.Principal;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.skoneczny.api.ITaskService;
import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

@Controller
public class profileController {

	private final Logger logger = Logger.getLogger(profileController.class);
	@Autowired	private ITaskService taskService;
	@Autowired	private UserService userService;
	@Autowired	private ServletContext context;	
	
	
	@GetMapping("/profile")
	public String showProfilePage(Model model,
			@RequestParam(defaultValue="") String email,
//			@RequestParam("page") Optional<Integer> page,
//			@RequestParam("size") Optional<Integer> size,
//			@RequestParam("sort") Optional<String> sort,
			Principal principal,
			HttpSession session,
			HttpServletRequest request,
			Pageable pageable
			) {
		String currentPageSessionNameProfile = "profileCurrentPageSessionAttribute";
//		int clikedPage;
//		int currentPage = page.orElse(0/*1*/);
//		int pageSize = size.orElse(5);
		
		if(session.getAttribute("emailSession") != null) {
			if(!session.getAttribute("emailSession").equals(email)) {
				session.removeAttribute(currentPageSessionNameProfile);
			}
		}		
//		if(session.getAttribute(currentPageSessionNameProfile) != null) {
//			clikedPage = page.isPresent()? currentPage : (int) session.getAttribute(currentPageSessionNameProfile) ;
//			currentPage = clikedPage;
//		}else
//		{
//			clikedPage = currentPage;
//		}
//		Sort sortP = pageable.getSort();	
		
//        String sortParam = sort.orElse("startDate");
        String year = Integer.toString(LocalDate.now().getYear());		
		if(email.isEmpty()) email = principal.getName();
		User user = userService.findOne(email);
//		String allYear = "All";
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
//		model.addAttribute("allYear", allYear);				
		
		Page<Task> pageableListTasksYear = taskService.findUsersTasksPageableByYear(user, year, pageable);
//		List<Task> findUserTasksYear = taskService.findUserTasksYear(user, year,sortP);
//		Page<Task> listPaged = (Page<Task>) taskService.findPaginated(PageRequest.of(currentPage /*-1*/ , pageSize, sortP /*Sort.by(sortParam)*/),findUserTasksYear);	
//		PageWrapper<Task> pageWrapp = new PageWrapper<Task>(listPaged, "/profile");
		model.addAttribute("tasks", pageableListTasksYear);
//		model.addAttribute("size",pageSize);
//		model.addAttribute("pageWrapp", pageWrapp);
			
		
//		session.setAttribute(currentPageSessionNameProfile, clikedPage);
		session.setAttribute("emailSession", email);

		return "views/profile";
	
	}
	
	@GetMapping("/profileYear")
	public String showProfilePageByYears(
			Model model,
			@RequestParam(defaultValue="") String email,
			@RequestParam(defaultValue="profileTableData") String returnPage,
//			@RequestParam("page") Optional<Integer> page,
//			@RequestParam("size") Optional<Integer> size,
//			@RequestParam("sort") Optional<String> sort,
			@RequestParam("selectedYear") Optional<String> selectedYear,
			Principal principal,
			HttpSession session,
			HttpServletRequest request,
			Pageable pageable
			) {
		String currentPageSessionNameProfile = "profileCurrentPageSessionAttribute";
		if(session.getAttribute("emailSession") != null) {
			if(!session.getAttribute("emailSession").equals(email)) {
				session.removeAttribute(currentPageSessionNameProfile);
			}
		} 
//		int clikedPage;
//		int currentPage = /*pageable.getPageNumber();*/ page.orElse(0/*1*/);		
//		if(session.getAttribute(currentPageSessionNameProfile) != null) {
//			clikedPage = page.isPresent()? currentPage : (int) session.getAttribute(currentPageSessionNameProfile) ;
//			currentPage = clikedPage;
//		}else
//		{
//			clikedPage = currentPage;
//		}
//		Sort sortP = pageable.getSort();
		//Sort s = new Sort.Order(Direction.ASC, sort.orElse("aa"));
		
		
//        int pageSize = /*pageable.getPageSize();*/ size.orElse(5);
//        String sortParam = sort.orElse("startDate");
        String year = Integer.toString(LocalDate.now().getYear());		
		if(email.isEmpty()) email = principal.getName();
		User user = userService.findOne(email);
		//String allYear = "All";
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
		//model.addAttribute("allYear", allYear);				
		Page<Task> pageableListTasksYear = taskService.findUsersTasksPageableByYear(user, year, pageable);
//		List<Task> findUserTasksYear = taskService.findUserTasksYear(user, selectedYear.orElse(year),sortP);
//		Page<Task> listPaged = (Page<Task>) taskService.findPaginated(PageRequest.of(currentPage /*-1*/ , pageSize, sortP /*Sort.by(sortParam)*/),findUserTasksYear);	
//		PageWrapper<Task> pageWrapp = new PageWrapper<Task>(listPaged, "/profile");
		model.addAttribute("tasks", pageableListTasksYear);
//		model.addAttribute("pageWrapp", pageWrapp);			
//		model.addAttribute("size",pageSize);
//		session.setAttribute(currentPageSessionNameProfile, clikedPage  );
		session.setAttribute("emailSession", email);
		return "views/" + returnPage;
	}
	
	
	@GetMapping("/createPdf")
	public void createPdf (
			@RequestParam(defaultValue="") String email,
			@RequestParam(defaultValue="profileTableData") String returnPage,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("sort") Optional<String> sort,
			@RequestParam("selectedYear") Optional<String> selectedYear,
			Principal principal,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Pageable pageable) {
				Sort sortP = pageable.getSort();
				User user = userService.findOne(email);
				String year = Integer.toString(LocalDate.now().getYear());	
				List<Task> findUserTasksYear = taskService.findUserTasksYear(user, selectedYear.orElse(year),sortP);
				boolean isFlag = taskService.createPdf(findUserTasksYear,context,selectedYear.orElse(year));
				if(isFlag) {
					String fullPath = request.getServletContext().getRealPath("/resources/reports/"+"userTasks"+".pdf");
					taskService.filedownload(fullPath,response,"userTasks.pdf");
				}
	
	}
	
	@GetMapping("/createExcel")
	public void createExcel (
			@RequestParam(defaultValue="") String email,
			@RequestParam(defaultValue="profileTableData") String returnPage,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("sort") Optional<String> sort,
			@RequestParam("selectedYear") Optional<String> selectedYear,
			Principal principal,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Pageable pageable) {
		Sort sortP = pageable.getSort();
		User user = userService.findOne(email);
		String year = Integer.toString(LocalDate.now().getYear());	
		List<Task> findUserTasksYear = taskService.findUserTasksYear(user, selectedYear.orElse(year),sortP);
		boolean isFlag = taskService.createExcel(findUserTasksYear,context,selectedYear.orElse(year));
		if(isFlag) {
			String fullPath = request.getServletContext().getRealPath("/resources/reports/"+"userTasks"+".xls");
			taskService.filedownload(fullPath,response,"userTasks.xls");
		}
				
	}	
}
