package com.capgemini.ktestmachine.component.testloader;

import java.util.List;

import com.capgemini.ktestmachine.data.TestInput;
import com.capgemini.ktestmachine.exception.ABaseException;

public interface TestLoader {
	List<TestInput> loadTests(String source) throws ABaseException;
}
