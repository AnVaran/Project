package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.RoleDAO;
import application.model.Role;

public class RoleDAOImpl extends AbstractDAOImpl<Role> implements RoleDAO {
	private static final Logger LOGGER = Logger.getLogger(RoleDAOImpl.class);
	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet flowerResultSet;

	@Override
	public String getSelectQuery() {
		return "SELECT * FROM public.user_roles;";
	}

	@Override
	public String getSelectByIdQuery() {
		return "SELECT * FROM public.user_roles WHERE id = ?;";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO public.user_roles (role_name) VALUES (?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE public.user_roles SET role_name = ? WHERE id = ?;";
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM public.user_roles WHERE id = ?;";
	}

	@Override
	protected Role parseResultSet(ResultSet rs) {
		Role roleFromDatabase = new Role();
		try {
			roleFromDatabase.setId(rs.getLong("id"));
			roleFromDatabase.setRoleName(rs.getString("role_name"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return roleFromDatabase;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement pStatement, Role role) {
		try {
			pStatement.setString(1, role.getRoleName());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement pStatement, Role role) {
		try {
			pStatement.setString(1, role.getRoleName());
			pStatement.setLong(2, role.getId());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	public boolean roleExists(Role role) {
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement("SELECT * FROM public.user_roles WHERE role_name = ?;");
			pStatement.setString(1, role.getRoleName());
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
}
