package com.skoneczny.api;

import java.util.List;

import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;

public interface InitiateDataApi {

	List<Task> generateManyTasks(User x);
	
	List<User> generateManyUser();
	
	List<Task> generateManyTasks();
	
}
