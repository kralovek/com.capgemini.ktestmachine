package com.capgemini.ktestmachine.component.ktestsynthese;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.exception.ABaseException;

public class KTestSyntheseGeneric extends KTestSyntheseGenericFwk implements
		KTestSynthese {
	private static final Logger LOGGER = Logger
			.getLogger(KTestSyntheseGeneric.class);

	public void synthese(String source) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}
}
