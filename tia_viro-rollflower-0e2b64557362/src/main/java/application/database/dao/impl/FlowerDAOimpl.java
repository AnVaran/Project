package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.FlowerDAO;
import application.enums.ColorEnum;
import application.enums.FlowerLengthEnum;
import application.model.Flower;

public class FlowerDAOimpl extends AbstractDAOImpl<Flower> implements FlowerDAO {
	private static final Logger LOGGER = Logger.getLogger(FlowerDAOimpl.class);
	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet flowerResultSet;

	public boolean flowerExists(Flower flower) {
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement(
					"SELECT * FROM public.flower WHERE flower_name = ? AND flower_color = ? AND flower_length = ?;");
			pStatement.setString(1, flower.getName());
			pStatement.setString(2, flower.getColor().toString());
			pStatement.setString(3, flower.getLength().toString());
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
	public String getSelectQuery() {
		return "SELECT * FROM public.flower;";
	}

	@Override
	public String getSelectByIdQuery() {
		return "SELECT * FROM public.flower WHERE id = ?;";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO public.flower (flower_name, flower_color, flower_length, flower_price) VALUES (?, ?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE public.flower SET flower_price = ? WHERE flower_name = ? AND flower_color = ? AND flower_length = ?;";
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM public.flower WHERE id = ?;";
	}

	@Override
	protected Flower parseResultSet(ResultSet rs) {
		Flower flowerFromDatabase = new Flower();
		try {
			flowerFromDatabase.setId(rs.getLong("id"));
			flowerFromDatabase.setName(rs.getString("flower_name"));
			flowerFromDatabase.setColor(ColorEnum.valueOf(rs.getString("flower_color")));
			flowerFromDatabase.setLength(FlowerLengthEnum.valueOf(rs.getString("flower_length")));
			flowerFromDatabase.setPrice(rs.getFloat("flower_price"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return flowerFromDatabase;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement pStatement, Flower flower) {
		try {
			pStatement.setString(1, flower.getName());
			pStatement.setString(2, flower.getColor().toString());
			pStatement.setString(3, flower.getLength().toString());
			pStatement.setFloat(4, flower.getPrice());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement pStatement, Flower flower) {
		try {
			pStatement.setFloat(1, flower.getPrice());
			pStatement.setString(2, flower.getName());
			pStatement.setString(3, flower.getColor().toString());
			pStatement.setString(4, flower.getLength().toString());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

}
