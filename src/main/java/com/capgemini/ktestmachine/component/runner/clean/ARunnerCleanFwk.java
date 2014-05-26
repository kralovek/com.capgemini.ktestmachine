package com.capgemini.ktestmachine.component.runner.clean;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.ktestmachine.component.cleanmanager.CleanManager;
import com.capgemini.ktestmachine.exception.ConfigurationException;


public abstract class ARunnerCleanFwk {
	private boolean configured;

	protected List<CleanManager> cleanManagers;
    protected String sysParamPrefix;

    public void config() throws ConfigurationException {
		configured = false;
		if (cleanManagers == null) {
			cleanManagers = new ArrayList<CleanManager>();
		}
		if (sysParamPrefix == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter sysParamPrefix is not configured");
		} else if (!sysParamPrefix.isEmpty() && !sysParamPrefix.endsWith("/")) {
			sysParamPrefix += "/";
		}
		configured = true;
    }
    
	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
		}
	}

	public String getSysParamPrefix() {
		return sysParamPrefix;
	}

	public void setSysParamPrefix(String sysParamPrefix) {
		this.sysParamPrefix = sysParamPrefix;
	}

	public List<CleanManager> getCleanManagers() {
		return cleanManagers;
	}

	public void setCleanManagers(List<CleanManager> cleanManagers) {
		this.cleanManagers = cleanManagers;
	}
}
