package com.capgemini.ktestmachine.component.runner.shellcommand;

import com.capgemini.ktestmachine.component.templateparser.TemplateParser;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;


public abstract class ARunnerShellCommandFwk {
	private boolean configured;
	protected String command;

	protected TemplateParser templateParser;

	protected String sysParamPrefix;

	public void config() throws ABaseException {
		configured = false;
		if (templateParser == null) {
			// OK
		}
		if (command == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter command is not configured");
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
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public TemplateParser getTemplateParser() {
		return templateParser;
	}

	public void setTemplateParser(TemplateParser templateParser) {
		this.templateParser = templateParser;
	}

	public String getSysParamPrefix() {
		return sysParamPrefix;
	}

	public void setSysParamPrefix(String sysParamPrefix) {
		this.sysParamPrefix = sysParamPrefix;
	}
}
