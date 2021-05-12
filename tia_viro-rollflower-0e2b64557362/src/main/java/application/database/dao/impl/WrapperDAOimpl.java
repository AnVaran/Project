package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.WrapperDAO;
import application.enums.MaterialEnum;
import application.model.Wrapper;

public class WrapperDAOimpl extends AbstractDAOImpl<Wrapper> implements WrapperDAO {
	private static final Logger LOGGER = Logger.getLogger(WrapperDAOimpl.class);
	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet wrapperResultSet;

	public boolean wrapperExists(Wrapper wrapper) {
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement(
					"SELECT * FROM public.bouquet_wrapper WHERE wrapper_name = ? AND wrapper_material = ?;");
			pStatement.setString(1, wrapper.getName());
			pStatement.setString(2, wrapper.getMaterial().toString());
			wrapperResultSet = pStatement.executeQuery();
			if (wrapperResultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (wrapperResultSet != null)
				try {
					wrapperResultSet.close();
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
	public String getSelectQuery() {
		return "SELECT * FROM public.bouquet_wrapper";
	}

	@Override
	public String getSelectByIdQuery() {
		return "SELECT * FROM public.bouquet_wrapper WHERE id = ?;";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO public.bouquet_wrapper (wrapper_name, wrapper_material, wrapper_price, wrapper_description) VALUES (?, ?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE public.bouquet_wrapper SET wrapper_price = ?, wrapper_description = ? WHERE wrapper_name = ? AND wrapper_material = ?;";
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM public.bouquet_wrapper WHERE id = ?;";
	}

	@Override
	protected Wrapper parseResultSet(ResultSet rs) {
		Wrapper wrapperFromDatabase = new Wrapper();
		try {
			wrapperFromDatabase.setId(rs.getLong("id"));
			wrapperFromDatabase.setName(rs.getString("wrapper_name"));
			wrapperFromDatabase.setMaterial(MaterialEnum.valueOf(rs.getString("wrapper_material")));
			wrapperFromDatabase.setPrice(rs.getFloat("wrapper_price"));
			wrapperFromDatabase.setDescription(rs.getString("wrapper_description"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return wrapperFromDatabase;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement pStatement, Wrapper wrapper) {
		try {
			pStatement.setString(1, wrapper.getName());
			pStatement.setString(2, wrapper.getMaterial().toString());
			pStatement.setFloat(3, wrapper.getPrice());
			pStatement.setString(4, wrapper.getDescription());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement pStatement, Wrapper wrapper) {
		try {
			pStatement.setFloat(1, wrapper.getPrice());
			if (wrapper.getDescription() == null) {
				pStatement.setNull(2, Types.VARCHAR);
			} else {
				pStatement.setString(2, wrapper.getDescription());
			}
			pStatement.setString(3, wrapper.getName());
			pStatement.setString(4, wrapper.getMaterial().toString());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}

	}

}
