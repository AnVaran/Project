package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.WrapperDAO;
import application.enums.MaterialEnum;
import application.model.Wrapper;

public class WrapperDAOTest {
	private static final Logger LOGGER = Logger.getLogger(WrapperDAOTest.class);
	private static Connection connection;
	private PreparedStatement pStatement;
	private static AbstractDAOImpl<Wrapper> wrapperDAO;
	private Wrapper wrapper = new Wrapper();

	@BeforeClass
	public static void initSetUp() throws Exception {
		wrapperDAO = new WrapperDAOimpl();
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
		wrapper.setId((long) 10000);
		wrapper.setName("wrap");
		wrapper.setMaterial(MaterialEnum.PAPER);
		wrapper.setPrice((float) 10.00);
		wrapper.setDescription("descr");

		pStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_wrapper (id, wrapper_name, wrapper_material, wrapper_price, wrapper_description) VALUES (?, ?, ?, ?, ?)");
		pStatement.setLong(1, wrapper.getId());
		pStatement.setString(2, wrapper.getName());
		pStatement.setString(3, wrapper.getMaterial().toString());
		pStatement.setFloat(4, wrapper.getPrice());
		if (wrapper.getDescription() == null || wrapper.getDescription().length() == 0) {
			pStatement.setNull(5, Types.VARCHAR);
		} else {
			pStatement.setString(5, wrapper.getDescription());
		}
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
		pStatement = connection.prepareStatement("DELETE FROM public.bouquet_wrapper WHERE id = ?;");
		pStatement.setLong(1, wrapper.getId());
		pStatement.execute();
		if (pStatement != null)
			try {
				pStatement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Test
	public void testAllWrappers() {
		List<Wrapper> resultList = wrapperDAO.getAll();
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testGetWrapperById() {
		Wrapper resultWrapper = wrapperDAO.getByPK(wrapper.getId());
		Assert.assertTrue(wrapper.equals(resultWrapper));
		Assert.assertTrue(wrapper.getDescription().equals(resultWrapper.getDescription()));
		Assert.assertTrue(wrapper.getPrice().equals(resultWrapper.getPrice()));
	}

	@Test
	public void testAddWrapper() {
		Wrapper wrapperToAdd = new Wrapper();
		wrapperToAdd.setName("add wrap");
		wrapperToAdd.setMaterial(MaterialEnum.PAPER);
		wrapperToAdd.setDescription("add descr");
		wrapperToAdd.setPrice((float) 10.00);
		Long id = wrapperDAO.create(wrapperToAdd);
		Wrapper resultWrapper = wrapperDAO.getByPK(id);
		wrapperDAO.delete(id);
		Assert.assertTrue(wrapperToAdd.equals(resultWrapper));
		Assert.assertTrue(wrapperToAdd.getDescription().equals(wrapperToAdd.getDescription()));
		Assert.assertTrue(wrapperToAdd.getPrice().equals(wrapperToAdd.getPrice()));
	}

	@Test
	public void testUpdateWrapper() {
		wrapper.setDescription("upd descr");
		wrapper.setPrice((float) 15.00);
		wrapperDAO.update(wrapper);
		Wrapper resultWrapper = wrapperDAO.getByPK(wrapper.getId());
		Assert.assertTrue(wrapper.equals(resultWrapper));
		Assert.assertTrue(wrapper.getDescription().equals(wrapper.getDescription()));
		Assert.assertTrue(wrapper.getPrice().equals(wrapper.getPrice()));
	}

	@Test
	public void testDeleteWrapper() {
		Wrapper wrapperToDlt = new Wrapper();
		wrapperToDlt.setName("dlt wrap");
		wrapperToDlt.setMaterial(MaterialEnum.PAPER);
		wrapperToDlt.setDescription("dlt wrap");
		wrapperToDlt.setPrice((float) 10.00);
		Long id = wrapperDAO.create(wrapperToDlt);
		wrapperDAO.delete(id);
		Assert.assertTrue(!((WrapperDAO) wrapperDAO).wrapperExists(wrapperToDlt));
		Assert.assertTrue(wrapperDAO.getByPK(id) == null);
	}

	@Test
	public void testWrapperExists() {
		Assert.assertTrue(((WrapperDAO) wrapperDAO).wrapperExists(wrapper));
	}
}
