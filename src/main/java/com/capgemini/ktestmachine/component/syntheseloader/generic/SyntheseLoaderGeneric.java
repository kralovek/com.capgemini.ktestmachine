package com.capgemini.ktestmachine.component.syntheseloader.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.syntheseloader.SyntheseLoader;
import com.capgemini.ktestmachine.data.Synthese;
import com.capgemini.ktestmachine.data.Test;
import com.capgemini.ktestmachine.data.TestInput;
import com.capgemini.ktestmachine.data.TestResult;
import com.capgemini.ktestmachine.exception.ABaseException;

public class SyntheseLoaderGeneric extends SyntheseLoaderGenericFwk implements
		SyntheseLoader {
	private static final Logger LOGGER = Logger
			.getLogger(SyntheseLoaderGeneric.class);

	public List<Synthese> loadSyntheses(String source, String batchId)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			List<Synthese> retval = new ArrayList<Synthese>();

			List<TestInput> testInputs = testLoader.loadTests(source);

			List<TestResult> testResults = null;
			if (testReporter != null) {
				testResults = testReporter.loadResults(source, batchId);
			}

			for (TestInput testInput : testInputs) {
				SyntheseImpl synthese = new SyntheseImpl(testInput);
				if (testResults != null) {
					Boolean result = getTestResult(testInput, testResults);
					synthese.setResult(result);
				}
				retval.add(synthese);
			}

			LOGGER.trace("OK");
			return retval;
		} finally {
			LOGGER.trace("END");
		}
	}

	private Boolean getTestResult(Test test, List<TestResult> testResults) {
		for (TestResult testResult : testResults) {
			if (testResult.getId().equals(test.getId())) {
				return testResult.getResult();
			}
		}
		return null;
	}
}
