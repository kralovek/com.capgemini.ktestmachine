package com.capgemini.ktestmachine.component.datasource;

import java.sql.Connection;

import com.capgemini.ktestmachine.exception.ABaseException;


public interface DataSource {
	  Connection getConnection() throws ABaseException;
}
