package com.assignment.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.entity.User;
import com.assignment.service.UserService;
import com.assignment.util.ParameterValidationUtil;
import com.assignment.util.ResponseMapUtil;
import com.assignment.util.Utils;
import com.assignment.vo.UserVO;


@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@ResponseBody
	@RequestMapping("")
	public Map<String,Object> getAllUsers(){
		List<User> userList = userService.getAllUser();
		if(userList == null || userList.size() == 0) {
			return ResponseMapUtil.getNoUserAvailableError();
		}
		return ResponseMapUtil.getSuccessResult(Utils.getUserVOList(userList));
	}  

	@ResponseBody
	@RequestMapping("/getByEmail/{email}")
	public Map<String,Object> getUser(@PathVariable("email") String email){
		if(!ParameterValidationUtil.validateEmail(email)) {
			return ResponseMapUtil.getFailureResult("Invalid Email - "+email);
		}
		User usr = userService.getUserByEmail(email);
		if(usr == null) {
			return ResponseMapUtil.getUserNotAvailableError();
		}
		return ResponseMapUtil.getSuccessResult(new UserVO(usr.getEmail(),usr.getFirstName(),usr.getLastName()));
	} 


	  @ResponseBody
	  @RequestMapping(value = "/create", method = RequestMethod.POST)
	  public Map<String,Object> createUser(@RequestBody User user){
		List<String> validationErrors = ParameterValidationUtil.validateUser(user);
		if(validationErrors != null) {
			return ResponseMapUtil.getFailureResult(validationErrors);
		}
		try {
			userService.createUser(user);
		} catch (Exception e) {
			logger.error("Error in creating user", e);
			return ResponseMapUtil.getFailureResult(e.getMessage());
		}
		return ResponseMapUtil.getSuccessResult();
	  }
	 
	  @ResponseBody
	  @RequestMapping(value = "/update/{email}", method = RequestMethod.PUT)
	  public Map<String,Object> updateUser(@RequestBody User user, @PathVariable("email") String email){
		  logger.info("i"+user.toString());
		  if(!ParameterValidationUtil.validateEmail(email)) {
				return ResponseMapUtil.getFailureResult("Invalid Email - "+email);
		  }
		  List<String> validationErrors = ParameterValidationUtil.validateUserForUpdate(user);
		  logger.info("2"+user.toString());
		  logger.info("i"+validationErrors);
		  if(validationErrors != null) {
				return ResponseMapUtil.getFailureResult(validationErrors);
			}
			try {
				userService.updateUser(user, email);
			} catch (Exception e) {
				logger.error("Error in updating user", e);
				return ResponseMapUtil.getFailureResult(e.getMessage());
			}
			return ResponseMapUtil.getSuccessResult();
	    
	  }
	  
	 
	  @ResponseBody
	  @RequestMapping(value = "/delete/{emailId}", method = RequestMethod.DELETE)
	  public Map<String,Object> deleteUser(@PathVariable("emailId") String email){
		  if(!ParameterValidationUtil.validateEmail(email)) {
				return ResponseMapUtil.getFailureResult("Invalid Email - "+email);
		  }
		  try {
			userService.deleteUser(email);
		} catch (Exception e) {
			logger.error("Error in deleting user", e);
			return ResponseMapUtil.getFailureResult(e.getMessage());
		}
		  return ResponseMapUtil.getSuccessResult();
	  }
	  
	  @ResponseBody
	  @RequestMapping(value = "/changePassword/{email}", method = RequestMethod.POST)
	  public Map<String,Object> changePassword(@PathVariable("email") String email,
			  @RequestParam("oldPassword")  String oldPassword,
			  @RequestParam("newPassword") String newPassword){
		  
		  List<String> validationErrors = new ArrayList<>();
		  if(email == null) {
			  validationErrors.add("email cannot be null");
		  }
		  if(oldPassword == null){
			  validationErrors.add("old password cannot be null");
		  }
		  if(newPassword == null) {
			  validationErrors.add("new password cannot be null");
		  }
		  if(validationErrors.size() > 0) {
			  return ResponseMapUtil.getFailureResult(validationErrors);
		  }
		  if(!ParameterValidationUtil.validateEmail(email)) {
				return ResponseMapUtil.getFailureResult("Invalid Email - "+email);
		  }
		  
		try {
			userService.changePassword(email, oldPassword, newPassword);
		} catch (Exception e) {
			logger.error("Error in changing password", e);
			return ResponseMapUtil.getFailureResult(e.getMessage());
		}
		return ResponseMapUtil.getSuccessResult();
	  }
	  
	  
	  @ResponseBody
	  @RequestMapping(value = "/login", method = RequestMethod.POST)
	  public Map<String,Object> login(@RequestParam("email") String email,
			  @RequestParam("password")  String password){
		  
		  List<String> validationErrors = new ArrayList<>();
		  if(email == null) {
			  validationErrors.add("email cannot be null");
		  }
		  if(password == null){
			  validationErrors.add("password cannot be null");
		  }
		  if(validationErrors.size() > 0) {
			  return ResponseMapUtil.getFailureResult(validationErrors);
		  }
		  if(!ParameterValidationUtil.validateEmail(email)) {
				return ResponseMapUtil.getFailureResult("Invalid Email - "+email);
		  }
		String loginMsg = null;  
		try {
			loginMsg = userService.login(email, password);
		} catch (Exception e) {
			logger.error("Error in login", e);
			return ResponseMapUtil.getFailureResult(e.getMessage());
		}
		return ResponseMapUtil.getSuccessResult(loginMsg);
	  }
	  
}