package com.capgemini.ktestmachine.component.resultparser.soap;

import com.capgemini.ktestmachine.component.resultparser.xml.ResultParserXml;
import com.capgemini.ktestmachine.exception.ABaseException;


public abstract class AResultParserSoapFwk extends ResultParserXml {
    private boolean configured;
    
	public void config() throws ABaseException {
		configured = false;
		super.config();
		configured = true;
	}

	public void testConfigured() {
		super.testConfigured();
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
		}
	}
}
