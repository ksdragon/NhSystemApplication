package com.skoneczny.api;

import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;
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

}
