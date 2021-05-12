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
import application.database.dao.BouquetOrderDAO;
import application.enums.OrderStatusEnum;
import application.model.BouquetOrder;

public class BouquetOrderDAOTest {
	private static final Logger LOGGER = Logger.getLogger(BouquetOrderDAOTest.class);
	private static Connection connection;
	private PreparedStatement pStatement;
	private static AbstractDAOImpl<BouquetOrder> orderDAO;
	private BouquetOrder order = new BouquetOrder();

	@BeforeClass
	public static void initSeetUp() throws Exception {
		orderDAO = new BouquetOrderDAOImpl();
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
		order.setId((long) 10000);
		order.setCommentary("commentary");
		order.setShippingAdres("adress");
		order.setOrderStatus(OrderStatusEnum.PENDING);
		pStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_order (id, order_commentary, shipping_adress, order_status) VALUES (?, ?, ?, ?);");
		pStatement.setLong(1, order.getId());
		if (order.getCommentary() == null || order.getCommentary().length() == 0) {
			pStatement.setNull(2, Types.VARCHAR);
		} else {
			pStatement.setString(2, order.getCommentary());
		}
		pStatement.setString(3, order.getShippingAdres());
		pStatement.setString(4, order.getOrderStatus().toString());
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
		pStatement = connection.prepareStatement("DELETE FROM public.bouquet_order WHERE id = ?;");
		pStatement.setLong(1, order.getId());
		pStatement.execute();
		if (pStatement != null)
			try {
				pStatement.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Test
	public void testAllOrders() {
		List<BouquetOrder> resultList = orderDAO.getAll();
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testGetOrderById() {
		BouquetOrder resultOrder = orderDAO.getByPK(order.getId());
		Assert.assertTrue(order.equals(resultOrder));
		Assert.assertTrue(resultOrder.getCommentary().equals(order.getCommentary()));
		Assert.assertTrue(resultOrder.getShippingAdres().equals(order.getShippingAdres()));
	}

	@Test
	public void testAddOrder() {
		BouquetOrder orderToAdd = new BouquetOrder();
		orderToAdd.setCommentary("commentary add");
		orderToAdd.setOrderStatus(OrderStatusEnum.COMPLETED);
		orderToAdd.setShippingAdres("adress add");
		Long id = orderDAO.create(orderToAdd);
		BouquetOrder resultOrder = orderDAO.getByPK(id);
		orderToAdd.setId(id);
		orderDAO.delete(id);
		Assert.assertTrue(orderToAdd.equals(resultOrder));
		Assert.assertTrue(orderToAdd.getCommentary().equals(resultOrder.getCommentary()));
		Assert.assertTrue(orderToAdd.getShippingAdres().equals(resultOrder.getShippingAdres()));
		Assert.assertTrue(orderToAdd.getOrderStatus().equals(resultOrder.getOrderStatus()));
	}

	@Test
	public void testUpdateOrder() {
		order.setOrderStatus(OrderStatusEnum.PENDING);
		order.setCommentary("commentary upd");
		order.setShippingAdres("adress upd");
		orderDAO.update(order);
		BouquetOrder resultOrder = orderDAO.getByPK(order.getId());
		Assert.assertTrue(order.equals(resultOrder));
		Assert.assertTrue(order.getCommentary().equals(resultOrder.getCommentary()));
		Assert.assertTrue(order.getShippingAdres().equals(resultOrder.getShippingAdres()));
		Assert.assertTrue(order.getOrderStatus().equals(resultOrder.getOrderStatus()));
	}

	@Test
	public void testDeleteOrder() {
		BouquetOrder orderToDlt = new BouquetOrder();
		orderToDlt.setOrderStatus(OrderStatusEnum.PENDING);
		orderToDlt.setShippingAdres("dlt order adress");
		Long id = orderDAO.create(orderToDlt);
		orderDAO.delete(id);
		Assert.assertTrue(!((BouquetOrderDAO) orderDAO).orderExists(id));
		Assert.assertTrue(orderDAO.getByPK(id)== null);
	}

	@Test
	public void testOrderExists() {
		Assert.assertTrue(((BouquetOrderDAO) orderDAO).orderExists(order.getId()));
	}
}
