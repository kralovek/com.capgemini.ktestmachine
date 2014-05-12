package com.capgemini.ktestmachine.component.templateparser;


import java.util.Map;

import com.capgemini.ktestmachine.exception.ABaseException;

/**
 * TemplateParser
 *
 * @author KRALOVEC-99999
 */
public interface TemplateParser {
    String parse(final String pSource, final Map<String, Object> pParameters) throws ABaseException;
}
