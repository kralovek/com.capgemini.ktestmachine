package com.capgemini.ktestmachine.component.diffmanager.databasetrigger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.ktestmachine.component.datasource.DataSource;
import com.capgemini.ktestmachine.exception.ConfigurationException;


public abstract class ADiffManagerDatabaseTriggerFwk {
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	private boolean configured;
	protected String name;
	protected List<TableInfo> tableInfos;
	protected DataSource dataSource;

	private String dateFormat;
	protected DateFormat patternDate;

	public void config() throws ConfigurationException {
		configured = false;
		if (tableInfos == null) {
			tableInfos = new ArrayList<TableInfo>();
		}
		if (dateFormat != null) {
			patternDate = new SimpleDateFormat(dateFormat);
		} else {
			patternDate = new SimpleDateFormat(DATE_FORMAT);
		}
		if (name == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter name is not configured");
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TableInfo> getTableInfos() {
		return tableInfos;
	}

	public void setTableInfos(List<TableInfo> tableInfos) {
		this.tableInfos = tableInfos;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
