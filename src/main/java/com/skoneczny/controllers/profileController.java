package com.skoneczny.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
//			@RequestParam("sort") Optional<String> sort,
			Principal principal,
			HttpSession session,
			HttpServletRequest request,
			@SortDefault("startDate") Pageable pageable
			) {		
		
		if(session.getAttribute("emailSession") != null) {
			if(!session.getAttribute("emailSession").equals(email)) {
				session.removeAttribute("currentPage");
			}
		}
		Sort sort = pageable.getSort();			
//		int p = pageable.getPageNumber();
//		Integer page = new Integer(p);
		int clikedPage;
		int currentPage =  /*pageable.getPageNumber();*/ page.orElse(0/*1*/);		
		if(session.getAttribute("currentPage") != null) {
			clikedPage = page.isPresent()? currentPage : (int) session.getAttribute("currentPage") ;
			currentPage = clikedPage;
		}else
		{
			clikedPage = currentPage;
		}	
        int pageSize = /*pageable.getPageSize();*/ size.orElse(5);
//        String sortParam = sort.orElse("startDate");
        String year = Integer.toString(LocalDate.now().getYear());		
		if(email.isEmpty()) email = principal.getName();
		User user = userService.findOne(email);
		String allYear = "All";
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
		model.addAttribute("allYear", allYear);					
		Pageable pageable2 = PageRequest.of(currentPage /*-1*/ , pageSize, sort/*Sort.by(sortParam)*/);
		List<Task> findUserTasksYear = taskService.findUserTasksYear(user, year,pageable);
		Page<Task> listPaged = (Page<Task>) taskService.findPaginated(pageable,findUserTasksYear);

//		PageWrapper<Task> pageWrapp = new PageWrapper<Task>(listPaged, "/profile");
		model.addAttribute("tasks", listPaged);
//		model.addAttribute("pageWrapp", pageWrapp);
			
		
		session.setAttribute("currentPage", clikedPage  );
		session.setAttribute("emailSession", email);
		
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
