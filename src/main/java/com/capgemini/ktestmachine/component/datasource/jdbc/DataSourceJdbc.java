package com.capgemini.ktestmachine.component.datasource.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.capgemini.ktestmachine.component.datasource.DataSource;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;


public class DataSourceJdbc extends ADataSourceJdbcFwk implements DataSource {
	private static Log LOGGER = LogFactory.getLog(DataSourceJdbc.class);

	public Connection getConnection() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException ex) {
				throw new TechnicalException(
						"Impossible de trouver charger la class du db driver: "
								+ driver);
			}

			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, user, password);
			} catch (SQLException ex) {
				throw new TechnicalException(
						"Impossible de se connecter vers la base de donn�e: "
								+ url + ", " + user, ex);
			}
			LOGGER.trace("OK");
			return connection;
		} finally {
			LOGGER.trace("END");
		}
	}
}
