package com.capgemini.ktestmachine.component.executant.bouchon;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.executant.Executant;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.FunctionalException;
import com.capgemini.ktestmachine.exception.TechnicalException;

/**
 * ExecutantBouchonFile
 * 
 * @author KRALOVEC-99999
 */
public class ExecutantBouchonFile extends AExecutantBouchonFileFwk implements Executant {
	private static final Logger LOGGER = Logger
			.getLogger(ExecutantBouchonFile.class);

	public String execute(final String pSource) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();

			String contentXml = loadContentFromFile(file);
			LOGGER.trace("OK");
			return contentXml;
		} finally {
			LOGGER.trace("END");
		}
	}

	private String loadContentFromFile(final File pFile) throws ABaseException {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(pFile);
			char[] buffer = new char[(int) pFile.length()];
			fileReader.read(buffer);
			String content = new String(buffer);
			fileReader.close();
			fileReader = null;
			return content;
		} catch (final FileNotFoundException ex) {
			throw new FunctionalException("Result file does not exist: "
					+ pFile.getAbsolutePath());
		} catch (final IOException ex) {
			throw new TechnicalException("Cannot read the Result file: "
					+ pFile.getAbsolutePath());
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (final IOException ex) {
				}
			}
		}
	}
}
