package com.vbaixinho.learningspringboot.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.vbaixinho.learningspringboot.model.User;
import com.vbaixinho.learningspringboot.model.User.Gender;

public class FakeDataDaoTest {

	private FakeDataDao fakeDataDao;

	@Before
	public void setUp() throws Exception {
		fakeDataDao = new FakeDataDao();
	}

	@Test
	public void testSelectAllUsers() {
		List<User> users = fakeDataDao.selectAllUsers();
		assertThat(users).hasSize(1);
		User user = users.get(0);
		assertThat(user.getAge()).isEqualTo(22);
		assertThat(user.getFirstName()).isEqualTo("joe");
		assertThat(user.getLastName()).isEqualTo("jones");
		assertThat(user.getGender()).isEqualTo(User.Gender.MALE);
		assertThat(user.getEmail()).isEqualTo("joe.jones@gmail.com");
		assertThat(user.getUserUid()).isNotNull();
	}

	@Test
	public void testSelectUserByUserUid() {
		UUID randomUUID = UUID.randomUUID();
		User user = new User(randomUUID, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");
		fakeDataDao.insertUser(randomUUID, user);
		assertThat(fakeDataDao.selectAllUsers()).hasSize(2);
		Optional<User> fakeUser = fakeDataDao.selectUserByUserUid(randomUUID);
		assertThat(fakeUser.isPresent()).isTrue();
		assertThat(fakeUser.get()).isEqualToComparingFieldByField(user);
	}

	@Test
	public void testNotSelectUserByRandomUserUid() {
		Optional<User> fakeUser = fakeDataDao.selectUserByUserUid(UUID.randomUUID());
		assertThat(fakeUser.isPresent()).isFalse();
	}

	@Test
	public void testUpdateUser() {
		UUID joeUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
		User newJoe = new User(joeUid, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");

		fakeDataDao.updateUser(newJoe);

		Optional<User> updatedUser = fakeDataDao.selectUserByUserUid(joeUid);
		assertThat(updatedUser.isPresent()).isTrue();

		assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
		assertThat(updatedUser.get()).isEqualToComparingFieldByField(newJoe);

	}

	@Test
	public void testDeleteUserByUserUid() {
		UUID joeUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
		
		fakeDataDao.deleteUserByUserUid(joeUid);
		
		assertThat(fakeDataDao.selectUserByUserUid(joeUid).isPresent()).isFalse();
		assertThat(fakeDataDao.selectAllUsers()).isEmpty();
	}

	@Test
	public void testInsertUSer() {
		UUID randomUUID = UUID.randomUUID();
		User newJoe = new User(randomUUID, "anna", "montana", Gender.FEMALE, 30, "anna@gmail.com");
	}

}
