package com.capgemini.ktestmachine.component.locker.empty;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.locker.Locker;
import com.capgemini.ktestmachine.exception.ABaseException;


public class LockerEmpty implements Locker {
	private static final Logger LOGGER = Logger.getLogger(LockerEmpty.class);

	public void lock() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			LOGGER.debug("LOCK IS NOT IMPLEMENTED");
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	public void unlock() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			LOGGER.debug("LOCK IS NOT IMPLEMENTED");
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	
}
