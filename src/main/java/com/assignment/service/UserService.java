package com.assignment.service;

import java.util.List;

import com.assignment.entity.User;
import com.assignment.vo.UserVO;

public interface UserService {
	
	public UserVO getUserByEmail(String email);
	
	public List<UserVO> getAllUser();
	
	public void deleteUser(String email) throws Exception;;
	
	public void createUser(User user) throws Exception;
	
	public void updateUser(User user, String oldEmail) throws Exception;
	
	public void changePassword(String email, String oldPassword, String newPassword) throws Exception;
	
	public String login(String email, String password) throws Exception;
}
