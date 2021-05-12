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
import application.database.dao.FlowerDAO;
import application.enums.ColorEnum;
import application.enums.FlowerLengthEnum;
import application.model.Flower;

public class FlowerDAOTest {
	private static final Logger LOGGER = Logger.getLogger(FlowerDAOTest.class);
	private static Connection connection;
	private PreparedStatement pStatement;
	private static AbstractDAOImpl<Flower> flowerDAO;
	private Flower flower = new Flower();

	@BeforeClass
	public static void initSetUp() throws Exception {
		flowerDAO = new FlowerDAOimpl();
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
		flower.setId((long) 10000);
		flower.setName("flower");
		flower.setColor(ColorEnum.RED);
		flower.setLength(FlowerLengthEnum.MEDIUM);
		flower.setPrice((float) 100.00);

		pStatement = connection.prepareStatement(
				"INSERT INTO public.flower (id, flower_name, flower_color, flower_length, flower_price) VALUES (?, ?, ?, ?, ?);");
		pStatement.setLong(1, flower.getId());
		pStatement.setString(2, flower.getName());
		pStatement.setString(3, flower.getColor().toString());
		pStatement.setString(4, flower.getLength().toString());
		pStatement.setFloat(5, flower.getPrice());
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
		pStatement = connection.prepareStatement("DELETE FROM public.flower WHERE id = ?;");
		pStatement.setLong(1, flower.getId());
		pStatement.execute();
		if (pStatement != null)
			try {
				pStatement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Test
	public void testAllFlowers() {
		List<Flower> resultList = flowerDAO.getAll();
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testGetFlowerById() {
		Flower resultFlower = flowerDAO.getByPK(flower.getId());
		Assert.assertTrue(resultFlower.equals(flower));
		Assert.assertTrue(flower.getPrice().equals(resultFlower.getPrice()));
	}

	@Test
	public void testAddFlower() {
		Flower flowerToAdd = new Flower();
		flowerToAdd.setName("flower add");
		flowerToAdd.setColor(ColorEnum.RED);
		flowerToAdd.setLength(FlowerLengthEnum.MEDIUM);
		flowerToAdd.setPrice((float) 100.00);
		long id = flowerDAO.create(flowerToAdd);
		Flower resultFlower = flowerDAO.getByPK(id);
		flowerDAO.delete(id);
		Assert.assertTrue(resultFlower.equals(flowerToAdd));
		Assert.assertTrue(flowerToAdd.getPrice().equals(resultFlower.getPrice()));
	}

	@Test
	public void testUpdateFlower() {
		flower.setPrice((float) 80.00);
		flowerDAO.update(flower);
		Flower resulFlower = flowerDAO.getByPK(flower.getId());
		Assert.assertTrue(flower.getPrice().equals(resulFlower.getPrice()));
		Assert.assertTrue(flower.equals(resulFlower));
	}

	@Test
	public void testDeleteFlower() {
		Flower flowerToDlt = new Flower();
		flowerToDlt.setName("flower dlt");
		flowerToDlt.setColor(ColorEnum.RED);
		flowerToDlt.setLength(FlowerLengthEnum.MEDIUM);
		flowerToDlt.setPrice((float) 100.00);
		long id = flowerDAO.create(flowerToDlt);
		flowerDAO.delete(id);
		Assert.assertTrue(!((FlowerDAO) flowerDAO).flowerExists(flowerToDlt));
		Assert.assertTrue(flowerDAO.getByPK(id) == null);
	}

	@Test
	public void testFlowerExists() {
		Assert.assertTrue(((FlowerDAO) flowerDAO).flowerExists(flower));
	}

}
