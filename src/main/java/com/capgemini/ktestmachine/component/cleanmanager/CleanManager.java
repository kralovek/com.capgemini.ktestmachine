package com.capgemini.ktestmachine.component.cleanmanager;

import java.util.List;

import com.capgemini.ktestmachine.exception.ABaseException;

public interface CleanManager {

	String getName();

	static interface Group {
		String getName();
		List<String> getPatterns();
	}
	
	void clean(List<Group> groups) throws ABaseException;
}
