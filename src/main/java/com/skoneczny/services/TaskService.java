package com.skoneczny.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;
import com.skoneczny.repositories.TaskRepository;


/**
 * @author HP ProDesk
 *
 */
@Service
public class TaskService {
	
	@Autowired TaskRepository taskRepository;
	
	public void addTask(Task task, User user) {
		task.setUser(user);
		taskRepository.save(task);
	}
	
	public List<Task> findUserTask(User user){
		return taskRepository.findByUser(user);
	}

	public void deleteTask(Long id) {
		taskRepository.deleteById(id);		
	}
	
	/**
	 * Metoda używana do pobrania lat z zadań poszczególnych użytkowników
	 *@param user
	 *@return lista lat posortowana malejąco
	 */
	public TreeSet<String> getAllYeas(User user){
		List<String> yList = new ArrayList<>();
		List<Task> tasks = taskRepository.findByUser(user);
		for (Task task : tasks) {
			yList.add(task.getDate().substring(0,4));			
		}
		TreeSet<String> yeasList = new  TreeSet<String>(yList);
		yeasList = (TreeSet<String>)yeasList.descendingSet();
		return yeasList;		
	}
		

	/*public List<Task> findUserTasksYear(User user, String year) {
		List<Task> tList = new ArrayList<>();
		
		List<Task> tasks = taskRepository.findByUser(user);
		for (Task task : tasks) {
			if(task.getDate().substring(0,4).equals((year))) {
				tList.add(task);
			};
		}
		return tList;
	}*/
	
	public List<Task> findUserTasksYear(User user, String year) {		
		return taskRepository.findByUser(user)
				.stream()
				.filter(task -> Objects.equals(task.getDate().substring(0,4), year))
				.collect(Collectors.toList());
	}

}
