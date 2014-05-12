package com.capgemini.ktestmachine.component.datasource.xml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.datasource.DataSource;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;


public class DataSourceXml extends ADataSourceXmlFwk implements DataSource {
	private static final Logger LOGGER = Logger.getLogger(DataSourceXml.class);

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
