package com.capgemini.ktestmachine.component.executant;

import com.capgemini.ktestmachine.exception.ABaseException;

/**
 * Executant
 *
 * @author KRALOVEC-99999
 */
public interface Executant {

    String execute(final String pSource) throws ABaseException;
}
