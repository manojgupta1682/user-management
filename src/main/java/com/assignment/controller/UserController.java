package com.assignment.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.entity.User;
import com.assignment.service.UserService;
import com.assignment.util.ParameterValidationUtil;
import com.assignment.util.ResponseMapUtil;
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
		List<UserVO> userList = userService.getAllUser();
		if(userList == null || userList.size() == 0) {
			return ResponseMapUtil.getNoUserAvailableError();
		}
		return ResponseMapUtil.getSuccessResult(userList);
	}  

	@ResponseBody
	@RequestMapping("/getByEmail/{email}")
	public Map<String,Object> getUser(@PathVariable("email") String email){
		if(!ParameterValidationUtil.validateEmail(email)) {
			return ResponseMapUtil.getFailureResult("Invalid Email - "+email);
		}
		UserVO usr = userService.getUserByEmail(email);
		if(usr == null) {
			return ResponseMapUtil.getUserNotAvailableError();
		}
		return ResponseMapUtil.getSuccessResult(usr);
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
			return ResponseMapUtil.getFailureResult(e.getMessage());
		}
		  return ResponseMapUtil.getSuccessResult();
	  }
}