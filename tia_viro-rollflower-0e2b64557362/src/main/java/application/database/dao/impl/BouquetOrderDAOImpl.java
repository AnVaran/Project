package application.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import application.database.connection.DatabasePoolConnection;
import application.database.dao.BouquetOrderDAO;
import application.enums.OrderStatusEnum;
import application.model.BouquetOrder;

public class BouquetOrderDAOImpl extends AbstractDAOImpl<BouquetOrder> implements BouquetOrderDAO {
	private static final Logger LOGGER = Logger.getLogger(BouquetOrderDAOImpl.class);
	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet orderResultSet;

	public boolean orderExists(Long id) {
		try {
			connection = DatabasePoolConnection.getInstance().getConnection();
			pStatement = connection.prepareStatement("SELECT * FROM public.bouquet_order WHERE id = ?;");
			pStatement.setLong(1, id);
			orderResultSet = pStatement.executeQuery();
			if (orderResultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (orderResultSet != null)
				try {
					orderResultSet.close();
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
		return "SELECT * FROM public.bouquet_order;";
	}

	@Override
	public String getSelectByIdQuery() {
		return "SELECT * FROM public.bouquet_order WHERE id = ?;";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO public.bouquet_order (order_commentary, shipping_adress, order_status) VALUES (?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE public.bouquet_order SET order_commentary = ?, shipping_adress = ?, order_status = ? WHERE id = ?;";
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM public.bouquet_order WHERE id = ?;";
	}

	@Override
	protected BouquetOrder parseResultSet(ResultSet rs) {
		BouquetOrder orderFromDatabase = new BouquetOrder();
		try {
			orderFromDatabase.setId(rs.getLong("id"));
			orderFromDatabase.setOrderStatus(OrderStatusEnum.valueOf(rs.getString("order_status")));
			orderFromDatabase.setCommentary(rs.getString("order_commentary"));
			orderFromDatabase.setShippingAdres(rs.getString("shipping_adress"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return orderFromDatabase;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement pStatement, BouquetOrder order) {
		try {
			if (order.getCommentary() == null) {
				pStatement.setNull(1, Types.VARCHAR);
			} else {
				pStatement.setString(1, order.getCommentary());
			}
			if (order.getShippingAdres() == null) {
				pStatement.setNull(2, Types.VARCHAR);
			} else {
				pStatement.setString(2, order.getShippingAdres());
			}
			pStatement.setString(3, order.getOrderStatus().toString());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement pStatement, BouquetOrder order) {
		try {
			if (order.getCommentary() == null) {
				pStatement.setNull(1, Types.VARCHAR);
			} else {
				pStatement.setString(1, order.getCommentary());
			}
			if (order.getShippingAdres() == null) {
				pStatement.setNull(2, Types.VARCHAR);
			} else {
				pStatement.setString(2, order.getShippingAdres());
			}
			pStatement.setString(3, order.getOrderStatus().toString());
			pStatement.setLong(4, order.getId());
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}

	}

}
