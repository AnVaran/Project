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
import application.database.dao.FlowerComponentDAO;
import application.enums.ColorEnum;
import application.enums.FlowerLengthEnum;
import application.enums.MaterialEnum;
import application.enums.OrderStatusEnum;
import application.model.BouquetOrder;
import application.model.Decoration;
import application.model.Flower;
import application.model.FlowerBouquet;
import application.model.Wrapper;
import application.model.component.FlowerComponent;

public class FlowerComponentDAOTest {
	private static final Logger LOGGER = Logger.getLogger(FlowerComponentDAOTest.class);
	private static Connection connection;
	private static PreparedStatement firstPrepStatement;
	private PreparedStatement secondPrepStatement;
	private FlowerComponent component = new FlowerComponent();
	private static FlowerBouquet bouquet;
	private static BouquetOrder order;
	private static Flower flower;
	private static Wrapper wrapper;
	private static Decoration decoration;
	private static AbstractDAOImpl<FlowerComponent> componentDAO;

	@BeforeClass
	public static void initSetUp() throws Exception {
		connection = DatabasePoolConnection.getInstance().getConnection();
		componentDAO = new FlowerComponentDAOimpl();

		order = new BouquetOrder();
		order.setId((long) 30000);
		order.setCommentary(" ");
		order.setShippingAdres(" ");
		order.setOrderStatus(OrderStatusEnum.PENDING);

		flower = new Flower();
		flower.setId((long) 20000);
		flower.setName("flower");
		flower.setColor(ColorEnum.RED);
		flower.setLength(FlowerLengthEnum.MEDIUM);
		flower.setPrice((float) 100.00);

		wrapper = new Wrapper();
		wrapper.setId((long) 30000);
		wrapper.setName("wrap");
		wrapper.setMaterial(MaterialEnum.PAPER);
		wrapper.setPrice((float) 10.00);
		wrapper.setDescription(" ");

		decoration = new Decoration();
		decoration.setId((long) 30000);
		decoration.setName("deco");
		decoration.setMaterial(MaterialEnum.WOODEN);
		decoration.setPrice((float) 10.00);
		decoration.setDescription(" ");

		bouquet = new FlowerBouquet();
		bouquet.setId((long) 20000);
		bouquet.setWrapperComponent(wrapper);
		bouquet.setDecorationComponent(decoration);
		bouquet.setOrderId(order.getId());
		bouquet.setPrice((float) 100.00);

		firstPrepStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_order (id, order_commentary, shipping_adress, order_status) VALUES (?, ?, ?, ?);");
		firstPrepStatement.setLong(1, order.getId());
		firstPrepStatement.setString(2, order.getCommentary());
		firstPrepStatement.setString(3, order.getShippingAdres());
		firstPrepStatement.setString(4, order.getOrderStatus().toString());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		firstPrepStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_wrapper (id, wrapper_name, wrapper_material, wrapper_price, wrapper_description) VALUES (?, ?, ?, ?, ?);");
		firstPrepStatement.setLong(1, wrapper.getId());
		firstPrepStatement.setString(2, wrapper.getName());
		firstPrepStatement.setString(3, wrapper.getMaterial().toString());
		firstPrepStatement.setFloat(4, wrapper.getPrice());
		firstPrepStatement.setString(5, wrapper.getDescription());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		firstPrepStatement = connection.prepareStatement(
				"INSERT INTO public.bouquet_decoration (id, decoration_name, decoration_material, decoration_price, decoration_description) VALUES (?, ?, ?, ?, ?);");
		firstPrepStatement.setLong(1, decoration.getId());
		firstPrepStatement.setString(2, decoration.getName());
		firstPrepStatement.setString(3, decoration.getMaterial().toString());
		firstPrepStatement.setFloat(4, decoration.getPrice());
		firstPrepStatement.setString(5, decoration.getDescription());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		firstPrepStatement = connection.prepareStatement(
				"INSERT INTO public.flower (id, flower_name, flower_color, flower_length, flower_price) VALUES (?, ?, ?, ?, ?);");
		firstPrepStatement.setLong(1, flower.getId());
		firstPrepStatement.setString(2, flower.getName());
		firstPrepStatement.setString(3, flower.getColor().toString());
		firstPrepStatement.setString(4, flower.getLength().toString());
		firstPrepStatement.setFloat(5, flower.getPrice());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		firstPrepStatement = connection.prepareStatement(
				"INSERT INTO public.flower_bouquet (id, wrapper_id, decoration_id, order_id, bouquet_price) VALUES (?, ?, ?, ?, ?);");
		firstPrepStatement.setLong(1, bouquet.getId());
		firstPrepStatement.setLong(2, bouquet.getWrapperComponent().getId());
		firstPrepStatement.setLong(3, bouquet.getDecorationComponent().getId());
		firstPrepStatement.setLong(4, bouquet.getOrderId());
		firstPrepStatement.setFloat(5, bouquet.getPrice());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

	}

	@AfterClass
	public static void shutDown() throws Exception {
		firstPrepStatement = connection.prepareStatement("DELETE FROM public.flower_bouquet WHERE id = ?;");
		firstPrepStatement.setLong(1, bouquet.getId());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		firstPrepStatement = connection.prepareStatement("DELETE FROM public.bouquet_order WHERE id = ?;");
		firstPrepStatement.setLong(1, order.getId());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		firstPrepStatement = connection.prepareStatement("DELETE FROM public.bouquet_decoration WHERE id = ?;");
		firstPrepStatement.setLong(1, decoration.getId());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		firstPrepStatement = connection.prepareStatement("DELETE FROM public.bouquet_wrapper WHERE id = ?;");
		firstPrepStatement.setLong(1, wrapper.getId());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		firstPrepStatement = connection.prepareStatement("DELETE FROM public.flower WHERE id = ?;");
		firstPrepStatement.setLong(1, flower.getId());
		firstPrepStatement.execute();
		closeStatement(firstPrepStatement);

		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
	}

	@Before
	public void setUp() throws Exception {
		component.setId((long) 10000);
		component.setBouquetId(bouquet.getId());
		component.setFlowerItem(flower);
		component.setFlowerQuantyty(3);

		secondPrepStatement = connection.prepareStatement(
				"INSERT INTO public.flower_component (id, flower_quantyty, flower_id, bouquet_id) VALUES (?, ?, ?, ?);");

		secondPrepStatement.setLong(1, component.getId());
		secondPrepStatement.setInt(2, component.getFlowerQuantyty());
		secondPrepStatement.setLong(3, component.getFlowerItem().getId());
		secondPrepStatement.setLong(4, component.getBouquetId());

		secondPrepStatement.execute();

		closeStatement(secondPrepStatement);
	}

	@After
	public void tearDown() throws Exception {
		secondPrepStatement = connection.prepareStatement("DELETE FROM public.flower_component WHERE id = ?;");
		secondPrepStatement.setLong(1, component.getId());
		secondPrepStatement.execute();
		closeStatement(secondPrepStatement);
	}

	@Test
	public void testAllCOmponents() {
		List<FlowerComponent> resultList = componentDAO.getAll();
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testGetComponentById() {
		FlowerComponent resultComponent = componentDAO.getByPK(component.getId());
		Assert.assertTrue(resultComponent.getId().equals(component.getId()));
		Assert.assertTrue(resultComponent.getBouquetId().equals(bouquet.getId()));
		Assert.assertTrue(resultComponent.getFlowerItem().getId().equals(flower.getId()));
		Assert.assertTrue(resultComponent.getFlowerQuantyty() == component.getFlowerQuantyty());
	}

	@Test
	public void testGetComponentsByBouquetId() {
		List<FlowerComponent> resultList = ((FlowerComponentDAO) componentDAO).getFlowerComponentsByBouquetId(bouquet.getId());
		Assert.assertNotNull(resultList);
		Assert.assertTrue(resultList.size() > 0);
	}

	@Test
	public void testAddComponent() {
		FlowerComponent componentToAdd = new FlowerComponent();
		componentToAdd.setFlowerItem(flower);
		componentToAdd.setBouquetId(bouquet.getId());
		componentToAdd.setFlowerQuantyty(4);
		long resultId = componentDAO.create(componentToAdd);
		componentToAdd.setId(resultId);
		FlowerComponent resultComponent = componentDAO.getByPK(resultId);
		Assert.assertTrue(resultComponent.getId() == componentToAdd.getId());
		Assert.assertTrue(resultComponent.getBouquetId().equals(bouquet.getId()));
		Assert.assertTrue(resultComponent.getFlowerItem().getId().equals(flower.getId()));
		Assert.assertTrue(resultComponent.getFlowerQuantyty() == componentToAdd.getFlowerQuantyty());
	}

	@Test
	public void testUpdateFlowerComponent() {
		component.setFlowerQuantyty(6);
		componentDAO.update(component);
		FlowerComponent resultComponent = componentDAO.getByPK(component.getId());
		Assert.assertTrue(resultComponent.getId().equals(component.getId()));
		Assert.assertTrue(resultComponent.getFlowerItem().getId().equals(flower.getId()));
		Assert.assertTrue(resultComponent.getBouquetId().equals(bouquet.getId()));
		Assert.assertTrue(resultComponent.getFlowerQuantyty() == component.getFlowerQuantyty());
	}

	@Test
	public void testDeleteBouquet() {
		FlowerComponent componentToDlt = new FlowerComponent();
		componentToDlt.setBouquetId(bouquet.getId());
		componentToDlt.setFlowerItem(flower);
		componentToDlt.setFlowerQuantyty(1);
		Long resultId = componentDAO.create(componentToDlt);
		componentToDlt.setId(resultId);
		componentDAO.delete(resultId);
		FlowerComponent resultComponent = componentDAO.getByPK(resultId);
		Assert.assertTrue(resultComponent == null);
	}

	@Test
	public void testBouquetExists() {
		Assert.assertTrue(((FlowerComponentDAO) componentDAO).flowerComponentExists(component));
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
