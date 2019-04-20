package com.skoneczny.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	@Autowired	private ITaskService taskService;
	@Autowired	private UserService userService;
	@Autowired	private ServletContext context;	
	
	
	@GetMapping("/profile")
	public String showProfilePage(Model model,
			@RequestParam(defaultValue="") String email,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("sort") Optional<String> sort,
			Principal principal,
			HttpSession session,
			HttpServletRequest request,
			Pageable pageable
			) {
		int clikedPage;
		int currentPage = page.orElse(0/*1*/);
		int pageSize = size.orElse(5);
		
		if(session.getAttribute("emailSession") != null) {
			if(!session.getAttribute("emailSession").equals(email)) {
				session.removeAttribute("currentPage");
			}
		}		
		if(session.getAttribute("currentPage") != null) {
			clikedPage = page.isPresent()? currentPage : (int) session.getAttribute("currentPage") ;
			currentPage = clikedPage;
		}else
		{
			clikedPage = currentPage;
		}
		Sort sortP = pageable.getSort();	
		
        String sortParam = sort.orElse("startDate");
        String year = Integer.toString(LocalDate.now().getYear());		
		if(email.isEmpty()) email = principal.getName();
		User user = userService.findOne(email);
//		String allYear = "All";
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
//		model.addAttribute("allYear", allYear);				
		
		List<Task> findUserTasksYear = taskService.findUserTasksYear(user, year,sortP);
		Page<Task> listPaged = (Page<Task>) taskService.findPaginated(PageRequest.of(currentPage /*-1*/ , pageSize, sortP /*Sort.by(sortParam)*/),findUserTasksYear);	
//		PageWrapper<Task> pageWrapp = new PageWrapper<Task>(listPaged, "/profile");
		model.addAttribute("tasks", listPaged);
//		model.addAttribute("pageWrapp", pageWrapp);
			
		
		session.setAttribute("currentPage", clikedPage  );
		session.setAttribute("emailSession", email);
		
//		int totalPages = listPaged.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                .boxed()
//                .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
		return "views/profile";
	
	}
	
	@GetMapping("/profileYear")
	public String showProfilePageByYears(
			Model model,
			@RequestParam(defaultValue="") String email,
			@RequestParam(defaultValue="profileTableData") String returnPage,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("sort") Optional<String> sort,
			@RequestParam("selectedYear") Optional<String> selectedYear,
			Principal principal,
			HttpSession session,
			HttpServletRequest request,
			Pageable pageable
			) {		
		if(session.getAttribute("emailSession") != null) {
			if(!session.getAttribute("emailSession").equals(email)) {
				session.removeAttribute("currentPage");
			}
		} 
		int clikedPage;
		int currentPage = /*pageable.getPageNumber();*/ page.orElse(0/*1*/);		
		if(session.getAttribute("currentPage") != null) {
			clikedPage = page.isPresent()? currentPage : (int) session.getAttribute("currentPage") ;
			currentPage = clikedPage;
		}else
		{
			clikedPage = currentPage;
		}
		Sort sortP = pageable.getSort();
		//Sort s = new Sort.Order(Direction.ASC, sort.orElse("aa"));
		
		
        int pageSize = /*pageable.getPageSize();*/ size.orElse(5);
        String sortParam = sort.orElse("startDate");
        String year = Integer.toString(LocalDate.now().getYear());		
		if(email.isEmpty()) email = principal.getName();
		User user = userService.findOne(email);
		//String allYear = "All";
		model.addAttribute("years", taskService.getAllYeas(user));
		model.addAttribute("email",user.getEmail());
		//model.addAttribute("allYear", allYear);				
		
		List<Task> findUserTasksYear = taskService.findUserTasksYear(user, selectedYear.orElse(year),sortP);
		Page<Task> listPaged = (Page<Task>) taskService.findPaginated(PageRequest.of(currentPage /*-1*/ , pageSize, sortP /*Sort.by(sortParam)*/),findUserTasksYear);	
//		PageWrapper<Task> pageWrapp = new PageWrapper<Task>(listPaged, "/profile");
		model.addAttribute("tasks", listPaged);
//		model.addAttribute("pageWrapp", pageWrapp);
			
		
		session.setAttribute("currentPage", clikedPage  );
		session.setAttribute("emailSession", email);
				
//		int totalPages = listPaged.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                .boxed()
//                .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
//		
		
		return "views/" + returnPage;
	}
	
	
	@GetMapping("/createPdf")
	public void createPdf (
			@RequestParam("selectedYear") Optional<String> selectedYear,
			@RequestParam(defaultValue="") String email, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			Pageable pageable) {
				Sort sortP = pageable.getSort();
				User user = userService.findOne(email);
				String year = Integer.toString(LocalDate.now().getYear());	
				List<Task> findUserTasksYear = taskService.findUserTasksYear(user, selectedYear.orElse(year),sortP);
				boolean isFlag = taskService.createPdf(findUserTasksYear,context);
				if(isFlag) {
					String fullPath = request.getServletContext().getRealPath("/resources/reports/"+"userTasks"+".pdf");
					filedownload(fullPath,response,"userTasks.pdf");
				}
	
	}

	private void filedownload(String fullPath, HttpServletResponse response, String fileName) {
		File file = new File(fullPath);
		final int BUFFER_SIZE = 4096;
		if(file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				String mimeType = context.getMimeType(fullPath);
				response.setContentType(mimeType);
				response.setHeader("content-disposition", "attachment; filename="+fileName);
				OutputStream outputStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;
				while((bytesRead = inputStream.read(buffer))!= -1) {
					outputStream.write(buffer,0,bytesRead);
				}
				inputStream.close();
				outputStream.close();
				file.delete();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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
