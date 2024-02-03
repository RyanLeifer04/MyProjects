package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.AuthUser;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UsersDAO;

@RestController
@RequestMapping("users")
public class UserController{
	private static final Logger LOG = Logger.getLogger(UserController.class.getName());
	private UsersDAO userDao;

	public UserController(UsersDAO userDao){ this.userDao = userDao; }

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user){
		LOG.info("POST /users " + user);
		try{
			User newUser = userDao.register(user);
			if (newUser != null)
				return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
			else
				return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch(IOException e){
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/authenticate")
	public ResponseEntity<User> authenticate(@RequestBody AuthUser user){
		LOG.info(user.toString());
		try{
			if(userDao.authenticate(user.getUsername(),user.getPassword())){
				User returnUser = userDao.getUserByUsername(user.getUsername());
				return new ResponseEntity<User>(returnUser,HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch(IOException e){
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("")
	public ResponseEntity<User[]> getUsers(){
		LOG.info("GET /users");
		try{
			User[] users = userDao.getUsers();
			return new ResponseEntity<User[]>(users,HttpStatus.OK);
		} catch(IOException e){
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable int id){
		LOG.info("GET /users/" + id);
		try{
			User returnedUser = userDao.getUser(id);
			if(returnedUser != null)
				return new ResponseEntity<User>(returnedUser,HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}catch(IOException e){
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable int id){
		LOG.info("DELETE /users/" + id);
		try{
			User returned = userDao.deleteUser(id);
			if(returned != null){
				return new ResponseEntity<User>(returned,HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch(IOException e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user){
		LOG.info("PUT /users/" + id + " " + user);
		try{
			User returnedUser = userDao.updateUser(id,user);
			if (returnedUser != null){
				return new ResponseEntity<User>(returnedUser, HttpStatus.OK);
			} else{
				return register(user);
			}
		} catch (IOException e){
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
