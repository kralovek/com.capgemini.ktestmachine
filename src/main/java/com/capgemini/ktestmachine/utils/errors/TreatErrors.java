package com.capgemini.ktestmachine.utils.errors;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.exception.FunctionalException;
import com.capgemini.ktestmachine.exception.TechnicalException;

public final class TreatErrors {
	private static final Logger logger = Logger.getLogger(TreatErrors.class);

    private TreatErrors() {
        super();
    }

    private static final ABaseException findCauseException(final Throwable pException) {
        for (Throwable throwable = pException; throwable != null; throwable = throwable.getCause()) {
            if (throwable instanceof ABaseException) {
                return (ABaseException) throwable;
            }
        }
        return null;
    }

    public static final void treatException(final Throwable pThrowable) {
        final ABaseException aBaseException = findCauseException(pThrowable);
        if (aBaseException != null) {
            if (aBaseException instanceof ConfigurationException) {
            	logger.error("###############################");
            	logger.error("CONFIGURATION PROBLEM");
            	logger.error("");
            	logger.error("REASON: ");
            	logger.error(aBaseException.getMessage());
            	logger.error("###############################");
                return;
            } else if (aBaseException instanceof FunctionalException) {
            	logger.error("###############################");
            	logger.error("FUNCTIONAL PROBLEM");
            	logger.error("");
            	logger.error("REASON: ");
            	logger.error(aBaseException.getMessage());
            	logger.error("###############################");
                return;
            } else if (aBaseException instanceof TechnicalException) {
            	logger.error("###############################");
            	logger.error("TECHNICAL PROBLEM");
            	logger.error("");
            	logger.error("REASON: ");
            	logger.error(aBaseException.getMessage());
            	logger.error("");
            	logger.error("", aBaseException);
            	logger.error("###############################");
                return;
            }
        }

        logger.error("###############################");
        logger.error("UNEXPECTED PROBLEM");
        logger.error("");
        logger.error("REASON: ");
        logger.error(pThrowable.getMessage());
        logger.error("");
        logger.error("", pThrowable);
        logger.error("###############################");
    }

}
