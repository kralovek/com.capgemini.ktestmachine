package com.capgemini.ktestmachine.component.lancer;

import com.capgemini.ktestmachine.data.TestInput;
import com.capgemini.ktestmachine.data.TestOutput;
import com.capgemini.ktestmachine.exception.ABaseException;


/**
 * Lancer
 *
 * @author KRALOVEC-99999
 */
public interface Lancer {

	TestOutput lance(TestInput testUnit) throws ABaseException;
}
