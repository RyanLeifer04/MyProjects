package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Need class
 *
 * @author Blizzard Finnegan
 */

@Tag("Model-tier")
public class UserTest {
	@Test
	public void testCreate() {
		// Setup
		int id = 0;
		String first = "first";
		String last = "last";
		String username = "user";
		String clear_pass = "clear_pass";
		boolean admin = true;
		// invoke
		User user = new User(id, first, last, username, clear_pass, admin);

		// Analyse
		assertEquals(id, user.getID());
		assertTrue(user.isPassword(clear_pass));
		assertEquals(last, user.getLastName());
		assertEquals(first, user.getFirstName());
		assertEquals(admin, user.isAdmin());
		assertEquals(username, user.getUsername());
	}

	@Test
	public void testClone() {
		// Setup
		int id = 0;
		String first = "first";
		String last = "last";
		String username = "user";
		String clear_pass = "clear_pass";
		boolean admin = true;
		User user = new User(id, first, last, username, clear_pass, admin);
		int newID = 2;

		// invoke
		User user2 = new User(newID, user);

		// Analyse
		assertEquals(newID, user2.getID());
		assertTrue(user2.isPassword(clear_pass));
		assertEquals(last, user2.getLastName());
		assertEquals(first, user2.getFirstName());
		assertEquals(admin, user2.isAdmin());
		assertEquals(username, user2.getUsername());
	}

	@Test
	public void testUsernameMod() {
		// Setup
		int id = 0;
		String first = "first";
		String last = "last";
		String username = "user";
		String clear_pass = "clear_pass";
		boolean admin = true;
		User user = new User(id, first, last, username, clear_pass, admin);
		String newUsername = "better_username";

		// Invoke
		user.setUsername(newUsername);

		// Analyse
		assertEquals(newUsername, user.getUsername());
	}

	@Test
	public void testAdminMod() {
		// Setup
		int id = 0;
		String first = "first";
		String last = "last";
		String username = "user";
		String clear_pass = "clear_pass";
		boolean admin = true;
		User user = new User(id, first, last, username, clear_pass, admin);
		boolean newAdmin = false;

		// Invoke
		user.setAdmin(newAdmin);

		// Analyse
		assertEquals(newAdmin, user.isAdmin());
	}

	@Test
	public void testDefaultAdmin() {
		// Setup
		int id = 0;
		String first = "first";
		String last = "last";
		String username = "user";
		String clear_pass = null;
		boolean admin = true;
		User user = new User(id, first, last, username, clear_pass, admin);

		// Analyse
		assertTrue(user.isPassword("admin"));
	}

	@Test
	public void testPersonalInfo() {
		// Setup
		int id = 0;
		String first = "first";
		String last = "last";
		String username = "user";
		String clear_pass = "test";
		boolean admin = true;
		User user = new User(id, first, last, username, clear_pass, admin);

		// Invoke
		user.setFirstName("new_first");
		user.setLastName("new_last");

		// Analyse
		assertEquals("new_first", user.getFirstName());
		assertEquals("new_last", user.getLastName());
	}
}
