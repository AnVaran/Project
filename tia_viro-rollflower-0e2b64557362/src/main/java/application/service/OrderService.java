package application.service;

import java.util.List;

import application.model.BouquetOrder;

public interface OrderService {
	List<BouquetOrder> getAllOrder();

	BouquetOrder getOrderById(Long id);

	long addOrder(BouquetOrder order);

	void updateOrder(BouquetOrder order);

	void deleteOrder(Long id);

	boolean orderExists(Long id);
}
