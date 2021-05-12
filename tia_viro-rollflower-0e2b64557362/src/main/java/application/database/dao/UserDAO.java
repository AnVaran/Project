package application.database.dao;

import application.model.User;

public interface UserDAO {

	boolean userExists(User user);

	User getUserByEmail(String email);

	User getUserBySessionKey(String sessionKey);

	void updateUserSessionKey(User user);
}
