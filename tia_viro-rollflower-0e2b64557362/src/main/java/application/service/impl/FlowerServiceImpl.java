package application.service.impl;

import java.util.List;

import application.database.dao.FlowerDAO;
import application.database.dao.impl.AbstractDAOImpl;
import application.database.dao.impl.FlowerDAOimpl;
import application.model.Flower;
import application.service.FlowerService;

public class FlowerServiceImpl implements FlowerService {
	private FlowerDAO flowerDAO;
	private AbstractDAOImpl<Flower> aFlowerDAO;

	public FlowerServiceImpl() {
		flowerDAO = new FlowerDAOimpl();
		aFlowerDAO  = new FlowerDAOimpl();
	}

	public List<Flower> getAllFlowers() {
		return aFlowerDAO.getAll();
	}

	public Flower getFlowerById(Long id) {
		return aFlowerDAO.getByPK(id);
	}

	public void addFlower(Flower flower) {
		if(!flowerExists(flower)) {
			aFlowerDAO.create(flower);
		}
	}

	public void updateFlower(Flower flower) {
		aFlowerDAO.update(flower);
	}

	public void deleteFlower(Long id) {
		aFlowerDAO.delete(id);
	}

	public boolean flowerExists(Flower flower) {
		return flowerDAO.flowerExists(flower);
	}

}
