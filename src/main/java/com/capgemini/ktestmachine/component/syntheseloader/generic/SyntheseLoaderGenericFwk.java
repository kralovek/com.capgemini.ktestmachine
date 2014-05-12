package com.capgemini.ktestmachine.component.syntheseloader.generic;

import com.capgemini.ktestmachine.component.testloader.TestLoader;
import com.capgemini.ktestmachine.component.testreporter.TestReporter;
import com.capgemini.ktestmachine.exception.ConfigurationException;

public abstract class SyntheseLoaderGenericFwk {
	private boolean configured;

	protected TestLoader testLoader;
	protected TestReporter testReporter;

	public void config() throws ConfigurationException {
		configured = false;

		if (testLoader == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'testLoader' is not configured");
		}

		if (testReporter == null) {
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": The componet is not configured.");
		}
	}

	public TestLoader getTestLoader() {
		return testLoader;
	}

	public void setTestLoader(TestLoader testLoader) {
		this.testLoader = testLoader;
	}

	public TestReporter getTestReporter() {
		return testReporter;
	}

	public void setTestReporter(TestReporter testReporter) {
		this.testReporter = testReporter;
	}
}
