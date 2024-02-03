package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.model.AuthUser;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UsersDAO;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

/**
 * Test the Need Controller class
 *
 * @author Blizzard Finnegan
 */

@Tag("Controller-tier")
public class UserControllerTest {
	private UserController userController;
	private UsersDAO mockUserDao;

	@BeforeEach
	public void setupUserController(){
		mockUserDao = mock(UsersDAO.class);
		userController = new UserController(mockUserDao);
	}

	@Test
	public void testCreateUser() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(user);

			//Invoke
			ResponseEntity<User> response = userController.register(user);

			//Analyse
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			assertEquals(user, response.getBody());
		}catch (Exception e){}
	}

	@Test
	public void testCreateNeedFail() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(null);

			//Invoke
			ResponseEntity<User> response = userController.register(user);

			//Analyse
			assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testCreateNeedHandleException() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			doThrow(new IOException()).when(mockUserDao).register(user);

			//Invoke
			ResponseEntity<User> response = userController.register(user);

			//Analyse
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testGetUsers() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			User user2 = new User(2,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(user);
			userController.register(user);
			userController.register(user2);
			User[] userArray = {user, user2};
			when(mockUserDao.getUsers()).thenReturn(userArray);

			//Invoke
			ResponseEntity<User[]> response = userController.getUsers();

			//Analyse
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(userArray, response.getBody());
		}catch (Exception e){}
	}

	@Test
	public void testGetUsersHandleException() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			doThrow(new IOException()).when(mockUserDao).getUsers();

			//Invoke
			ResponseEntity<User[]> response = userController.getUsers();

			//Analyse
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testAuth() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(user);
			userController.register(user);
			when(mockUserDao.authenticate("username","clear_pass")).thenReturn(true);
			when(mockUserDao.getUserByUsername("username")).thenReturn(user);
			AuthUser auth = new AuthUser("username","clear_pass");

			//Invoke
			ResponseEntity<User> response = userController.authenticate(auth);

			//Analyse
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(user, response.getBody());
		}catch (Exception e){}
	}

	@Test
	public void testAuthFail() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(user);
			userController.register(user);
			when(mockUserDao.authenticate("username","clear_pass")).thenReturn(false);
			when(mockUserDao.getUserByUsername("username")).thenReturn(user);
			AuthUser auth = new AuthUser("username","clear_pass");

			//Invoke
			ResponseEntity<User> response = userController.authenticate(auth);

			//Analyse
			assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testAuthHandleException() throws IOException{
		try{
			//Setup
			doThrow(new IOException()).when(mockUserDao).authenticate("username","clear_pass");

			//Invoke
			ResponseEntity<User> response = userController.authenticate(new AuthUser("username","clear_pass"));

			//Analyse
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testGetSingle() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(user);
			userController.register(user);
			when(mockUserDao.getUser(0)).thenReturn(user);

			//Invoke
			ResponseEntity<User> response = userController.getUser(0);

			//Analyse
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(user, response.getBody());
		}catch (Exception e){}
	}

	@Test
	public void testGetSingleFail() throws IOException{
		try{
			//Setup
			when(mockUserDao.getUser(0)).thenReturn(null);

			//Invoke
			ResponseEntity<User> response = userController.getUser(0);

			//Analyse
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testGetSingleHandleException() throws IOException{
		try{
			//Setup
			doThrow(new IOException()).when(mockUserDao).getUser(0);

			//Invoke
			ResponseEntity<User> response = userController.getUser(0);

			//Analyse
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testDelete() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(user);
			userController.register(user);
			when(mockUserDao.deleteUser(0)).thenReturn(user);

			//Invoke
			ResponseEntity<User> response = userController.deleteUser(0);

			//Analyse
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(user, response.getBody());
		}catch (Exception e){}
	}

	@Test
	public void testDeleteFail() throws IOException{
		try{
			//Setup
			when(mockUserDao.deleteUser(0)).thenReturn(null);

			//Invoke
			ResponseEntity<User> response = userController.deleteUser(0);

			//Analyse
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testDeleteHandleException() throws IOException{
		try{
			//Setup
			doThrow(new IOException()).when(mockUserDao).deleteUser(0);

			//Invoke
			ResponseEntity<User> response = userController.deleteUser(0);

			//Analyse
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		}catch (Exception e){}
	}

	@Test
	public void testUpdateUser() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			User user2 = new User(2,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(user);
			userController.register(user);
			userController.register(user2);
			User[] userArray = {user, user2};
			when(mockUserDao.updateUser(0,user2)).thenReturn(user2);

			//Invoke
			ResponseEntity<User> response = userController.updateUser(0,user2);

			//Analyse
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(user2, response.getBody());
		}catch (Exception e){}
	}

	@Test
	public void testUpdateUserPseudoFail() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			User user2 = new User(2,"first","last","username","clear_pass",true);
			when(mockUserDao.register(user)).thenReturn(user);
			userController.register(user);
			when(mockUserDao.updateUser(0,user2)).thenReturn(null);
			when(mockUserDao.register(user2)).thenReturn(user2);

			//Invoke
			ResponseEntity<User> response = userController.updateUser(0,user2);

			//Analyse
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			assertEquals(user2, response.getBody());
		}catch (Exception e){}
	}

	@Test
	public void testUpdateUserHandleException() throws IOException{
		try{
			//Setup
			User user = new User(0,"first","last","username","clear_pass",true);
			doThrow(new IOException()).when(mockUserDao).updateUser(0,user);

			//Invoke
			ResponseEntity<User> response = userController.updateUser(0,user);

			//Analyse
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		}catch (Exception e){}
	}
}
