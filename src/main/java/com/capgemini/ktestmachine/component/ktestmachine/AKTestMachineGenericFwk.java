package com.capgemini.ktestmachine.component.ktestmachine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.capgemini.ktestmachine.component.lancer.Lancer;
import com.capgemini.ktestmachine.component.testloader.TestLoader;
import com.capgemini.ktestmachine.component.testreporter.TestReporter;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.parameters.UtilsParameters;


/**
 * AKTestMachineGenericFwk
 * 
 * @author KRALOVEC-99999
 */
public abstract class AKTestMachineGenericFwk {
	private boolean configured;

	protected String sysParamPrefix;

	protected File stopFile = new File("STOP");

	protected TestLoader testLoader;

	protected TestReporter testReporter;

	protected Lancer lancer;
	private Map<String, Lancer> lancerByType;
	protected Map<Pattern, Lancer> lancerByTypePattern;

	private String help;

	public void config() throws ABaseException {
		configured = false;
		if (testLoader == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": The parameter testLoader is not configured");
		}
		if (testReporter == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": The parameter testReporter is not configured");
		}
		if (lancerByType == null) {
			lancerByType = new HashMap<String, Lancer>();
		}
		if (lancer == null && lancerByType.isEmpty()) {
			throw new ConfigurationException(
					getClass().getSimpleName()
							+ ": Neither the parameter lancer nor lancerByType is configured");
		}
		lancerByTypePattern = UtilsParameters.toByTypePattern(getClass(), lancerByType);
		
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
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
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

	public Lancer getLancer() {
		return lancer;
	}

	public void setLancer(Lancer lancer) {
		this.lancer = lancer;
	}

	public Map<String, Lancer> getLancerByType() {
		return lancerByType;
	}

	public void setLancerByType(Map<String, Lancer> lancerByType) {
		this.lancerByType = lancerByType;
	}

	public File getStopFile() {
		return stopFile;
	}

	public void setStopFile(File stopFile) {
		this.stopFile = stopFile;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getSysParamPrefix() {
		return sysParamPrefix;
	}

	public void setSysParamPrefix(String sysParamPrefix) {
		this.sysParamPrefix = sysParamPrefix;
	}
}
