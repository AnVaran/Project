package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.FlowerBouquetDAO;
import application.model.Decoration;
import application.model.FlowerBouquet;
import application.model.Wrapper;

public class FlowerBouquetDAOImpl extends AbstractDAOImpl<FlowerBouquet> implements FlowerBouquetDAO {
	private static final Logger LOGGER = Logger.getLogger(FlowerBouquetDAOImpl.class);
	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet bouquetResultSet;

	public List<FlowerBouquet> getBouquetsByOrderId(long id) {
		List<FlowerBouquet> bouquetList = new ArrayList<FlowerBouquet>();
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement("SELECT * FROM public.flower_bouquet WHERE order_id = ?;");
			pStatement.setLong(1, id);
			bouquetResultSet = pStatement.executeQuery();
			while (bouquetResultSet.next()) {
				FlowerBouquet bouquetFromDatabase = new FlowerBouquet();
				bouquetFromDatabase.setId(bouquetResultSet.getLong("id"));
				bouquetFromDatabase.setPrice(bouquetResultSet.getFloat("bouquet_price"));
				Long decoId = bouquetResultSet.getLong("decoration_id");
				if (decoId == 0) {
					bouquetFromDatabase.setDecorationComponent(null);
				} else {
					Decoration deco = new Decoration();
					deco.setId(decoId);
					bouquetFromDatabase.setDecorationComponent(deco);
				}
				Long wrapperId = bouquetResultSet.getLong("wrapper_id");
				if (wrapperId == 0) {
					bouquetFromDatabase.setWrapperComponent(null);
				} else {
					Wrapper wrap = new Wrapper();
					wrap.setId(wrapperId);
					bouquetFromDatabase.setWrapperComponent(wrap);
				}
				bouquetFromDatabase.setOrderId(id);
				bouquetList.add(bouquetFromDatabase);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (bouquetResultSet != null)
				try {
					bouquetResultSet.close();
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
		return bouquetList;
	}

	public boolean bouquetExists(long id) {
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement("SELECT * FROM public.flower_bouquet WHERE id = ?;");
			pStatement.setLong(1, id);
			bouquetResultSet = pStatement.executeQuery();
			if (bouquetResultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (bouquetResultSet != null)
				try {
					bouquetResultSet.close();
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
		return "SELECT * FROM public.flower_bouquet;";
	}

	@Override
	public String getSelectByIdQuery() {
		return "SELECT * FROM public.flower_bouquet WHERE id = ?;";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO public.flower_bouquet (wrapper_id, decoration_id, order_id, bouquet_price) VALUES (?, ?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE public.flower_bouquet SET decoration_id = ?, wrapper_id = ?, bouquet_price = ? WHERE id = ?;";
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM public.flower_bouquet WHERE id = ?;";
	}

	@Override
	protected FlowerBouquet parseResultSet(ResultSet rs) {
		FlowerBouquet bouquetFromDatabase = new FlowerBouquet();
		try {
			bouquetFromDatabase.setId(rs.getLong("id"));
			bouquetFromDatabase.setPrice(rs.getFloat("bouquet_price"));
			Long decoId = rs.getLong("decoration_id");
			if (decoId == 0 || decoId == null) {
				bouquetFromDatabase.setDecorationComponent(null);
			} else {
				Decoration deco = new Decoration();
				deco.setId(decoId);
				bouquetFromDatabase.setDecorationComponent(deco);
			}
			Long wrapperId = rs.getLong("wrapper_id");
			if (wrapperId == 0) {
				bouquetFromDatabase.setWrapperComponent(null);
			} else {
				Wrapper wrap = new Wrapper();
				wrap.setId(wrapperId);
				bouquetFromDatabase.setWrapperComponent(wrap);
			}
			bouquetFromDatabase.setOrderId(rs.getLong("order_id"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return bouquetFromDatabase;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement pStatement, FlowerBouquet bouquet) {
		try {
			if (bouquet.getWrapperComponent() == null) {
				pStatement.setNull(1, Types.INTEGER);
			} else {
				pStatement.setLong(1, bouquet.getWrapperComponent().getId());
			}
			if (bouquet.getDecorationComponent() == null) {
				pStatement.setNull(2, Types.INTEGER);
			} else {
				pStatement.setLong(2, bouquet.getDecorationComponent().getId());
			}
			pStatement.setLong(3, bouquet.getOrderId());
			if (bouquet.getPrice() == null) {
				pStatement.setNull(4, Types.NUMERIC);
			} else {
				pStatement.setFloat(4, bouquet.getPrice());
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement pStatement, FlowerBouquet bouquet) {
		try {
			if (bouquet.getDecorationComponent() == null) {
				pStatement.setNull(1, Types.INTEGER);
			} else {
				pStatement.setLong(1, bouquet.getDecorationComponent().getId());
			}
			if (bouquet.getWrapperComponent() == null) {
				pStatement.setNull(2, Types.INTEGER);
			} else {
				pStatement.setLong(2, bouquet.getWrapperComponent().getId());
			}
			if (bouquet.getPrice() == null) {
				pStatement.setNull(3, Types.NUMERIC);
			} else {
				pStatement.setFloat(3, bouquet.getPrice());
			}
			pStatement.setLong(4, bouquet.getId());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}
}
