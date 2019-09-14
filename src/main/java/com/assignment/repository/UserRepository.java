package com.assignment.repository;

import java.util.Collection;

import com.assignment.entity.User;

public interface UserRepository {
	
	public void createUser(User user);

	public void saveUser(User user);
	
	public void deleteUser(String emailId) throws Exception;
	
	public User getUser(String emailId);
	
	public Collection<User> getAllUser();

}
