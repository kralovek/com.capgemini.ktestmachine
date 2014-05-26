package com.capgemini.ktestmachine.component.templatearchiv.impl;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.templatearchiv.TemplateArchiv;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.exception.TechnicalException;

public class TemplateArchivImpl extends ATemplateArchivImplFwk implements TemplateArchiv {
	private static final Logger LOGGER = Logger
			.getLogger(TemplateArchivImpl.class);

	public String loadTemplate(final String pCode) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			File fileTemplate = null;
			for (File dir : dirs) {
				fileTemplate = createFilepath(dir, pCode);
				if (fileTemplate.isFile()) {
					break;
				} else {
					fileTemplate = null;
				}
			}
			if (fileTemplate == null) {
				throw new ConfigurationException("No template found for the code: " + pCode);
			}
			LOGGER.debug("Loading the template: " + fileTemplate);
			String retval = loadContent(fileTemplate);
			LOGGER.trace("OK");
			return retval;
		} finally {
			LOGGER.trace("END");
		}
	}

	private String loadContent(final File pFile) throws ABaseException {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(pFile);
			final char[] buffer = new char[(int) pFile.length()];
			fileReader.read(buffer);
			final String content = new String(buffer);
			fileReader.close();
			fileReader = null;
			return content;
		} catch (final FileNotFoundException ex) {
			throw new ConfigurationException("Template file does not exist: "
					+ pFile.getAbsolutePath());
		} catch (final IOException ex) {
			throw new TechnicalException("Cannot read the template file: "
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

	private File createFilepath(final File dir, final String pCode) {
		if (mapping != null) {
			for (final Map.Entry<String, String> entry : mapping.entrySet()) {
				if (pCode.equals(entry.getKey())) {
					return new File(dir, entry.getValue());
				}
			}
		}
		return new File(dir, pCode + ".tpl");
	}
}
