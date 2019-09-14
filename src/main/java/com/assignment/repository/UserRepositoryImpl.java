package com.assignment.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.assignment.controller.UserController;
import com.assignment.entity.User;

@Repository
public class UserRepositoryImpl implements UserRepository{
	
	private Map<String,User> userStorage;
	
	private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
	
	public UserRepositoryImpl() {
		userStorage = new HashMap<>();
	}

	public User getUser(String emailId) {
		return userStorage.get(emailId);
	}
	
	public Collection<User> getAllUser(){
		return userStorage.values();
	}
	
	public void createUser(User user) {
		logger.info("created - "+user.toString());
		userStorage.put(user.getEmailId(), user);
	}
	
	public void saveUser(User user) {
		User usr = userStorage.get(user.getEmailId());
		usr.setFirstName(user.getFirstName());
		usr.setLastName(user.getLastName());
	}
	
	public void deleteUser(String emailId) throws Exception {
		if(userStorage.get(emailId) == null) {
			throw new Exception("Invalid User");
		}
		userStorage.remove(emailId);
	}
	
}
