package com.capgemini.ktestmachine.component.diffmanager.databasetrigger;

import java.util.List;

import com.capgemini.ktestmachine.exception.ConfigurationException;

public class ATableInfoFwk {

	private boolean configured;

	protected String ktuSchema;
	protected String ktuName;
	private String ktuNamePrefix;

	protected String schema;
	protected String name;
	protected List<String> columnsPK;

	protected Boolean joinStatus;
	
	public void config() throws ConfigurationException {
		configured = false;
		if (schema == null) {
			// OK
		}
		if (name == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter name is not configured");
		}
		if (ktuSchema == null) {
			ktuSchema = schema;
		}
		if (ktuNamePrefix != null) {
			ktuName = ktuNamePrefix + name;
		} else {
			ktuName = name;
		}
		if (name == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter name is not configured");
		}
		if (columnsPK == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter columnsPK is not configured");
		} else if (columnsPK.size() == 0) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter-list columnsPK may not be empty");
		}
		if (joinStatus == null) {
			joinStatus = false;
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configuré.");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getColumnsPK() {
		return columnsPK;
	}

	public void setColumnsPK(List<String> columnsPK) {
		this.columnsPK = columnsPK;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getKtuSchema() {
		return ktuSchema;
	}

	public void setKtuSchema(String ktuSchema) {
		this.ktuSchema = ktuSchema;
	}

	public String getKtuNamePrefix() {
		return ktuNamePrefix;
	}

	public void setKtuNamePrefix(String ktuNamePrefix) {
		this.ktuNamePrefix = ktuNamePrefix;
	}

	public boolean getJoinStatus() {
		return joinStatus;
	}

	public void setJoinStatus(boolean joinStatus) {
		this.joinStatus = joinStatus;
	}
}
