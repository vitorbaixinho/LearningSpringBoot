package com.vbaixinho.learningspringboot.resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vbaixinho.learningspringboot.model.User;
import com.vbaixinho.learningspringboot.service.UserService;

//@RestController
//@RequestMapping(path = "/api/v1/users")
public class UserResourceSptingMVC {

	private UserService userService;

	@Autowired
	public UserResourceSptingMVC(UserService userService) {
		this.userService = userService;
	}
	
	//SPRING MVC STYLE
	
	//Package
	// - org.springframework.web.bind.annotation
	// - org.springframework.http

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> fetchUsers(@QueryParam("gender") String gender) {
		return userService.getAllUsers(Optional.ofNullable(gender));
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "{userUid}")
	public ResponseEntity<?> fetchUser(@PathVariable("userUid") UUID userUid) {

		return userService.getUser(userUid).<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity
				.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("User " + userUid + " was not found.")));
//		Optional<User> user = userService.getUser(userUid);
//		if (user.isPresent()) {
//			return ResponseEntity.ok(user.get());
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND)
//				.body(new ErrorMessage("User " + userUid + " was not found."));
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> insertNewUser(@RequestBody User user) {
		int result = userService.insertUser(user);
		return responseIntegerEntity(result);
	}

	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> updateUser(@RequestBody User user) {
		int result = userService.updateUser(user);
		return responseIntegerEntity(result);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "{userUid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> deleteUser(@PathVariable("userUid") UUID userUid) {
		int result = userService.removeUser(userUid);
		return responseIntegerEntity(result);
	}

	private ResponseEntity<Integer> responseIntegerEntity(int result) {
		if (result == 1) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}
}
