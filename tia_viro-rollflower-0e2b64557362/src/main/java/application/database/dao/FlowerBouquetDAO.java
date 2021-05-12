package application.database.dao;

import java.util.List;

import application.model.FlowerBouquet;

public interface FlowerBouquetDAO {

	List<FlowerBouquet> getBouquetsByOrderId(long id);

	boolean bouquetExists(long id);
}
