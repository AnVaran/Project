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
import application.database.dao.DecorationDAO;
import application.enums.MaterialEnum;
import application.model.Decoration;

public class DecorationDAOTest {
	private static final Logger LOGGER = Logger.getLogger(DecorationDAOTest.class);
	private static Connection connection;
	private PreparedStatement pStatement;
	private static AbstractDAOImpl<Decoration> decorationDAO;
	private Decoration decoration = new Decoration();

	@BeforeClass
	public static void initSetUp() throws Exception {
		decorationDAO = new DecorationDAOimpl();
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
		decoration.setId((long) 10000);
		decoration.setName("deco");
		decoration.setMaterial(MaterialEnum.WOODEN);
		decoration.setPrice((float) 10.00);
		decoration.setDescription("descr");

		pStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_decoration (id, decoration_name, decoration_material, decoration_price, decoration_description) VALUES (?, ?, ?, ?, ?);");
		pStatement.setLong(1, decoration.getId());
		pStatement.setString(2, decoration.getName());
		pStatement.setString(3, decoration.getMaterial().toString());
		pStatement.setFloat(4, decoration.getPrice());
		if (decoration.getDescription() == null || decoration.getDescription().length() == 0) {
			pStatement.setNull(5, Types.VARCHAR);
		} else {
			pStatement.setString(5, decoration.getDescription());
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
		pStatement = connection.prepareStatement("DELETE FROM public.bouquet_decoration WHERE id = ?;");
		pStatement.setLong(1, decoration.getId());
		pStatement.execute();
		if (pStatement != null)
			try {
				pStatement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Test
	public void testAllDecorations() {
		List<Decoration> resultList = decorationDAO.getAll();
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testGetDecorationById() {
		Decoration resultDecoration = decorationDAO.getByPK(decoration.getId());
		Assert.assertTrue(decoration.equals(resultDecoration));
		Assert.assertTrue(decoration.getDescription().equals(resultDecoration.getDescription()));
		Assert.assertTrue(decoration.getPrice().equals(resultDecoration.getPrice()));
	}

	@Test
	public void testAddDecoration() {
		Decoration decorationToAdd = new Decoration();
		decorationToAdd.setName("deco add");
		decorationToAdd.setMaterial(MaterialEnum.WOODEN);
		decorationToAdd.setDescription("descr add");
		decorationToAdd.setPrice((float) 10.00);
		Long id = decorationDAO.create(decorationToAdd);
		Decoration resultDecoration = decorationDAO.getByPK(id);
		decorationDAO.delete(id);
		Assert.assertTrue(decorationToAdd.equals(resultDecoration));
		Assert.assertTrue(decorationToAdd.getDescription().equals(resultDecoration.getDescription()));
		Assert.assertTrue(decorationToAdd.getPrice().equals(resultDecoration.getPrice()));
	}

	@Test
	public void testUpdateDecoration() {
		decoration.setDescription("descr upd");
		decoration.setPrice((float) 15.00);
		decorationDAO.update(decoration);
		Decoration resultDecoration = decorationDAO.getByPK(decoration.getId());
		Assert.assertTrue(decoration.equals(resultDecoration));
		Assert.assertTrue(decoration.getDescription().equals(resultDecoration.getDescription()));
		Assert.assertTrue(decoration.getPrice().equals(resultDecoration.getPrice()));
	}

	@Test
	public void testDeleteDecoration() {
		Decoration decorationToDlt = new Decoration();
		decorationToDlt.setName(" dlt deco");
		decorationToDlt.setMaterial(MaterialEnum.WOODEN);
		decorationToDlt.setDescription("dlt descr");
		decorationToDlt.setPrice((float) 10.00);
		Long id = decorationDAO.create(decorationToDlt);
		decorationDAO.delete(id);
		Decoration resultDecoration = decorationDAO.getByPK(id);
		Assert.assertTrue(resultDecoration == null);
	}

	@Test
	public void testDecorationExists() {
		Assert.assertTrue(((DecorationDAO) decorationDAO).decorationExists(decoration));
	}
}
