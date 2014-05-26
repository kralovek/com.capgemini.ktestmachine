package com.capgemini.ktestmachine.component.templateparser.impl;

import com.capgemini.ktestmachine.exception.ABaseException;



/**
 * ATemplateParserImplFwk
 * 
 * @author KRALOVEC-99999
 */
public abstract class ATemplateParserImplFwk {
    private boolean configured;

	public void config() throws ABaseException {
		configured = false;
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
		}
	}

	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(this.getClass().getName()).append("]\n");
		return buffer.toString();
	}
}
