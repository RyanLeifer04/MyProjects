package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.ufund.api.ufundapi.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.*;

@Tag("Persistence-tier")
public class UserFileDAOTest {
	private UsersFileDAO usersFileDAO;
	private User[] testUsers;
	private ObjectMapper mockObjectWrapper;

	@BeforeEach
	public void initialise() throws IOException {
		mockObjectWrapper = mock(ObjectMapper.class);
		testUsers = new User[3];

		testUsers[0] = new User(0, "Sage", "Powell", "cars_4_catz", "a_unique_password", true);
		testUsers[1] = new User(3, "Brie", "Tanwen", "ice-queen", "a-better-password", false);
		testUsers[2] = new User(14, "Rizal", "Holt", "not!a!chicken", "the@best#password", false);

		when(mockObjectWrapper.readValue(new File("doesnt_matter.txt"), User[].class)).thenReturn(testUsers);
		// when(mockObjectWrapper.writeValue(new
		// File("doesnt_matter.txt"),User[].class)).
		usersFileDAO = new UsersFileDAO("doesnt_matter.txt", mockObjectWrapper);
	}

	@Test
	public void testGetUsers() throws IOException {
		User[] users = usersFileDAO.getUsers();

		assertEquals(users.length, testUsers.length);
		for (int i = 0; i < testUsers.length; i++) {
			assertEquals(users[i], testUsers[i]);
		}
	}

	@Test
	public void testGetSingleUser() throws IOException {
		User user = usersFileDAO.getUser(0);
		assertEquals(user, testUsers[0]);
	}

	@Test
	public void testRegister() throws IOException {
		User newUser = new User(2, "Ivy", "Holland", "snowscape", "an^even=better+password", false);
		usersFileDAO.register(newUser);
		assertNotEquals(testUsers, usersFileDAO.getUsers());
	}

	@Test
	public void testRegisterFail() throws IOException {
		User newUser = new User(3, "Brie", "Tanwen", "ice-queen", "a-better-password", false);
		assertNull(usersFileDAO.register(newUser));
	}

	@Test
	public void testGetByUsername() throws IOException {
		User returnedVal = usersFileDAO.getUserByUsername("ice-queen");
		assertEquals(returnedVal, testUsers[1]);
	}

	@Test
	public void testGetByUsernameFail() throws IOException {
		User returnedVal = usersFileDAO.getUserByUsername("this_will_fail");
		assertNull(returnedVal);
	}

	@Test
	public void testAuthenticate() throws IOException {
		boolean returnedVal = usersFileDAO.authenticate("ice-queen", "a-better-password");
		assertTrue(returnedVal);
	}

	@Test
	public void testAuthenticateFailUser() throws IOException {
		boolean returnedVal = usersFileDAO.authenticate("this_will_fail", "a-better-password");
		assertTrue(!returnedVal);
	}

	@Test
	public void testAuthenticateFailPass() throws IOException {
		boolean returnedVal = usersFileDAO.authenticate("ice-queen", "this_will_fail");
		assertTrue(!returnedVal);
	}

	@Test
	public void testAuthenticateException() throws IOException {
		boolean returnedVal = usersFileDAO.authenticate("ice-queen", "a-better-password");
		assertTrue(returnedVal);
	}

	@Test
	public void testUpdateUser() throws IOException {
		User newUser = new User(0, "Ivy", "Holland", "snowscape", "an^even=better+password", false);
		User returnedVal = usersFileDAO.updateUser(0, newUser);
		assertNotNull(returnedVal);
	}

	@Test
	public void testUpdateUserFail() throws IOException {
		User newUser = new User(289, "Ivy", "Holland", "snowscape", "an^even=better+password", false);
		User returnedVal = usersFileDAO.updateUser(99, newUser);
		assertNull(returnedVal);
	}

	@Test
	public void testDeleteUser() throws IOException {
		User returnedVal = usersFileDAO.deleteUser(0);
		assertNotNull(returnedVal);
	}

	@Test
	public void testDeleteUserFail() throws IOException {
		User returnedVal = usersFileDAO.deleteUser(999);
		assertNull(returnedVal);
	}
}
