package application.database.dao;

import application.model.Flower;

public interface FlowerDAO {
	boolean flowerExists(Flower flower);
}
