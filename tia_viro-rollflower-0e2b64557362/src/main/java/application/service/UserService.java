package application.service;

import java.util.List;

import application.model.User;

public interface UserService {
	List<User> getAllUsers();

	User getUserById(Long id);

	Long addUser(User user);

	void updateUser(User user);

	void deleteUser(Long id);

	boolean userExists(User user);

	User getUserByEmail(String email);

	User getUserBySessionKey(String sessionKey);

	void updateUserSessionKey(User user);
}
