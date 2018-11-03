package com.skoneczny;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NhSystemApplicationTests {

	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	
	@Before
	public void intDb() {
		{
		User newUser = new User("testUser@mail.com","testUser","123456");
		userService.createUser(newUser);
	}
	{
		User newUser1 = new User("testAdmin@mail.com","testAdmin","123456");
		userService.createAdmin(newUser1);
	}
	
	Task  userTask = new Task("03/01/2018","00:11","11:00","You need to work today");
	User user = userService.findOne("testUser@mail.com");
	taskService.addTask(userTask, user);
}
	
	@Test
	public void testUser() {
		User user = userService.findOne("testUser@mail.com");
		assertNotNull(user);
		User admin = userService.findOne("testAdmin@mail.com");
		assertEquals(admin.getEmail(), "testAdmin@mail.com");
	}
	
	@Test
	public void testTask() {
		User user = userService.findOne("testUser@mail.com");
		List<Task> task = taskService.findUserTask(user);
		assertNotNull(task);
	}

}
