package com.capgemini.ktestmachine.component.templatearchiv;

import com.capgemini.ktestmachine.exception.ABaseException;

/**
 * TemplateArchiv
 *
 * @author KRALOVEC-99999
 */
public interface TemplateArchiv {

    public String loadTemplate(final String pCode) throws ABaseException;
}
