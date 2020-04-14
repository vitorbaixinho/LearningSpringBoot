package com.vbaixinho.learningspringboot.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import jersey.repackaged.com.google.common.collect.ImmutableList;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vbaixinho.learningspringboot.dao.FakeDataDao;
import com.vbaixinho.learningspringboot.model.User;
import com.vbaixinho.learningspringboot.model.User.Gender;

public class UserServiceTest {

	@Mock
	private FakeDataDao fakeDataDao;

	private UserService userService;

	@Before
	public void setUpBeforeClass() throws Exception {
		MockitoAnnotations.initMocks(this);
		userService = new UserService(fakeDataDao);
	}

	@Test
	public void testGetAllUsers() {
		UUID randomUUID = UUID.randomUUID();
		User anna = new User(randomUUID, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");

		ImmutableList<User> users = new ImmutableList.Builder<User>().add(anna).build();

		given(fakeDataDao.selectAllUsers()).willReturn(users);

		List<User> allUsers = userService.getAllUsers(Optional.empty());
		assertThat(allUsers).hasSize(1);

		User user = users.get(0);

		assertAnnaUserFields(user);
	}
	
	@Test
	public void testGetAllUsersByGender() {
		UUID annaUUID = UUID.randomUUID();
		User anna = new User(annaUUID, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");
		
		UUID joeUUID = UUID.randomUUID();
		User joe = new User(joeUUID, "joe", "santana", Gender.MALE, 40, "joe@gmail.com");

		ImmutableList<User> users = new ImmutableList.Builder<User>()
				.add(anna)
				.add(joe)
				.build();

		given(fakeDataDao.selectAllUsers()).willReturn(users);

		List<User> filteredUsers = userService.getAllUsers(Optional.of("MALE"));
		assertThat(filteredUsers).hasSize(1);

		User user = users.get(0);

		assertAnnaUserFields(user);
	}
	
	@Test
	public void testThrowExceptionWhenGetAllUsersByGenderIsInvalid() {
		assertThatThrownBy(() -> userService.getAllUsers(Optional.of("teste")))
		.isInstanceOf(IllegalStateException.class)
		.hasMessage("Invalid gender");
	}

	@Test
	public void testGetUser() {
		UUID randomUUID = UUID.randomUUID();
		User anna = new User(randomUUID, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");

		given(fakeDataDao.selectUserByUserUid(randomUUID)).willReturn(Optional.of(anna));

		Optional<User> userOptional = userService.getUser(randomUUID);
		assertThat(userOptional.isPresent()).isTrue();

		User user = userOptional.get();

		assertAnnaUserFields(user);
	}

	@Test
	public void testUpdateUser() {
		UUID randomUUID = UUID.randomUUID();
		User anna = new User(randomUUID, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");

		given(fakeDataDao.selectUserByUserUid(randomUUID)).willReturn(Optional.of(anna));
		given(fakeDataDao.updateUser(anna)).willReturn(1);

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

		int result = this.userService.updateUser(anna);

		verify(fakeDataDao).selectUserByUserUid(randomUUID);
		verify(fakeDataDao).updateUser(captor.capture());

		User user = captor.getValue();
		assertAnnaUserFields(user);

		assertThat(result).isEqualTo(1);

	}

	@Test
	public void testRemoveUser() {
		UUID randomUUID = UUID.randomUUID();
		User anna = new User(randomUUID, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");

		given(fakeDataDao.selectUserByUserUid(randomUUID)).willReturn(Optional.of(anna));
		given(fakeDataDao.deleteUserByUserUid(randomUUID)).willReturn(1);
		
		int result = this.userService.removeUser(randomUUID);

		verify(fakeDataDao).selectUserByUserUid(randomUUID);
		verify(fakeDataDao).deleteUserByUserUid(randomUUID);
		
		assertThat(result).isEqualTo(1);
	}

	@Test
	public void testInsertUSer() {
		UUID randomUUID = UUID.randomUUID();
		User anna = new User(randomUUID, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");
		given(fakeDataDao.insertUser(any(UUID.class), any(User.class))).willReturn(1);

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		
		int result = this.userService.insertUser(anna);
		
		verify(fakeDataDao).insertUser(eq(randomUUID), captor.capture());
		
		User user = captor.getValue();
		
		assertAnnaUserFields(user);
		assertThat(result).isEqualTo(1);
		
	}

	private void assertAnnaUserFields(User user) {
		assertThat(user.getAge()).isEqualTo(30);
		assertThat(user.getFirstName()).isEqualTo("anna");
		assertThat(user.getLastName()).isEqualTo("montana");
		assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
		assertThat(user.getEmail()).isEqualTo("anna@gmail.com");
		assertThat(user.getUserUid()).isNotNull();
		assertThat(user.getUserUid()).isInstanceOf(UUID.class);
	}

}
