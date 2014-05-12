package com.capgemini.ktestmachine.component.ktestsynthese;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.ktestmachine.component.syntheseloader.SyntheseLoader;
import com.capgemini.ktestmachine.component.synthesereporter.SyntheseReporter;
import com.capgemini.ktestmachine.exception.ConfigurationException;

public abstract class KTestSyntheseGenericFwk {
	private boolean configured;

	protected SyntheseLoader syntheseLoader;
	private SyntheseReporter syntheseReporter;
	protected List<SyntheseReporter> syntheseReporters;

	public void config() throws ConfigurationException {
		configured = false;
		if (syntheseLoader == null) {
			throw new ConfigurationException(
					"Parameter 'syntheseLoader' is not configured");
		}
		if (syntheseReporters == null) {
			syntheseReporters = new ArrayList<SyntheseReporter>();
		}
		if (syntheseReporter != null) {
			syntheseReporters.add(syntheseReporter);
		}
		if (syntheseReporters.size() == 0) {
			throw new ConfigurationException(
					"No 'syntheseReporter' of 'syntheseReporters' is not configured");
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": The componet is not configured.");
		}
	}

	public SyntheseLoader getSyntheseLoader() {
		return syntheseLoader;
	}

	public void setSyntheseLoader(SyntheseLoader syntheseLoader) {
		this.syntheseLoader = syntheseLoader;
	}

	public SyntheseReporter getSyntheseReporter() {
		return syntheseReporter;
	}

	public void setSyntheseReporter(SyntheseReporter syntheseReporter) {
		this.syntheseReporter = syntheseReporter;
	}

	public List<SyntheseReporter> getSyntheseReporters() {
		return syntheseReporters;
	}

	public void setSyntheseReporters(List<SyntheseReporter> syntheseReporters) {
		this.syntheseReporters = syntheseReporters;
	}
}
