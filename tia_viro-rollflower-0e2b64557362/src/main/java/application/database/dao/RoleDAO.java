package application.database.dao;

import application.model.Role;

public interface RoleDAO {
	boolean roleExists(Role role);
}
