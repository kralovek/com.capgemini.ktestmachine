package com.capgemini.ktestmachine.component.filemanager.local;

import com.capgemini.ktestmachine.exception.ConfigurationException;



public abstract class AFileManagerLocalFwk {
	private boolean configured;

	protected String traceDataFile;

	public void config() throws ConfigurationException {
		configured = false;
		if (traceDataFile == null) {
			// OK
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append("[").append(this.getClass().getName()).append("]\n");
		return buffer.toString();
	}

	public String getTraceDataFile() {
		return traceDataFile;
	}

	public void setTraceDataFile(String traceDataFile) {
		this.traceDataFile = traceDataFile;
	}
}