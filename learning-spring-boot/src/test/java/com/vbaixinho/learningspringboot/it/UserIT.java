package com.vbaixinho.learningspringboot.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.vbaixinho.learningspringboot.clientproxy.UserResourceV1;
import com.vbaixinho.learningspringboot.model.User;
import com.vbaixinho.learningspringboot.model.User.Gender;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class UserIT {

	@Autowired
	private UserResourceV1 userResourceV1;

	@Test
	public void shouldInsertUser() throws Exception {

		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "joe", "jones", Gender.MALE, 22, "joe.jones@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		// Then
		User joe = userResourceV1.fetchUser(userUid);
		assertThat(joe).isEqualToComparingFieldByField(user);
	}

	@Test
	public void shouldDeleteUser() throws Exception {

		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "joe", "jones", Gender.MALE, 22, "joe.jones@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		// Then
		User joe = userResourceV1.fetchUser(userUid);
		assertThat(joe).isEqualToComparingFieldByField(user);

		// When
		userResourceV1.deleteUser(userUid);

		// Then
		assertThatThrownBy(() -> userResourceV1.fetchUser(userUid)).isInstanceOf(NotFoundException.class);
	}

	@Test
	public void shouldUpdateUser() throws Exception {

		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "joe", "jones", Gender.MALE, 22, "joe.jones@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		// Then
		User joe = userResourceV1.fetchUser(userUid);
		assertThat(joe).isEqualToComparingFieldByField(user);

		// Given
		User newUser = new User(userUid, "alex", "jones", Gender.MALE, 33, "alex.jones@gmail.com");

		// When
		userResourceV1.updateUser(newUser);

		// Then
		User alex = userResourceV1.fetchUser(userUid);
		assertThat(alex).isEqualToComparingFieldByField(newUser);

	}

	@Test
	public void shouldFetchUsersByGender() throws Exception {

		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "joe", "jones", Gender.MALE, 22, "joe.jones@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		List<User> females = userResourceV1.fetchUsers(Gender.FEMALE.name());

		assertThat(females).extracting("userUid").doesNotContain(user.getUserUid());
		assertThat(females).extracting("firstName").doesNotContain(user.getFirstName());
		assertThat(females).extracting("lastName").doesNotContain(user.getLastName());
		assertThat(females).extracting("gender").doesNotContain(user.getGender());
		assertThat(females).extracting("age").doesNotContain(user.getAge());
		assertThat(females).extracting("email").doesNotContain(user.getEmail());

		List<User> males = userResourceV1.fetchUsers(Gender.MALE.name());

		assertThat(males).extracting("userUid").contains(user.getUserUid());
		assertThat(males).extracting("firstName").contains(user.getFirstName());
		assertThat(males).extracting("lastName").contains(user.getLastName());
		assertThat(males).extracting("gender").contains(user.getGender());
		assertThat(males).extracting("age").contains(user.getAge());
		assertThat(males).extracting("email").contains(user.getEmail());

	}

}
