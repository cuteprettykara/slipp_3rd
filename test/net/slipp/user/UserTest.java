package net.slipp.user;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class UserTest {
	public static User TEST_USER = new User("prettykara", "1111", "sangbum", "prettykara@gmail.com");
	private UserDAO userDao;
	
	@Before
	public void setup() throws SQLException {
		userDao = new UserDAO();
		userDao.removeUser(TEST_USER.getUserId());
	}

	@Test
	public void matchPassword() {
		
		assertTrue(TEST_USER.matchPassword("1111"));
	}

	@Test
	public void notMatchPassword() {
//		User user = new User("prettykara", "1111", "sangbum", "prettykara@gmail.com");
		
		assertFalse(TEST_USER.matchPassword("222"));
	}
	
	@Test
	public void login() throws Exception {
		userDao.addUser(TEST_USER);
		
		assertTrue(User.login(TEST_USER.getUserId(), TEST_USER.getPassword()));
	}
	
	@Test(expected = UserNotFoundException.class)
	public void loginWhenUserNotExists() throws Exception {
		assertFalse(User.login("userid2", TEST_USER.getPassword()));
	}

	@Test(expected = PasswordMismatchException.class)
	public void loginWhenPasswordMismatch() throws Exception {
		userDao.addUser(TEST_USER);
		
		assertFalse(User.login(TEST_USER.getUserId(), "3333"));
	}
}
