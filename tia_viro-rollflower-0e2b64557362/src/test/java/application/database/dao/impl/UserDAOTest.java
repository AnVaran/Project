package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.UserDAO;
import application.model.Role;
import application.model.User;

public class UserDAOTest {
	private static final Logger LOGGER = Logger.getLogger(UserDAOTest.class);
	private static Connection connection;
	private static PreparedStatement pStatement;
	private static AbstractDAOImpl<User> userDAO;
	private User user;
	private static Role role;

	@BeforeClass
	public static void initSetUp() throws Exception {
		userDAO = new UserDAOImpl();
		connection = DatabasePoolConnection.getInstance().getConnection();

		role = new Role();
		role.setId((long) 20000);
		role.setRoleName("TEST");
		pStatement = connection.prepareStatement("INSERT INTO public.user_roles (id ,role_name) VALUES (?, ?);");
		pStatement.setLong(1, role.getId());
		pStatement.setString(2, role.getRoleName());
		pStatement.execute();
		if (pStatement != null)
			try {
				pStatement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@AfterClass
	public static void shutDown() throws Exception {
		pStatement = connection.prepareStatement("DELETE FROM public.user_roles WHERE id = ?;");
		pStatement.setLong(1, role.getId());
		pStatement.execute();
		if (pStatement != null)
			try {
				pStatement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setId((long) 10000);
		user.setUsername("user");
		user.setEmail("email");
		user.setPassword("password");
		user.setUserRole(role);
		pStatement = connection.prepareStatement(
				"INSERT INTO public.flower_user (id, role_id, user_name, user_email, user_password) VALUES (?, ?, ?, ?, ?);");
		pStatement.setLong(1, user.getId());
		pStatement.setLong(2, user.getUserRole().getId());
		pStatement.setString(3, user.getUsername());
		pStatement.setString(4, user.getEmail());
		pStatement.setString(5, user.getPassword());
		pStatement.execute();
		if (pStatement != null)
			try {
				pStatement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@After
	public void tearDown() throws Exception {
		pStatement = connection.prepareStatement("DELETE FROM public.flower_user WHERE id = ?;");
		pStatement.setLong(1, user.getId());
		pStatement.execute();
		if (pStatement != null)
			try {
				pStatement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Test
	public void testGetAll() {
		List<User> resultList = userDAO.getAll();
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testGetByPK() {
		User resultUser = userDAO.getByPK(user.getId());
		Assert.assertTrue(resultUser.equals(user));
		Assert.assertTrue(resultUser.getUsername().equals(user.getUsername()));
		Assert.assertTrue(resultUser.getUserRole().equals(user.getUserRole()));
	}

	@Test
	public void testCreate() {
		User userToAdd = new User();
		userToAdd.setUsername("user add");
		userToAdd.setEmail("email add");
		userToAdd.setPassword("password add");
		userToAdd.setUserRole(role);
		Long id = userDAO.create(userToAdd);
		User resultUser = userDAO.getByPK(id);
		userDAO.delete(id);
		Assert.assertTrue(resultUser.equals(userToAdd));
		Assert.assertTrue(resultUser.getUsername().equals(userToAdd.getUsername()));
		Assert.assertTrue(resultUser.getUserRole().equals(userToAdd.getUserRole()));
	}

	@Test
	public void testUpdate() {
		user.setUsername("user upd");
		user.setPassword("password upd");
		user.setEmail("email upd");
		userDAO.update(user);
		User resultUser = userDAO.getByPK(user.getId());
		Assert.assertTrue(resultUser.equals(user));
		Assert.assertTrue(resultUser.getUsername().equals(user.getUsername()));
		Assert.assertTrue(resultUser.getUserRole().equals(user.getUserRole()));
	}

	@Test
	public void testDelete() {
		User userToDlt = new User();
		userToDlt.setUsername("user dlt");
		userToDlt.setEmail("email dlt");
		userToDlt.setPassword("password dlt");
		userToDlt.setUserRole(role);
		Long id = userDAO.create(userToDlt);
		userDAO.delete(id);
		Assert.assertTrue(userDAO.getByPK(id) == null);
	}

	@Test
	public void testExists() {
		Assert.assertTrue(((UserDAO) userDAO).userExists(user));
	}

	@Test
	public void testGetUserByEmail() {
		User resultUser = ((UserDAO) userDAO).getUserByEmail(user.getEmail());
		Assert.assertTrue(resultUser.equals(user));
		Assert.assertTrue(resultUser.getUsername().equals(user.getUsername()));
		Assert.assertTrue(resultUser.getUserRole().equals(user.getUserRole()));
		Assert.assertNull(resultUser.getSessionKey());
	}

	@Test
	public void testGetUserBySessionKey() {
		user.setSessionKey("TESTSESSIONKEY");
		((UserDAO) userDAO).updateUserSessionKey(user);
		User resultUser = ((UserDAO) userDAO).getUserBySessionKey(user.getSessionKey());
		Assert.assertTrue(resultUser.equals(user));
		Assert.assertTrue(resultUser.getUsername().equals(user.getUsername()));
		Assert.assertTrue(resultUser.getUserRole().equals(user.getUserRole()));
		Assert.assertTrue(resultUser.getSessionKey().equals(user.getSessionKey()));
	}

	@Test
	public void testUpdateUserSessionKey() {
		user.setSessionKey("TSETSESSIONKEY");
		((UserDAO) userDAO).updateUserSessionKey(user);
		User resultUser = userDAO.getByPK(user.getId());
		Assert.assertTrue(resultUser.equals(user));
		Assert.assertTrue(resultUser.getUsername().equals(user.getUsername()));
		Assert.assertTrue(resultUser.getUserRole().equals(user.getUserRole()));
		Assert.assertTrue(resultUser.getSessionKey().equals(user.getSessionKey()));
	}
}
