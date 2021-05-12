package application.service;

import java.util.List;

import application.model.Flower;

public interface FlowerService {
	List<Flower> getAllFlowers();

	Flower getFlowerById(Long id);

	void addFlower(Flower flower);

	void updateFlower(Flower flower);

	void deleteFlower(Long id);

	boolean flowerExists(Flower flower);
}
