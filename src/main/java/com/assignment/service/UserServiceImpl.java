package com.assignment.service;

import java.util.Collection;
import java.util.List;
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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AESCipher cipher;
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserVO getUserByEmail(String email) {
		UserVO usrVO = null;
		User usr = userRepository.getUser(email);
		if(usr != null) {
			usrVO = new UserVO(usr.getEmailId(), usr.getFirstName(), usr.getLastName());
		}
		return usrVO;
	}

	@Override
	public List<UserVO> getAllUser() {
		Collection<User> colls =  userRepository.getAllUser();
		return colls.stream()
				.map(u -> new UserVO(u.getEmailId(), u.getFirstName(), u.getLastName()))
				.collect(Collectors.toList());
	}

	@Override
	public void createUser(User user) throws Exception {
		try {
			logger.info("Service created inside- "+user.toString());
			user.setPassword(cipher.getEncryptedText(user.getPassword()));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			logger.error("Exception in encrypting password" , e);
			throw new Exception("Exception in encrypting password",e);
		}
		logger.info("Service created - "+user.toString());
		userRepository.createUser(user);
	}

	@Override
	public void updateUser(User user, String oldEmail) throws Exception {
		User usr = userRepository.getUser(oldEmail);
		if(usr == null) {
			throw new Exception(oldEmail+" - User does not Exist");
		}else if(usr.equals(user)) {
			if(user.getFirstName() != null) {
				usr.setFirstName(user.getFirstName());
			}if(user.getLastName() != null) {
				usr.setLastName(user.getLastName());
			}
			userRepository.saveUser(user);
		}else {
			if(user.getFirstName() == null) {
				user.setFirstName(usr.getFirstName());
			}if(user.getLastName() == null) {
				user.setLastName(usr.getLastName());
			}
			user.setPassword(usr.getPassword());
			userRepository.deleteUser(usr.getEmailId());
			userRepository.createUser(user);
		}
	}

	@Override
	public void deleteUser(String email) throws Exception {
		User usr = userRepository.getUser(email);
		if(usr == null) {
			throw new Exception(email+" - User does not Exist");
		}
		userRepository.deleteUser(email);
	}

	public void changePassword(String email, String oldPassword, String newPassword) throws Exception {
		User usr = userRepository.getUser(email);
		if(usr == null) {
			throw new Exception("User does not exist");
		}
		if(!oldPassword.equals(cipher.getDecryptedText(usr.getPassword()))) {
			throw new Exception("Old password is not same");
		}
		try {
			usr.setPassword(cipher.getEncryptedText(newPassword));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new Exception("Exception is encrypting password");
		}
	}

	public String login(String email, String password) throws Exception {
		User usr = userRepository.getUser(email);
		if(usr == null) {
			throw new Exception("Invalid User");
		}
		if(!password.equals(cipher.getDecryptedText(usr.getPassword()))) {
			throw new Exception("password doesn't match");
		}
		
		return "Welcome - "+usr.getFirstName()+" "+usr.getLastName();
	}
}
