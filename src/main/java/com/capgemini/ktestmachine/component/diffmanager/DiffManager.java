package com.capgemini.ktestmachine.component.diffmanager;

import java.util.List;
import java.util.Map;

import com.capgemini.ktestmachine.exception.ABaseException;


public interface DiffManager {

	enum Status {
		NEW, UPD, DEL, UNK
	}
	
	interface Item {
		String getName();
		long getIndex();
		Status getStatus();
		Map<String, String> getParameters();
	}

	interface Group {
		String getName();
		long getLastIndex();
		List<Item> getItems();
	}

	String getName();
	
	List<Group> loadDiffs(List<Group> groupStates) throws ABaseException;

	List<Group> loadCurrents() throws ABaseException;
}
