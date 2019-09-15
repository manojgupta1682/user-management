package com.assignment.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.entity.User;
import com.assignment.repository.UserRepository;
import com.assignment.util.AESCipher;
import com.assignment.vo.UserVO;


@Service
public class UserServiceImpl implements UserService{


	private final UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> getAllUser() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User createUser(User user) throws Exception {
		User existingUser = userRepository.findByEmail(user.getEmail());
		logger.info("existingUser - "+existingUser);
		if(existingUser != null) {
			throw new Exception("User exist, hence cannot create new User.");
		}
		try {
			logger.info("Service created inside- "+user.toString());
			user.setPassword(AESCipher.encrypt(user.getPassword()));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			logger.error("Exception in encrypting password" , e);
			throw new Exception("Exception in encrypting password",e);
		}
		logger.info("Service created - "+user.toString());
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user, String oldEmail) throws Exception {
		User usr = userRepository.findByEmail(oldEmail);
		if(usr == null) {
			throw new Exception(oldEmail+" - User does not Exist");
		}else if(usr.equals(user)) {
			if(user.getFirstName() != null) {
				usr.setFirstName(user.getFirstName());
			}if(user.getLastName() != null) {
				usr.setLastName(user.getLastName());
			}
			return userRepository.save(user);
		}else {
			if(user.getFirstName() == null) {
				user.setFirstName(usr.getFirstName());
			}if(user.getLastName() == null) {
				user.setLastName(usr.getLastName());
			}
			user.setPassword(usr.getPassword());
			userRepository.deleteById(usr.getEmail());
			return userRepository.save(user);
		}
	}

	@Override
	public void deleteUser(String email) throws Exception {
		User usr = userRepository.findByEmail(email);
		if(usr == null) {
			throw new Exception(email+" - User does not Exist");
		}
		userRepository.deleteById(email);
	}

	public void changePassword(String email, String oldPassword, String newPassword) throws Exception {
		User usr = userRepository.findByEmail(email);
		if(usr == null) {
			throw new Exception("User does not exist");
		}
		if(!oldPassword.equals(AESCipher.decrypt(usr.getPassword()))) {
			logger.error("old password="+AESCipher.decrypt(usr.getPassword()));
			throw new Exception("Old password is not same");
		}
		try {
			usr.setPassword(AESCipher.encrypt(newPassword));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new Exception("Exception in encrypting password");
		}
		userRepository.save(usr);
		
	}

	public String login(String email, String password) throws Exception {
		User usr = userRepository.findByEmail(email);
		if(usr == null) {
			throw new Exception("Invalid User");
		}
		if(!password.equals(AESCipher.decrypt(usr.getPassword()))) {
			return "Access denied";
		}
		
		return "Welcome "+usr.getFirstName()+" "+usr.getLastName()+"!";
	}
}
