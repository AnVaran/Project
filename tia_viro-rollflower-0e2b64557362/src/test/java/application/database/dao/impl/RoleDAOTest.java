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
import application.database.dao.RoleDAO;
import application.model.Role;

public class RoleDAOTest {
	private static final Logger LOGGER = Logger.getLogger(RoleDAOTest.class);
	private static Connection connection;
	private PreparedStatement pStatement;
	private static AbstractDAOImpl<Role> roleDAO;
	private Role role = new Role();

	@BeforeClass
	public static void initSetUp() throws Exception {
		roleDAO = new RoleDAOImpl();
		connection = DatabasePoolConnection.getInstance().getConnection();
	}

	@AfterClass
	public static void shutDown() throws Exception {
		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Before
	public void setUp() throws Exception {
		role.setId((long) 10000);
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

	@After
	public void tearDown() throws Exception {
		pStatement = connection.prepareStatement("DELETE FROM public.user_roles WHERE id = ?;");
		pStatement.setLong(1, role.getId());
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
		List<Role> resultList = roleDAO.getAll();
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testGetByPK() {
		Role resultRole = roleDAO.getByPK(role.getId());
		Assert.assertTrue(resultRole.equals(role));
	}

	@Test
	public void testCreate() {
		Role roleToAdd = new Role();
		roleToAdd.setRoleName("TEST ADD");
		Long id = roleDAO.create(roleToAdd);
		roleToAdd.setId(id);
		Role resultRole = roleDAO.getByPK(id);
		roleDAO.delete(id);
		Assert.assertTrue(resultRole.equals(roleToAdd));
	}

	@Test
	public void testUpdate() {
		role.setRoleName("TEST UPDATE");
		roleDAO.update(role);
		Role resulRole = roleDAO.getByPK(role.getId());
		Assert.assertTrue(role.equals(resulRole));
	}

	@Test
	public void testDelete() {
		Role roleToDlt = new Role();
		roleToDlt.setRoleName("TEST DELETE");
		Long id = roleDAO.create(roleToDlt);
		roleDAO.delete(id);
		Assert.assertTrue(roleDAO.getByPK(id) == null);
	}

	@Test
	public void testExists() {
		Assert.assertTrue(((RoleDAO) roleDAO).roleExists(role));
	}

}
