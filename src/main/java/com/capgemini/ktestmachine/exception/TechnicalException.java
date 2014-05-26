package com.capgemini.ktestmachine.exception;

public class TechnicalException extends ABaseException {

	public TechnicalException() {
		super();
	}

	public TechnicalException(String message, Throwable cause) {
		super(message, cause);
	}

	public TechnicalException(String message) {
		super(message);
	}

	public TechnicalException(Throwable cause) {
		super(cause);
	}
}
