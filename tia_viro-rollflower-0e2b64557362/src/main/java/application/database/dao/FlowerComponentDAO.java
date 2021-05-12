package application.database.dao;

import java.util.List;

import application.model.component.FlowerComponent;

public interface FlowerComponentDAO {

	List<FlowerComponent> getFlowerComponentsByBouquetId(Long id);

	boolean flowerComponentExists(FlowerComponent component);
}
