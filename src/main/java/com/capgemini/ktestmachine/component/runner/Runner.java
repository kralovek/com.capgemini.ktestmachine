package com.capgemini.ktestmachine.component.runner;

import java.util.Map;

import com.capgemini.ktestmachine.exception.ABaseException;


public interface Runner {
	Map<String, Object> run(Map<String, Object> parameters) throws ABaseException;
}
