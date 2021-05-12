package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.DecorationDAO;
import application.enums.MaterialEnum;
import application.model.Decoration;

public class DecorationDAOimpl extends AbstractDAOImpl<Decoration> implements DecorationDAO {
	private static final Logger LOGGER = Logger.getLogger(DecorationDAOimpl.class);
	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet decorationResultSet;

	public boolean decorationExists(Decoration decoration) {
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement(
					"SELECT * FROM public.bouquet_decoration WHERE decoration_name = ? AND decoration_material = ?;");
			pStatement.setString(1, decoration.getName());
			pStatement.setString(2, decoration.getMaterial().toString());
			decorationResultSet = pStatement.executeQuery();
			if (decorationResultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (decorationResultSet != null)
				try {
					decorationResultSet.close();
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
		return "SELECT * FROM public.bouquet_decoration;";
	}

	@Override
	public String getSelectByIdQuery() {
		return "SELECT * FROM public.bouquet_decoration WHERE id = ?;";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO public.bouquet_decoration (decoration_name, decoration_material, decoration_price, decoration_description) VALUES (?, ?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE public.bouquet_decoration SET decoration_price = ?, decoration_description = ? WHERE decoration_name = ? AND decoration_material = ?;";
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM public.bouquet_decoration WHERE id = ?;";
	}

	@Override
	protected Decoration parseResultSet(ResultSet rs) {
		Decoration decorationFromDatabase = new Decoration();
		try {
			decorationFromDatabase.setId(rs.getLong("id"));
			decorationFromDatabase.setName(rs.getString("decoration_name"));
			decorationFromDatabase.setMaterial(MaterialEnum.valueOf(rs.getString("decoration_material")));
			decorationFromDatabase.setPrice(rs.getFloat("decoration_price"));
			decorationFromDatabase.setDescription(rs.getString("decoration_description"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return decorationFromDatabase;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement pStatement, Decoration decoration) {
		try {
			pStatement.setString(1, decoration.getName());
			pStatement.setString(2, decoration.getMaterial().toString());
			pStatement.setFloat(3, decoration.getPrice());
			pStatement.setString(4, decoration.getDescription());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement pStatement, Decoration decoration) {
		try {
			pStatement.setFloat(1, decoration.getPrice());
			if (decoration.getDescription() == null) {
				pStatement.setNull(2, Types.VARCHAR);
			} else {
				pStatement.setString(2, decoration.getDescription());
			}
			pStatement.setString(3, decoration.getName());
			pStatement.setString(4, decoration.getMaterial().toString());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

}
