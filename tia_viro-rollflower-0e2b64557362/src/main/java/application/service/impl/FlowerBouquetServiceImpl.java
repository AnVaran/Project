package application.service.impl;

import java.util.List;

import application.database.dao.FlowerBouquetDAO;
import application.database.dao.FlowerComponentDAO;
import application.database.dao.impl.AbstractDAOImpl;
import application.database.dao.impl.DecorationDAOimpl;
import application.database.dao.impl.FlowerBouquetDAOImpl;
import application.database.dao.impl.FlowerComponentDAOimpl;
import application.database.dao.impl.FlowerDAOimpl;
import application.database.dao.impl.WrapperDAOimpl;
import application.model.Decoration;
import application.model.Flower;
import application.model.FlowerBouquet;
import application.model.Wrapper;
import application.model.component.FlowerComponent;
import application.service.FlowerBouquetService;

public class FlowerBouquetServiceImpl implements FlowerBouquetService {
	private AbstractDAOImpl<FlowerBouquet> bouquetDAO;
	private FlowerBouquetDAO altBouquetDAO;
	private FlowerComponentDAO altComponentDAO;
	private AbstractDAOImpl<Flower> flowerDAO;
	private AbstractDAOImpl<Wrapper> wrapperDAO;
	private AbstractDAOImpl<Decoration> decorationDAO;
	private AbstractDAOImpl<FlowerComponent> flowerComponentDAO;

	public FlowerBouquetServiceImpl() {
		bouquetDAO = new FlowerBouquetDAOImpl();
		altBouquetDAO = new FlowerBouquetDAOImpl();
		flowerDAO = new FlowerDAOimpl();
		flowerComponentDAO = new FlowerComponentDAOimpl();
		altComponentDAO = new FlowerComponentDAOimpl();
		wrapperDAO = new WrapperDAOimpl();
		decorationDAO = new DecorationDAOimpl();
	}

	public List<FlowerBouquet> getAllBouquets() {
		List<FlowerBouquet> bouquetsList = bouquetDAO.getAll();
		for (FlowerBouquet bouquet : bouquetsList) {
			List<FlowerComponent> flowerComponentList = getFlowerComponentList(bouquet);
			for (FlowerComponent component : flowerComponentList) {
				component.setFlowerItem(flowerDAO.getByPK(component.getFlowerItem().getId()));
			}
			bouquet.setFlowerComponent(flowerComponentList);
			if (bouquet.getDecorationComponent() != null) {
				bouquet.setDecorationComponent(decorationDAO.getByPK(bouquet.getDecorationComponent().getId()));
			}
			if (bouquet.getWrapperComponent() != null) {
				bouquet.setWrapperComponent(wrapperDAO.getByPK(bouquet.getWrapperComponent().getId()));
			}
		}
		return bouquetsList;
	}

	public List<FlowerBouquet> getBouquetByOrderId(Long id) {
		List<FlowerBouquet> bouquetsList = altBouquetDAO.getBouquetsByOrderId(id);
		for (FlowerBouquet bouquet : bouquetsList) {
			List<FlowerComponent> flowerComponentList = getFlowerComponentList(bouquet);
			for (FlowerComponent component : flowerComponentList) {
				component.setFlowerItem(flowerDAO.getByPK(component.getFlowerItem().getId()));
			}
			bouquet.setFlowerComponent(flowerComponentList);
			if (bouquet.getDecorationComponent() != null) {
				bouquet.setDecorationComponent(decorationDAO.getByPK(bouquet.getDecorationComponent().getId()));
			}
			if (bouquet.getWrapperComponent() != null) {
				bouquet.setWrapperComponent(wrapperDAO.getByPK(bouquet.getWrapperComponent().getId()));
			}
		}
		return bouquetsList;
	}

	public FlowerBouquet getBouquetById(Long id) {
		FlowerBouquet bouquetById = bouquetDAO.getByPK(id);
		List<FlowerComponent> flowerComponentList = getFlowerComponentList(bouquetById);
		for (FlowerComponent component : flowerComponentList) {
			component.setFlowerItem(flowerDAO.getByPK(component.getFlowerItem().getId()));
		}
		bouquetById.setFlowerComponent(flowerComponentList);
		if (bouquetById.getDecorationComponent() != null) {
			bouquetById.setDecorationComponent(decorationDAO.getByPK(bouquetById.getDecorationComponent().getId()));
		}
		if (bouquetById.getWrapperComponent() != null) {
			bouquetById.setWrapperComponent(wrapperDAO.getByPK(bouquetById.getWrapperComponent().getId()));
		}
		return bouquetById;
	}

	public void addBouquet(FlowerBouquet bouquet, Long orderId) {
		bouquet.setOrderId(orderId);
		Long bouquetId = bouquetDAO.create(bouquet);
		addFlowerComponentList(bouquet, bouquetId);
	}

	public void updateBouquet(FlowerBouquet bouquet) {
		List<FlowerComponent> componentList = altComponentDAO.getFlowerComponentsByBouquetId(bouquet.getId());
		for (FlowerComponent component : componentList) {
			flowerComponentDAO.delete(component.getId());
		}
		for (FlowerComponent component : bouquet.getFlowerComponent()) {
			component.setBouquetId(bouquet.getId());
			flowerComponentDAO.update(component);
		}
		bouquetDAO.update(bouquet);
	}

	public void deleteBouquet(Long id) {
		bouquetDAO.delete(id);
	}

	public boolean bouquetExists(Long id) {
		return altBouquetDAO.bouquetExists(id);
	}

	private List<FlowerComponent> getFlowerComponentList(FlowerBouquet bouquet) {
		List<FlowerComponent> orderFlowerComponentList = altComponentDAO
				.getFlowerComponentsByBouquetId(bouquet.getId());
		for (FlowerComponent floverComponent : orderFlowerComponentList) {
			floverComponent.setFlowerItem(flowerDAO.getByPK(floverComponent.getFlowerItem().getId()));
		}
		return orderFlowerComponentList;
	}

	private void addFlowerComponentList(FlowerBouquet bouquet, long bouquetId) {
		List<FlowerComponent> orderFlowerComponentList = bouquet.getFlowerComponent();
		for (FlowerComponent flowerComponent : orderFlowerComponentList) {
			flowerComponent.setFlowerItem(flowerDAO.getByPK(flowerComponent.getFlowerItem().getId()));
			flowerComponent.setBouquetId(bouquetId);
			flowerComponentDAO.create(flowerComponent);
		}
	}
}
