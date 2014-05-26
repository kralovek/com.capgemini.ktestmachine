package com.capgemini.ktestmachine.component.executant.xstream;

import java.util.HashMap;
import java.util.Map;

import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;



public abstract class AExecutantXStreamFwk {
    private boolean configured;

	protected Map<String, Object> instances;

	public void config() throws ABaseException {
		configured = false;
		if (instances != null) {
			for (final Map.Entry<String, Object> entry : instances.entrySet()) {
				if (entry.getValue() == null) {
					throw new ConfigurationException(getClass().getSimpleName()
							+ ": The instance is null: " + entry.getKey());
				}
			}
		} else {
			instances = new HashMap<String, Object>();
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
		}
	}

	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(this.getClass().getName()).append("]\n");
		buffer.append("    ").append("instances").append(":").append("\n");
		if (instances != null) {
			for (final Map.Entry<String, Object> entry : instances.entrySet()) {
				buffer.append("    ").append("    ").append(entry.getKey())
						.append(" -> ").append(
								entry.getValue().getClass().getName()).append(
								"\n");
			}
		}
		return buffer.toString();
	}

	public Map<String, Object> getInstances() {
		return instances;
	}

	public void setInstances(final Map<String, Object> instances) {
		this.instances = instances;
	}
}
