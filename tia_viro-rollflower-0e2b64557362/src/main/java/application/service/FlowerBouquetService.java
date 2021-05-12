package application.service;

import java.util.List;

import application.model.FlowerBouquet;

public interface FlowerBouquetService {
	List<FlowerBouquet> getAllBouquets();
	
	List<FlowerBouquet> getBouquetByOrderId(Long id);

	FlowerBouquet getBouquetById(Long id);

	void addBouquet(FlowerBouquet bouquet, Long orderId);

	void updateBouquet(FlowerBouquet bouquet);

	void deleteBouquet(Long id);

	boolean bouquetExists(Long id);
}
