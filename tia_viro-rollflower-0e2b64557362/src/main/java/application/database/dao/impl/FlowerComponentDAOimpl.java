package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.FlowerComponentDAO;
import application.model.Flower;
import application.model.component.FlowerComponent;

public class FlowerComponentDAOimpl extends AbstractDAOImpl<FlowerComponent> implements FlowerComponentDAO {
	private static final Logger LOGGER = Logger.getLogger(FlowerComponentDAOimpl.class);
	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet componentResultSet;

	public List<FlowerComponent> getFlowerComponentsByBouquetId(Long id) {
		List<FlowerComponent> flowerComponentsByBouquetIdList = new ArrayList<FlowerComponent>();
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement("SELECT * FROM public.flower_component WHERE bouquet_id = ?;");
			pStatement.setLong(1, id);
			componentResultSet = pStatement.executeQuery();
			while (componentResultSet.next()) {
				FlowerComponent flowerComponentFromDatabase = new FlowerComponent();
				flowerComponentFromDatabase.setId(componentResultSet.getLong("id"));
				flowerComponentFromDatabase.setFlowerQuantyty(componentResultSet.getInt("flower_quantyty"));
				Flower flowerForComponent = new Flower();
				flowerForComponent.setId(componentResultSet.getLong("flower_id"));
				flowerComponentFromDatabase.setFlowerItem(flowerForComponent);
				flowerComponentFromDatabase.setBouquetId(id);
				flowerComponentsByBouquetIdList.add(flowerComponentFromDatabase);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (componentResultSet != null)
				try {
					componentResultSet.close();
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
		return flowerComponentsByBouquetIdList;
	}

	public boolean flowerComponentExists(FlowerComponent component) {
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection
					.prepareStatement("SELECT * FROM public.flower_component WHERE flower_id = ? AND bouquet_id = ?;");
			pStatement.setLong(1, component.getFlowerItem().getId());
			pStatement.setLong(2, component.getBouquetId());
			componentResultSet = pStatement.executeQuery();
			if (componentResultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (componentResultSet != null)
				try {
					componentResultSet.close();
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
		return "SELECT * FROM public.flower_component;";
	}

	@Override
	public String getSelectByIdQuery() {
		return "SELECT * FROM public.flower_component WHERE id = ?;";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO public.flower_component (flower_quantyty, flower_id, bouquet_id) VALUES (?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE public.flower_component SET flower_quantyty = ? WHERE flower_id = ? AND bouquet_id = ?;";
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM public.flower_component WHERE id = ?;";
	}

	@Override
	protected FlowerComponent parseResultSet(ResultSet rs) {
		FlowerComponent flowerComponentFromDatabase = new FlowerComponent();
		try {
			flowerComponentFromDatabase.setId(rs.getLong("id"));
			flowerComponentFromDatabase.setFlowerQuantyty(rs.getInt("flower_quantyty"));
			Flower flowerForComponent = new Flower();
			flowerForComponent.setId(rs.getLong("flower_id"));
			flowerComponentFromDatabase.setFlowerItem(flowerForComponent);
			flowerComponentFromDatabase.setBouquetId(rs.getLong("bouquet_id"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return flowerComponentFromDatabase;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement pStatement, FlowerComponent flowerComponent) {
		try {
			pStatement.setInt(1, flowerComponent.getFlowerQuantyty());
			pStatement.setLong(2, flowerComponent.getFlowerItem().getId());
			pStatement.setLong(3, flowerComponent.getBouquetId());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}

	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement pStatement, FlowerComponent flowerComponent) {
		try {
			pStatement.setInt(1, flowerComponent.getFlowerQuantyty());
			pStatement.setLong(2, flowerComponent.getFlowerItem().getId());
			pStatement.setLong(3, flowerComponent.getBouquetId());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}

	}

}
