package com.capgemini.ktestmachine.component.locker.database;

import com.capgemini.ktestmachine.component.datasource.DataSource;
import com.capgemini.ktestmachine.exception.ConfigurationException;


public abstract class ALockerDatabaseFwk {

	private boolean configured;
	
	protected String whoami;
	protected DataSource dataSource;
	protected Long waitMax;
	protected Long waitInterval;

	public void config() throws ConfigurationException {
		configured = false;
		if (whoami == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter whoami is not configured");
		} else if (whoami.length() > 10) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter whoami can have max 10 characters");
		}
		if (dataSource == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter dataSource is not configured");
		}
		if (waitMax == null) {
			// OK
		}
		if (waitInterval == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter waitInterval is not configured");
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getWhoami() {
		return whoami;
	}

	public void setWhoami(String whoami) {
		this.whoami = whoami;
	}

	public Long getWaitMax() {
		return waitMax;
	}

	public void setWaitMax(Long waitMax) {
		this.waitMax = waitMax;
	}

	public Long getWaitInterval() {
		return waitInterval;
	}

	public void setWaitInterval(Long waitInterval) {
		this.waitInterval = waitInterval;
	}
}
