package com.capgemini.ktestmachine.main;

import com.capgemini.ktestmachine.exception.ConfigurationException;

public class ConfigKTestSynthese extends ConfigKTestMachine {

	private String batchId;
	
	public ConfigKTestSynthese(String[] args)
			throws ConfigurationException {
		super(args);
		init(args);
	}
	

	public String getBatchId() {
		return batchId;
	}

	protected void init(final String[] pArgs) throws ConfigurationException {
		super.init(pArgs);
		for (int i = 0; i < pArgs.length; i += 2) {
			if ("-batchid".equals(pArgs[i])) {
				batchId = pArgs[i + 1];
			}
		}
		if (batchId.isEmpty()) {
			batchId = null;
		}
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer(toString())
				.append("-batchid: ").append(batchId);
		return buffer.toString();
	}

}
