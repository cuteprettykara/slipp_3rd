package net.slipp.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAOTest {
	private static final Logger log = LoggerFactory.getLogger(UserDAOTest.class);

	private UserDAO userDao;
	
	@Before
	public void setup() {
		userDao = new UserDAO();
	}

	@Test
	public void crud() throws Exception {
		User user = UserTest.TEST_USER;
		userDao.removeUser(user.getUserId());
		userDao.addUser(user);
		
		User actual = userDao.findById(user.getUserId());
		assertEquals(user, actual);
		
		User updateUser = new User(user.getUserId(), "java", "java", "java@gmail.com");
		userDao.update(updateUser);
		
		actual = userDao.findById(updateUser.getUserId());
		
		assertEquals(updateUser, actual);
	}
	
	@Test
	public void 존재하지_않는_사용자_조회() throws Exception {
		User user = UserTest.TEST_USER;
		userDao.removeUser(user.getUserId());
		
		User actual = userDao.findById(user.getUserId());
		assertNull(actual);
	}
	
	@Test
	public void findUsers() throws Exception {
		List<User> users = userDao.findUsers();
		assertTrue(users.size() > 0);
		log.debug("Users : {}", users);
	}
}
