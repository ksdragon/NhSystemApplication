package com.skoneczny.initiate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.skoneczny.api.InitiateDataApi;
import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

public class InitiateData implements InitiateDataApi {

	@Autowired
	private GenarateTask taskGenerator;
	@Autowired
	private GenerateUser userGenerator;
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;

	@Override
	public List<Task> generateManyTasks(User x) {
		List<Task> list = new ArrayList<Task>();
	 	for (int i = 0; i < 10; i++) {
	 		Task t = taskGenerator.generate();
	 		taskService.addTask(t, x);
	 		t.setUser(x);
	 		list.add(t);	 		
        }
		return list;
	}

	@Override
	public List<User> generateManyUser() {
		List<User> listUser = new ArrayList<User>();
	 	for (int i = 0; i < 4; i++) {	            
	 		User user = userGenerator.generate();
			listUser.add(user);
			userService.saveRegisteredUser(user);			
        }
		return listUser;
	}

	@Override
	public List<Task> generateManyTasks() {
		List<Task> listTask = new ArrayList<Task>();
	 	for (int i = 0; i < 10; i++) {	            
	 		listTask.add(taskGenerator.generate());
        }
		return listTask;
	}

}
