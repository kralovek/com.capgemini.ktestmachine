package com.capgemini.ktestmachine.main;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.ktestmachine.exception.ConfigurationException;

public class ConfigKTestMachine {
	private String excel;
	private String config;
	private List<String> properties = new ArrayList<String>();

	public ConfigKTestMachine(final String[] pArgs)
			throws ConfigurationException {
		init(pArgs);
	}

	public String getExcel() {
		return excel;
	}

	public String getConfig() {
		return config;
	}

	public List<String> getProperties() {
		return properties;
	}

	protected void init(final String[] pArgs) throws ConfigurationException {
		if (pArgs.length % 2 != 0) {
			throw new ConfigurationException(
					"A command-line parameter is missing a value");
		}
		for (int i = 0; i < pArgs.length; i += 2) {
			if (pArgs[i + 1] == null || pArgs[i + 1].isEmpty()) {
				throw new ConfigurationException("Value of the parameter '"
						+ pArgs[i] + "' is empty");
			}
			if ("-config".equals(pArgs[i])) {
				config = pArgs[i + 1];
			} else if ("-properties".equals(pArgs[i])) {
				String parProperties = pArgs[i + 1];
				String[] arrProperties = parProperties.split("\\|");
				for (int j = 0; j < arrProperties.length; j++) {
					arrProperties[j] = arrProperties[j].trim();
					if (arrProperties[j].isEmpty()) {
						throw new ConfigurationException(
								"Parameter -properties must contain a list of ressources separated by |");
					}
					properties.add(arrProperties[j]);
				}
			} else if ("-excel".equals(pArgs[i])) {
				excel = pArgs[i + 1];
			}
		}

		if (config == null) {
			throw new ConfigurationException("Config file is not specified");
		}
		if (excel == null) {
			throw new ConfigurationException("Excel file is not specified");
		}
		if (properties == null) {

		}
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("CONFIG: ").append("\n")
				.append("-excel: ").append(excel).append("\n")
				.append("-config: ").append(config).append("\n")
				.append("-properties: ");

		for (String oneProperties : properties) {
			buffer.append("\n\t").append(oneProperties);
		}
		return buffer.toString();
	}
}
