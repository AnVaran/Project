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
import application.database.dao.FlowerBouquetDAO;
import application.enums.MaterialEnum;
import application.enums.OrderStatusEnum;
import application.model.BouquetOrder;
import application.model.Decoration;
import application.model.FlowerBouquet;
import application.model.Wrapper;

public class FlowerBouquetDAOTest {
	private static final Logger LOGGER = Logger.getLogger(FlowerBouquetDAOTest.class);
	private static Connection connection;
	private static PreparedStatement firstPrepStatement;
	private static PreparedStatement secondPrepStatement;
	private static PreparedStatement thirdPrepStatement;
	private PreparedStatement forthPrepStatement;
	private FlowerBouquet bouquet = new FlowerBouquet();
	private static BouquetOrder order;
	private static Wrapper wrapper;
	private static Decoration decoration;
	private static AbstractDAOImpl<FlowerBouquet> bouquetDAO;

	@BeforeClass
	public static void initSetUp() throws Exception {
		connection = DatabasePoolConnection.getInstance().getConnection();
		bouquetDAO = new FlowerBouquetDAOImpl();

		order = new BouquetOrder();
		order.setId((long) 20000);
		order.setCommentary(" ");
		order.setShippingAdres(" ");
		order.setOrderStatus(OrderStatusEnum.PENDING);

		wrapper = new Wrapper();
		wrapper.setId((long) 20000);
		wrapper.setName("wrap");
		wrapper.setMaterial(MaterialEnum.PAPER);
		wrapper.setPrice((float) 10.00);
		wrapper.setDescription(" ");

		decoration = new Decoration();
		decoration.setId((long) 20000);
		decoration.setName("deco");
		decoration.setMaterial(MaterialEnum.WOODEN);
		decoration.setPrice((float) 10.00);
		decoration.setDescription(" ");

		firstPrepStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_order (id, order_commentary, shipping_adress, order_status) VALUES (?, ?, ?, ?);");
		secondPrepStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_decoration (id, decoration_name, decoration_material, decoration_price, decoration_description) VALUES (?, ?, ?, ?, ?);");
		thirdPrepStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_wrapper (id, wrapper_name, wrapper_material, wrapper_price, wrapper_description) VALUES (?, ?, ?, ?, ?);");

		firstPrepStatement.setLong(1, order.getId());
		firstPrepStatement.setString(2, order.getCommentary());
		firstPrepStatement.setString(3, order.getShippingAdres());
		firstPrepStatement.setString(4, order.getOrderStatus().toString());

		secondPrepStatement.setLong(1, wrapper.getId());
		secondPrepStatement.setString(2, wrapper.getName());
		secondPrepStatement.setString(3, wrapper.getMaterial().toString());
		secondPrepStatement.setFloat(4, wrapper.getPrice());
		secondPrepStatement.setString(5, wrapper.getDescription());

		thirdPrepStatement.setLong(1, decoration.getId());
		thirdPrepStatement.setString(2, decoration.getName());
		thirdPrepStatement.setString(3, decoration.getMaterial().toString());
		thirdPrepStatement.setFloat(4, decoration.getPrice());
		thirdPrepStatement.setString(5, decoration.getDescription());

		firstPrepStatement.execute();
		secondPrepStatement.execute();
		thirdPrepStatement.execute();

		closeStatement(firstPrepStatement);
		closeStatement(secondPrepStatement);
		closeStatement(thirdPrepStatement);
	}

	@AfterClass
	public static void shutDown() throws Exception {
		firstPrepStatement = connection.prepareStatement("DELETE FROM public.bouquet_order WHERE id = ?;");
		secondPrepStatement = connection.prepareStatement("DELETE FROM public.bouquet_decoration WHERE id = ?;");
		thirdPrepStatement = connection.prepareStatement("DELETE FROM public.bouquet_wrapper WHERE id = ?;");

		firstPrepStatement.setLong(1, order.getId());
		secondPrepStatement.setLong(1, decoration.getId());
		thirdPrepStatement.setLong(1, wrapper.getId());

		firstPrepStatement.execute();
		secondPrepStatement.execute();
		thirdPrepStatement.execute();

		closeStatement(firstPrepStatement);
		closeStatement(secondPrepStatement);
		closeStatement(thirdPrepStatement);

		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Before
	public void setUp() throws Exception {
		bouquet.setId((long) 10000);
		bouquet.setWrapperComponent(wrapper);
		bouquet.setDecorationComponent(decoration);
		bouquet.setOrderId(order.getId());
		bouquet.setPrice((float) 100.00);

		forthPrepStatement = connection.prepareStatement(
				"INSERT INTO public.flower_bouquet (id, wrapper_id, decoration_id, order_id, bouquet_price) VALUES (?, ?, ?, ?, ?);");

		forthPrepStatement.setLong(1, bouquet.getId());
		forthPrepStatement.setLong(2, bouquet.getWrapperComponent().getId());
		forthPrepStatement.setLong(3, bouquet.getDecorationComponent().getId());
		forthPrepStatement.setLong(4, bouquet.getOrderId());
		forthPrepStatement.setFloat(5, bouquet.getPrice());

		forthPrepStatement.execute();

		closeStatement(forthPrepStatement);
	}

	@After
	public void tearDown() throws Exception {
		forthPrepStatement = connection.prepareStatement("DELETE FROM public.flower_bouquet WHERE id = ?;");
		forthPrepStatement.setLong(1, bouquet.getId());
		forthPrepStatement.execute();
		closeStatement(forthPrepStatement);
	}

	@Test
	public void testAllBouquets() {
		List<FlowerBouquet> resultList = bouquetDAO.getAll();
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testGetBouquetById() {
		FlowerBouquet resultBouquet = bouquetDAO.getByPK(bouquet.getId());
		Assert.assertTrue(resultBouquet.getId().equals(bouquet.getId()));
		Assert.assertTrue(resultBouquet.getOrderId().equals(order.getId()));
		Assert.assertTrue(resultBouquet.getWrapperComponent().getId().equals(wrapper.getId()));
		Assert.assertTrue(resultBouquet.getDecorationComponent().getId().equals(decoration.getId()));
		Assert.assertTrue(resultBouquet.getPrice().equals(bouquet.getPrice()));
	}

	@Test
	public void testGetBouquetsByOrderId() {
		List<FlowerBouquet> resultList = ((FlowerBouquetDAO) bouquetDAO).getBouquetsByOrderId(order.getId());
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testAddBouquet() {
		FlowerBouquet bouquetToAdd = new FlowerBouquet();
		bouquetToAdd.setDecorationComponent(decoration);
		bouquetToAdd.setWrapperComponent(wrapper);
		bouquetToAdd.setOrderId(order.getId());
		bouquetToAdd.setPrice((float) 150.00);
		long resultId = bouquetDAO.create(bouquetToAdd);
		bouquetToAdd.setId(resultId);
		FlowerBouquet resultBouquet = bouquetDAO.getByPK(resultId);
		Assert.assertTrue(resultBouquet.getId().equals(bouquetToAdd.getId()));
		Assert.assertTrue(resultBouquet.getOrderId().equals(order.getId()));
		Assert.assertTrue(resultBouquet.getWrapperComponent().getId().equals(wrapper.getId()));
		Assert.assertTrue(resultBouquet.getDecorationComponent().getId().equals(decoration.getId()));
		Assert.assertTrue(resultBouquet.getPrice().equals(bouquetToAdd.getPrice()));
	}

	@Test
	public void testUpdateBouquet() {
		bouquet.setDecorationComponent(null);
		bouquet.setPrice((float) 90.00);
		bouquetDAO.update(bouquet);
		FlowerBouquet resultBouquet = bouquetDAO.getByPK(bouquet.getId());
		Assert.assertTrue(resultBouquet.getOrderId().equals(order.getId()));
		Assert.assertTrue(resultBouquet.getWrapperComponent().getId().equals(wrapper.getId()));
		Assert.assertTrue(resultBouquet.getDecorationComponent() == null);
		Assert.assertTrue(resultBouquet.getPrice().equals(bouquet.getPrice()));
	}

	@Test
	public void testDeleteBouquet() {
		FlowerBouquet bouquetToDlt = new FlowerBouquet();
		bouquetToDlt.setDecorationComponent(decoration);
		bouquetToDlt.setWrapperComponent(null);
		bouquetToDlt.setOrderId(order.getId());
		bouquetToDlt.setPrice((float) 80.00);
		Long resultId = bouquetDAO.create(bouquetToDlt);
		bouquetDAO.delete(resultId);
		Assert.assertTrue(!((FlowerBouquetDAO) bouquetDAO).bouquetExists(resultId));
		FlowerBouquet resultBouquet = bouquetDAO.getByPK(resultId);
		Assert.assertTrue(resultBouquet == null);
	}

	@Test
	public void testBouquetExists() {
		Assert.assertTrue(((FlowerBouquetDAO) bouquetDAO).bouquetExists(bouquet.getId()));
	}

	private static void closeStatement(PreparedStatement statement) {
		if (statement != null)
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}
}
