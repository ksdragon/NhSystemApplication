package com.skoneczny.api;

import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;

public interface ITaskService {
	
	void addTask(Task task, User user);
	
	List<Task> findUserTask(User user);
	
	void deleteTask(Long id);
	
	TreeSet<String> getAllYeas(User user);
	
	public List<Task> findUserTasksYear(User user, String year);

	boolean checkTimeStopIsCorrect(@Valid Task task);

	String startTimePlusDuration(@Valid Task task);

	Task findTaskById(Long id);

	void approvDeapprovTask(Long id);

	List<Task> findUserTasksYear(User user, String year, Sort sortP);
	
	Page<?> findPaginated(Pageable pageable, List<?> listToPage);	

	boolean createPdf(List<Task> findUserTasksYear, ServletContext context, String year);

	boolean createExcel(List<Task> findUserTasksYear, ServletContext context, String orElse);

	boolean createExcelAllUsersTasks(List<User> usersList, String selectedYear, Sort sortP, ServletContext context);

	TreeSet<String> getAllYeas(List<User> user);

	void filedownload(String fullPath, HttpServletResponse response, String fileName);

	Page<Task> findUsersTasksPageableByYear(User user, String year, Pageable pageable);

}
