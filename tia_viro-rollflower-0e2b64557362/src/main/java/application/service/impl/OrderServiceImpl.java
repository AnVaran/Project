package application.service.impl;

import java.util.List;

import application.database.dao.BouquetOrderDAO;
import application.database.dao.impl.AbstractDAOImpl;
import application.database.dao.impl.BouquetOrderDAOImpl;
import application.model.BouquetOrder;
import application.model.FlowerBouquet;
import application.service.FlowerBouquetService;
import application.service.OrderService;

public class OrderServiceImpl implements OrderService {
	private BouquetOrderDAO orderDAO;
	private AbstractDAOImpl<BouquetOrder> altOrderDAO;
	private FlowerBouquetService bouquetService;

	public OrderServiceImpl() {
		orderDAO = new BouquetOrderDAOImpl();
		altOrderDAO = new BouquetOrderDAOImpl();
		bouquetService = new FlowerBouquetServiceImpl();
	}

	public List<BouquetOrder> getAllOrder() {
		List<BouquetOrder> orderList = altOrderDAO.getAll();
		for (BouquetOrder order : orderList) {
			order.setBouquets(bouquetService.getBouquetByOrderId(order.getId()));
		}
		return orderList;
	}

	public BouquetOrder getOrderById(Long id) {
		BouquetOrder order = altOrderDAO.getByPK(id);
		order.setBouquets(bouquetService.getBouquetByOrderId(order.getId()));
		return order;
	}

	public long addOrder(BouquetOrder order) {
		long orderId = altOrderDAO.create(order);
		for (FlowerBouquet bouquet : order.getBouquets()) {
			bouquetService.addBouquet(bouquet, orderId);
		}
		return orderId;
	}

	public void updateOrder(BouquetOrder order) {
		List<FlowerBouquet> bouquetList = order.getBouquets();
		for (FlowerBouquet bouquet : bouquetList) {
			bouquetService.updateBouquet(bouquet);
		}
		altOrderDAO.update(order);
	}

	public void deleteOrder(Long id) {
		altOrderDAO.delete(id);
	}

	public boolean orderExists(Long id) {
		return orderDAO.orderExists(id);
	}

}
