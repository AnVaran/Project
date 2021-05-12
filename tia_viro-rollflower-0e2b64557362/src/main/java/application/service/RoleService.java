package application.service;

import java.util.List;

import application.model.Role;

public interface RoleService {
	List<Role> getAllRoles();

	Role getRoleById(Long id);

	void addRoler(Role role);

	void updateUser(Role role);

	void deleteUser(Role role);
}
