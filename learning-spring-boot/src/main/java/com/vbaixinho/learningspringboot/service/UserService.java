package com.vbaixinho.learningspringboot.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbaixinho.learningspringboot.dao.UserDao;
import com.vbaixinho.learningspringboot.model.User;
import com.vbaixinho.learningspringboot.model.User.Gender;

@Service
public class UserService {

	private UserDao userDao;

	@Autowired
	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	public List<User> getAllUsers(Optional<String> gender) {
		List<User> users = userDao.selectAllUsers();
		if (!gender.isPresent()) {
			return users;
		}
		try {
			Gender theGender = Gender.valueOf(gender.get().toUpperCase());
			return users.stream().filter(user -> user.getGender().equals(theGender)).collect(Collectors.toList());
		} catch (Exception e) {
			throw new IllegalStateException("Invalid gender", e);
		}
	}

	public Optional<User> getUser(UUID userUid) {
		return userDao.selectUserByUserUid(userUid);
	}

	public int updateUser(User user) {
		Optional<User> optionalUser = getUser(user.getUserUid());
		if(optionalUser.isPresent()) {
			return 	userDao.updateUser(user);
		}
		throw new NotFoundException("user " + user.getUserUid() + " not found");
	}

	public int removeUser(UUID uid) {
		UUID userUid = getUser(uid)
				.map(User::getUserUid)
				.orElseThrow(() -> new NotFoundException("User " + uid + " not found"));
		return userDao.deleteUserByUserUid(userUid);
	}

	public int insertUser(User user) {
		UUID randomUUID = user.getUserUid() == null ? UUID.randomUUID() : user.getUserUid();
//		validateUser(user);
		return userDao.insertUser(randomUUID, User.newUser(randomUUID, user));
	}

	private void validateUser(User user) {
		Objects.requireNonNull(user.getFirstName(), "first name required");
		Objects.requireNonNull(user.getLastName(), "last name required");
		Objects.requireNonNull(user.getAge(), "age required");
		Objects.requireNonNull(user.getEmail(), "email required");
		Objects.requireNonNull(user.getGender(), "gender required");
	}
}
