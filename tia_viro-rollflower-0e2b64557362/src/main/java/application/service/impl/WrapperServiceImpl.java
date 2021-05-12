package application.service.impl;

import java.util.List;

import application.database.dao.WrapperDAO;
import application.database.dao.impl.AbstractDAOImpl;
import application.database.dao.impl.WrapperDAOimpl;
import application.model.Wrapper;
import application.service.WrapperService;

public class WrapperServiceImpl implements WrapperService {
	private WrapperDAO wrapperDAO;
	private AbstractDAOImpl<Wrapper> aWrapperDAO;

	public WrapperServiceImpl() {
		wrapperDAO = new WrapperDAOimpl();
		aWrapperDAO = new WrapperDAOimpl();
	}

	public List<Wrapper> getAllWrappers() {
		return aWrapperDAO.getAll();
	}

	public Wrapper getWrapperById(Long id) {
		return aWrapperDAO.getByPK(id);
	}

	public void addWrapper(Wrapper wrapper) {
		if (!wrapperExists(wrapper)) {
			aWrapperDAO.create(wrapper);
		}
	}

	public void updateWrapper(Wrapper wrapper) {
		if (wrapperExists(wrapper)) {
			aWrapperDAO.update(wrapper);
		}
	}

	public void deleteWrapper(Long id) {
		if (wrapperExists(aWrapperDAO.getByPK(id))) {
			aWrapperDAO.delete(id);
		}
	}

	public boolean wrapperExists(Wrapper wrapper) {
		return wrapperDAO.wrapperExists(wrapper);
	}

}
