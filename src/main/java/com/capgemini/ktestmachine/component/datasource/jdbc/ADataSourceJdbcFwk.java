package com.capgemini.ktestmachine.component.datasource.jdbc;

import com.capgemini.ktestmachine.exception.ConfigurationException;



public abstract class ADataSourceJdbcFwk {
    private boolean configured;

	protected String driver;
	protected String user;
	protected String password;
	protected String url;
	
	public void config() throws ConfigurationException {
		configured = false;
		if (driver == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter driver is not configured");
		}
		if (user == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter user is not configured");
		}
		if (password == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter password is not configured");
		}
		if (url == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter url is not configured");
		}
		configured = true;
	}
	
	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
		}
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
