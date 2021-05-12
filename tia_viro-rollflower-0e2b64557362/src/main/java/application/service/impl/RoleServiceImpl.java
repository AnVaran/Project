package application.service.impl;

import java.util.List;

import application.database.dao.RoleDAO;
import application.database.dao.impl.AbstractDAOImpl;
import application.database.dao.impl.RoleDAOImpl;
import application.model.Role;
import application.service.RoleService;

public class RoleServiceImpl implements RoleService {
	private AbstractDAOImpl<Role> roleDAO;
	private RoleDAO altRoleDAO;

	public RoleServiceImpl() {
		roleDAO = new RoleDAOImpl();
		altRoleDAO = new RoleDAOImpl();
	}

	@Override
	public List<Role> getAllRoles() {
		return roleDAO.getAll();
	}

	@Override
	public Role getRoleById(Long id) {
		return roleDAO.getByPK(id);
	}

	@Override
	public void addRoler(Role role) {
		if (!altRoleDAO.roleExists(role)) {
			roleDAO.create(role);
		}
	}

	@Override
	public void updateUser(Role role) {
		if (!altRoleDAO.roleExists(role)) {
			roleDAO.update(role);
		}
	}

	@Override
	public void deleteUser(Role role) {
		if (!altRoleDAO.roleExists(role)) {
			roleDAO.delete(role.getId());
		}
	}

}
