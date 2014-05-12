package com.capgemini.ktestmachine.component.testreporter;

import java.util.List;

import com.capgemini.ktestmachine.data.TestOutput;
import com.capgemini.ktestmachine.data.TestResult;
import com.capgemini.ktestmachine.exception.ABaseException;

public interface TestReporter {
	boolean reportTest(TestOutput testOutput, String batchID)
			throws ABaseException;

	List<TestResult> loadResults(String source, String batchID)
			throws ABaseException;
}
