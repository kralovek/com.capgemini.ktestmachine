package com.capgemini.ktestmachine.component.syntheseloader;

import java.util.List;

import com.capgemini.ktestmachine.data.Synthese;
import com.capgemini.ktestmachine.exception.ABaseException;

public interface SyntheseLoader {
	List<Synthese> loadSyntheses(String source, String batchId) throws ABaseException;
}
