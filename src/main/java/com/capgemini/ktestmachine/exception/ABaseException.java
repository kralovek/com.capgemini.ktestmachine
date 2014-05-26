package com.capgemini.ktestmachine.exception;

public abstract class ABaseException extends Exception {
	private boolean knowing = false;

	public ABaseException() {
		super();
	}

	public ABaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ABaseException(String message) {
		super(message);
	}

	public ABaseException(Throwable cause) {
		super(cause);
	}

	public boolean isKnowing() {
		return knowing;
	}

	public void setKnowing(boolean knowing) {
		this.knowing = knowing;
	}
}
