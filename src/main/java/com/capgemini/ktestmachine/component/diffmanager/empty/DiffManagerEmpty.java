package com.capgemini.ktestmachine.component.diffmanager.empty;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager;
import com.capgemini.ktestmachine.exception.ABaseException;


public class DiffManagerEmpty implements DiffManager {
	private static final Logger LOGGER = Logger
			.getLogger(DiffManagerEmpty.class);

	public String getName() {
		return "EMPTY";
	}

	public List<Group> loadDiffs(List<Group> groupStates) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			List<Group> retval = new ArrayList<DiffManager.Group>();
			LOGGER.trace("OK");
			return retval;
		} finally {
			LOGGER.trace("END");
		}
	}

	public List<Group> loadCurrents() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			List<Group> retval = new ArrayList<DiffManager.Group>();
			LOGGER.trace("OK");
			return retval;
		} finally {
			LOGGER.trace("END");
		}
	}
}
