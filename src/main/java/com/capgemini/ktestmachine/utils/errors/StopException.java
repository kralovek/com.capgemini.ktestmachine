package com.capgemini.ktestmachine.utils.errors;

import com.capgemini.ktestmachine.exception.ABaseException;

public class StopException extends ABaseException {

	public StopException() {
		super();
	}

	public StopException(String message, Throwable cause) {
		super(message, cause);
	}

	public StopException(String message) {
		super(message);
	}

	public StopException(Throwable cause) {
		super(cause);
	}

}
