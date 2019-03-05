package com.skoneczny;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.skoneczny.api.IPasswordService;
import com.skoneczny.entites.Password;
import com.skoneczny.entites.Task;
import com.skoneczny.entites.User;
import com.skoneczny.repositories.PasswordRepository;
import com.skoneczny.services.TaskService;
import com.skoneczny.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NhSystemApplicationTests {

	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	@Autowired 
	private PasswordRepository passworRepository;
	@Autowired
	private IPasswordService iPasswordService;
	
	
	@Before
	public void intDb() {
//		{
//			User newUser = new User("testUser@mail.com", "testUser", "123456");
//			userService.createUser(newUser);
//		}
//		{
//			User newUser1 = new User("testAdmin@mail.com", "testAdmin", "123456");
//			userService.createAdmin(newUser1);
//		}
		{
//			Password pass = new Password();
//			pass.setId(1L);
//			pass.setNewPassword("12345");
//			pass.setEmail("one@one.pl");
			User newUser = new User("testUser@mail.com", "testUser", "123456");
			Password pass1 = new Password(newUser,"1234","12345","12345");
			iPasswordService.createPassword(pass1,newUser);
		}
//
//		Task userTask = new Task("03/01/2018", "00:11", "11:00", "You need to work today");
//		User user = userService.findOne("testUser@mail.com");
//		taskService.addTask(userTask, user);
	}

//	@Test
//	public void testUser() {
//		User user = userService.findOne("testUser@mail.com");
//		assertNotNull(user);
//		User admin = userService.findOne("testAdmin@mail.com");
//		assertEquals(admin.getEmail(), "testAdmin@mail.com");
//	}
//
//	@Test
//	public void testTask() {
//		User user = userService.findOne("testUser@mail.com");
//		List<Task> task = taskService.findUserTask(user);
//		assertNotNull(task);
//	}
	
	@Test
	public void testIsCorrectPassword() {
		User user = userService.findOne("one@one.pl");
		assertNotNull(user);		
		boolean correctPassword = iPasswordService.isCorrectPassword("one@one.pl", "1234");
		assertTrue(correctPassword);
	}

}
