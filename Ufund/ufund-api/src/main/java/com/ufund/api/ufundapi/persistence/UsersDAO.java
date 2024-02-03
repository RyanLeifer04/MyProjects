package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.User;

public interface UsersDAO{
	User register(User user) throws IOException;

	boolean authenticate(String username, String password) throws IOException;

	User updateUser(int id, User user) throws IOException;

	User deleteUser(int id) throws IOException;

	User[] getUsers() throws IOException;
	
	User getUser(int id) throws IOException;

	User getUserByUsername(String username) throws IOException;
}
