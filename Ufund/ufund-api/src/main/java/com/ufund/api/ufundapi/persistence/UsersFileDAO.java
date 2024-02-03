package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.ufund.api.ufundapi.model.User;

@Component
public class UsersFileDAO implements UsersDAO {
	private static final Logger LOG = Logger.getLogger(UsersFileDAO.class.getName());
	private Map<Integer, User> users;
	private ObjectMapper objectMapper;
	private static int nextID;
	private String filename;

	public UsersFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
		this.filename = filename;
		this.objectMapper = objectMapper;
		load();
	}

	private synchronized static int nextID() {
		int id = nextID;
		++nextID;
		return id;
	}

	private User[] getUsersArray() {
		ArrayList<User> userArrayList = new ArrayList<>(users.values());
		User[] userArray = new User[userArrayList.size()];
		userArrayList.toArray(userArray);
		return userArray;
	}

	private User getSingleUser(int id) {
		return users.get(id);
	}

	private User searchUsersByUsername(String username) {
		ArrayList<User> allUsers = new ArrayList<>(users.values());
		for (User user : allUsers) {
			if (user.getUsername().equals(username))
				return user;
		}
		return null;
	}

	private boolean save() throws IOException {
		User[] usersArray = getUsersArray();
		objectMapper.writeValue(new File(filename), usersArray);
		return true;
	}

	private boolean load() throws IOException {
		LOG.fine("Loading...");
		users = new HashMap<>();
		nextID = 0;
		User[] userArray = objectMapper.readValue(createFile(filename), User[].class);

		// add default admin user
		User admin = new User(0, "Admin", "", "admin", "admin", true);
		users.put(admin.getID(), admin);

		// load users from file
		for (User user : userArray) {
			users.put(user.getID(), user);
			if (user.getID() > nextID) {
				nextID = user.getID();
			}
		}
		++nextID;
		return true;
	}

	public User register(User user) throws IOException {
		synchronized (users) {
			for (User existingUser : users.values()) {
				if (existingUser.getUsername().equals(user.getUsername()))
					return null;
			}
			User newUser = new User(nextID(), user);
			users.put(newUser.getID(), newUser);
			save();
			return newUser;
		}
	}

	public boolean authenticate(String username, String password) throws IOException {
		synchronized (users) {
			for (User existingUser : users.values()) {
				try {
					if (existingUser.getUsername().equals(username) &&
							existingUser.isPassword(password))
						return true;
				} catch (Exception e) {
					return false;
				}
			}
			return false;
		}
	}

	public User updateUser(int id, User user) throws IOException {
		synchronized (users) {
			if (!users.containsKey(user.getID()))
				return null;
			users.put(user.getID(), user);
			save();
			return user;
		}
	}

	public User deleteUser(int id) throws IOException {
		synchronized (users) {
			if (users.containsKey(id)) {
				User returnValue = users.get(id);
				users.remove(id);
				save();
				return returnValue;
			} else
				return null;
		}
	}

	public User[] getUsers() throws IOException {
		synchronized (users) {
			return getUsersArray();
		}
	}

	public User getUser(int id) throws IOException {
		synchronized (users) {
			return getSingleUser(id);
		}
	}

	public User getUserByUsername(String username) throws IOException {
		synchronized (users) {
			return searchUsersByUsername(username);
		}
	}

	/**
	 * Creates the storage File object, creating the file if it does not exist.
	 *
	 * @param filename The file to create
	 *
	 * @return The file object
	 *
	 * @throws IOException when file cannot be accessed/created
	 */
	private File createFile(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			try {
				if (file.getParentFile() != null) {
					file.getParentFile().mkdirs();
				}
				if (!file.createNewFile()) {
					LOG.severe("Could not create file " + filename);
					throw new IOException("Could not create file " + filename);
				}
				// write empty JSON array to file
				objectMapper.writeValue(file, new User[0]);
				LOG.info(filename + " created");
			} catch (IOException e) {
				LOG.severe("Could not create file " + filename);
				throw e;
			}
		}
		return file;
	}
}
