package application.service.impl;

import java.util.List;

import application.database.dao.DecorationDAO;
import application.database.dao.impl.AbstractDAOImpl;
import application.database.dao.impl.DecorationDAOimpl;
import application.model.Decoration;
import application.service.DecorationService;

public class DecorationServiceImpl implements DecorationService {
	private DecorationDAO decorationDAO;
	private AbstractDAOImpl<Decoration> aDecorationDAO;

	public DecorationServiceImpl() {
		decorationDAO = new DecorationDAOimpl();
		aDecorationDAO = new DecorationDAOimpl();
	}

	public List<Decoration> getAllDecorations() {
		return aDecorationDAO.getAll();
	}

	public Decoration getDecorationById(Long id) {
		return aDecorationDAO.getByPK(id);
	}

	public void addDecoration(Decoration decoration) {
		if (!decorationExists(decoration)) {
			aDecorationDAO.create(decoration);
		}
	}

	public void updateDecoration(Decoration decoration) {
		if (decorationExists(decoration)) {
			aDecorationDAO.update(decoration);
		}
	}

	public void deleteDecoration(Long id) {
		aDecorationDAO.delete(id);
	}

	public boolean decorationExists(Decoration decoration) {
		return decorationDAO.decorationExists(decoration);
	}

}
