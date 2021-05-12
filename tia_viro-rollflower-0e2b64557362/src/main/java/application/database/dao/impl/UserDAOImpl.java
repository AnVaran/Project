package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.UserDAO;
import application.model.Role;
import application.model.User;

public class UserDAOImpl extends AbstractDAOImpl<User> implements UserDAO {
	private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);
	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet flowerResultSet;

	@Override
	public String getSelectQuery() {
		return "SELECT * FROM public.flower_user INNER JOIN public.user_roles ON public.flower_user.role_id = public.user_roles.id;";
	}

	@Override
	public String getSelectByIdQuery() {
		return "SELECT * FROM public.flower_user INNER JOIN public.user_roles ON public.flower_user.role_id = public.user_roles.id WHERE  public.flower_user.id = ?;";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO public.flower_user (role_id, user_name, user_email, user_password) VALUES (?, ?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE public.flower_user SET user_name = ?, user_email = ?, user_password = ?, role_id = ? WHERE id = ?;";
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM public.flower_user WHERE id = ?;";
	}

	@Override
	protected User parseResultSet(ResultSet rs) {
		User userFromDatabase = new User();
		try {
			userFromDatabase.setId(rs.getLong("id"));
			userFromDatabase.setEmail(rs.getString("user_email"));
			userFromDatabase.setPassword(rs.getString("user_password"));
			userFromDatabase.setUsername(rs.getString("user_name"));
			userFromDatabase.setSessionKey(rs.getString("session_key"));
			Role role = new Role();
			role.setRoleName(rs.getString("role_name"));
			role.setId(rs.getLong("role_id"));
			userFromDatabase.setUserRole(role);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return userFromDatabase;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement pStatement, User user) {
		try {
			pStatement.setLong(1, user.getUserRole().getId());
			pStatement.setString(2, user.getUsername());
			pStatement.setString(3, user.getEmail());
			pStatement.setString(4, user.getPassword());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement pStatement, User user) {
		try {
			pStatement.setString(1, user.getUsername());
			pStatement.setString(2, user.getEmail());
			pStatement.setString(3, user.getPassword());
			pStatement.setLong(4, user.getUserRole().getId());
			pStatement.setLong(5, user.getId());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	public boolean userExists(User user) {
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement("SELECT * FROM public.flower_user WHERE user_email = ?;");
			pStatement.setString(1, user.getEmail());
			flowerResultSet = pStatement.executeQuery();
			if (flowerResultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (flowerResultSet != null)
				try {
					flowerResultSet.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
			if (pStatement != null)
				try {
					pStatement.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
		}
		return false;
	}

	@Override
	public User getUserByEmail(String email) {
		User user = null;
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement("SELECT * FROM public.flower_user INNER JOIN public.user_roles ON public.flower_user.role_id = public.user_roles.id WHERE  public.flower_user.user_email = ?;");
			pStatement.setString(1, email);
			flowerResultSet = pStatement.executeQuery();
			if (flowerResultSet.next()) {
				user = parseResultSet(flowerResultSet);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (flowerResultSet != null)
				try {
					flowerResultSet.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
			if (pStatement != null)
				try {
					pStatement.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
		}
		return user;
	}

	@Override
	public User getUserBySessionKey(String sessionKey) {
		User user = null;
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement("SELECT * FROM public.flower_user INNER JOIN public.user_roles ON public.flower_user.role_id = public.user_roles.id WHERE  public.flower_user.session_key = ?;");
			pStatement.setString(1, sessionKey);
			flowerResultSet = pStatement.executeQuery();
			if (flowerResultSet.next()) {
				user = parseResultSet(flowerResultSet);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (flowerResultSet != null)
				try {
					flowerResultSet.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
			if (pStatement != null)
				try {
					pStatement.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
		}
		return user;
	}

	@Override
	public void updateUserSessionKey(User user) {
		try (Connection connection = DatabasePoolConnection.getInstance().getConnection();
				PreparedStatement pStatement = connection.prepareStatement(
						"UPDATE public.flower_user SET session_key = ? WHERE id = ?;")) {
			if (user.getSessionKey()==null) {
				pStatement.setNull(1, Types.VARCHAR);
			} else{
				pStatement.setString(1, user.getSessionKey());
			}
			pStatement.setLong(2, user.getId());
			pStatement.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

}
