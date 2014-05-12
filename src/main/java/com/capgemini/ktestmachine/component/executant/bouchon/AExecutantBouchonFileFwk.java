package com.capgemini.ktestmachine.component.executant.bouchon;


import java.io.File;

import com.capgemini.ktestmachine.exception.ABaseException;


/**
 * AExecutantBouchonFileFwk
 * 
 * @author KRALOVEC-99999
 */
public abstract class AExecutantBouchonFileFwk {
    private boolean configured;

	protected File file;

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
		buffer.append("    ").append("file").append("=").append(
				file == null ? "" : file.getAbsolutePath()).append("\n");
		return buffer.toString();
	}

	public File getFile() {
		return file;
	}

	public void setFile(final File pFileResponse) {
		this.file = pFileResponse;
	}
}
