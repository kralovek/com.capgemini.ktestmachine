package com.capgemini.ktestmachine.component.synthesereporter;

import java.util.List;

import com.capgemini.ktestmachine.data.Synthese;
import com.capgemini.ktestmachine.exception.ABaseException;

public interface SyntheseReporter {
	void reportSyntheses(String source, List<Synthese> syntheses) throws ABaseException;
}
