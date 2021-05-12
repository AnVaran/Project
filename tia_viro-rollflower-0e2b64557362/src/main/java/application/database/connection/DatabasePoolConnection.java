package application.database.connection;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DatabasePoolConnection {
	private static final Logger LOGGER = Logger.getLogger(DatabasePoolConnection.class);
	private static DatabasePoolConnection datasource;
	private ComboPooledDataSource cpds;

	private DatabasePoolConnection() {
		try {
			Properties properties =new Properties (); 
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(properties.getProperty("driver"));
			cpds.setJdbcUrl(properties.getProperty("URL"));
			cpds.setUser(properties.getProperty("username"));
			cpds.setPassword(properties.getProperty("password"));
			cpds.setMinPoolSize(5);
			cpds.setInitialPoolSize(5);;
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(20);
			cpds.setMaxStatements(180);
		} catch (PropertyVetoException e) {
			LOGGER.error(e.getMessage());
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

	}

	public static DatabasePoolConnection getInstance() {
		if (datasource == null) {
			datasource = new DatabasePoolConnection();
			return datasource;
		} else {
			return datasource;
		}
	}

	public Connection getConnection() throws SQLException {
		return this.cpds.getConnection();
	}
}
