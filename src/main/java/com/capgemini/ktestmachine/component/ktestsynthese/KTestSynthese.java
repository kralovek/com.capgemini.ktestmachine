package com.capgemini.ktestmachine.component.ktestsynthese;

import com.capgemini.ktestmachine.exception.ABaseException;

public interface KTestSynthese {
	void synthese(String source, String batchId) throws ABaseException;
	void synthese(String source) throws ABaseException;
}
