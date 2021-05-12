package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.GenericDAO;

public abstract class AbstractDAOImpl<T> implements GenericDAO<T> {
	private static final Logger LOGGER = Logger.getLogger(AbstractDAOImpl.class);

	public abstract String getSelectQuery();

	public abstract String getSelectByIdQuery();

	public abstract String getCreateQuery();

	public abstract String getUpdateQuery();

	public abstract String getDeleteQuery();

	protected abstract T parseResultSet(ResultSet rs);

	protected abstract void prepareStatementForInsert(PreparedStatement statement, T object);

	protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object);

	public Long create(T object) {
		ResultSet resultSet = null;
		try (Connection connection = DatabasePoolConnection.getInstance().getConnection();
				PreparedStatement pStatement = connection.prepareStatement(getCreateQuery(),
						Statement.RETURN_GENERATED_KEYS)) {
			prepareStatementForInsert(pStatement, object);
			pStatement.executeUpdate();
			resultSet = pStatement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getLong(1);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		return null;
	}

	public T getByPK(Long key) {
		Optional<T> result = Optional.empty();
		ResultSet resultSet = null;
		try (Connection connection = DatabasePoolConnection.getInstance().getConnection();
				PreparedStatement pStatement = connection.prepareStatement(getSelectByIdQuery())) {
			pStatement.setLong(1, key);
			resultSet = pStatement.executeQuery();
			if (resultSet.next()) {
				result = Optional.ofNullable(parseResultSet(resultSet));
			} else
				return null;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		return result.get();
	}

	public void update(T object) {
		try (Connection connection = DatabasePoolConnection.getInstance().getConnection();
				PreparedStatement pStatement = connection.prepareStatement(getUpdateQuery())) {
			prepareStatementForUpdate(pStatement, object);
			pStatement.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public void delete(Long key) {
		try (Connection connection = DatabasePoolConnection.getInstance().getConnection();
				PreparedStatement pStatement = connection.prepareStatement(getDeleteQuery())) {
			pStatement.setLong(1, key);
			pStatement.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public List<T> getAll() {
		List<T> resultList = new ArrayList<T>();
		try (Connection connection = DatabasePoolConnection.getInstance().getConnection();
				PreparedStatement pStatement = connection.prepareStatement(getSelectQuery());
				ResultSet resultSet = pStatement.executeQuery()) {
			while (resultSet.next()) {
				resultList.add(parseResultSet(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return resultList;
	}

}
