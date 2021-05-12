package application.service.impl;

import java.util.List;

import application.database.dao.UserDAO;
import application.database.dao.impl.AbstractDAOImpl;
import application.database.dao.impl.UserDAOImpl;
import application.model.User;
import application.service.UserService;

public class UserServiceImpl implements UserService {
	private AbstractDAOImpl<User> userDAO;

	public UserServiceImpl() {
		userDAO = new UserDAOImpl();
	}

	@Override
	public List<User> getAllUsers() {
		return userDAO.getAll();
	}

	@Override
	public User getUserById(Long id) {
		return userDAO.getByPK(id);
	}

	@Override
	public Long addUser(User user) {
		if (!((UserDAO) userDAO).userExists(user)) {
			return userDAO.create(user);
		}
		return null;
	}

	@Override
	public void updateUser(User user) {
		if (((UserDAO) userDAO).userExists(user)) {
			userDAO.update(user);
		}
	}

	@Override
	public void deleteUser(Long id) {
		userDAO.delete(id);
	}

	@Override
	public boolean userExists(User user) {
		return ((UserDAO) userDAO).userExists(user);
	}

	@Override
	public User getUserByEmail(String email) {
		return ((UserDAO) userDAO).getUserByEmail(email);
	}

	@Override
	public User getUserBySessionKey(String sessionKey) {
		return ((UserDAO) userDAO).getUserBySessionKey(sessionKey);
	}

	@Override
	public void updateUserSessionKey(User user) {
		((UserDAO) userDAO).updateUserSessionKey(user);		
	}

}
