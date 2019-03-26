package com.skoneczny.api;

import java.util.List;
import java.util.TreeSet;

import javax.validation.Valid;

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

}
