package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
public class AuthUserTest {
	@Test
	public void testCreate(){
		//Setup
		String username = "username";
		String password = "password";
		String toString = "AuthUser [username='username',password='password']";

		//Invoke
		AuthUser user = new AuthUser(username,password);

		//Analyse
		assertEquals(username,user.getUsername());
		assertEquals(password,user.getPassword());
		assertEquals(toString,user.toString());
	}
}
