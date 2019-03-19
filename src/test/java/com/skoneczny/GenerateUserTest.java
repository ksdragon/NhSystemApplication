package com.skoneczny;




import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;
import com.skoneczny.initiate.GenarateTask;
import com.skoneczny.initiate.GenerateUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenerateUserTest {
	@Autowired
	private GenerateUser userGenerator;
	private List<User> listUser;
	private List<Task> listTask;
	@Autowired
	private GenarateTask taskGenerator;
	
	@Before
	public void init() {
		{			
			listUser = generateManyUser();			
			listTask = generateManyTasks();			
			for (User user : listUser) {
				generateManyTasks(user);
			}
		}		
		
	}
	
	 private List<Task> generateManyTasks(User x) {
		 List<Task> list = new ArrayList<Task>();
		 	for (int i = 0; i < 10; i++) {
		 		Task t = taskGenerator.generate();
		 		t.setUser(x);
		 		list.add(t);
	        }
			return list;
	}

	private List<User> generateManyUser() {
		 	List<User> listUser = new ArrayList<User>();
		 	for (int i = 0; i < 4; i++) {	            
		 		listUser.add(userGenerator.generate());
	        }
			return listUser;
	    }
	 
	 private List<Task> generateManyTasks() {
		 	List<Task> listTask = new ArrayList<Task>();
		 	for (int i = 0; i < 10; i++) {	            
		 		listTask.add(taskGenerator.generate());
	        }
			return listTask;
	    }
	
	@Test
	public void testUserGenerate() {
		
		assertNotNull(listUser);		
	}

}
