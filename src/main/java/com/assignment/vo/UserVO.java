package com.assignment.vo;

public class UserVO {
	
	private final String emailId;
	private final String firstName;
	private final String lastName;
	
	public UserVO(String emailId, String firstName, String lastName) {
		super();
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	

}
