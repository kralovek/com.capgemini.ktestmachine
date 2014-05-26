package com.capgemini.ktestmachine.component.resultparser;


import java.util.Map;

import com.capgemini.ktestmachine.exception.ABaseException;

/**
 * ResultParser
 *
 * @author KRALOVEC-99999
 */
public interface ResultParser {
    Map<String, Object> parse(final String pSource) throws ABaseException;
}
