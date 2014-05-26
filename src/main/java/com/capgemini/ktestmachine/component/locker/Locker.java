package com.capgemini.ktestmachine.component.locker;

import com.capgemini.ktestmachine.exception.ABaseException;

public interface Locker {

	void lock() throws ABaseException;
	
	void unlock() throws ABaseException;
}
