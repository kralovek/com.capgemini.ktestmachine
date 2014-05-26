package com.capgemini.ktestmachine.component.runner.shellcommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.runner.Runner;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.capgemini.ktestmachine.utils.file.UtilsFile;

public class RunnerShellCommand extends ARunnerShellCommandFwk implements
		Runner {
	private static final Logger LOGGER = Logger
			.getLogger(RunnerShellCommand.class);

	private static final String PARAM_STDOUT = "STDOUT";
	private static final String PARAM_STDERR = "STDERR";
	private static final String PARAM_COMMAND = "COMMAND";

	public Map<String, Object> run(Map<String, Object> parameters)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();

			Map<String, Object> retval = new HashMap<String, Object>();

			String commandParsed;
			if (templateParser != null) {
				commandParsed = templateParser.parse(command, parameters);
			} else {
				commandParsed = command;
			}

			retval.put(sysParamPrefix + PARAM_COMMAND, commandParsed);
			
			LOGGER.info("\tStarting execute shell: " + commandParsed);
			try {
				Process process = Runtime.getRuntime().exec(commandParsed);
				String outputStdErr = getOutput(process.getErrorStream());
				retval.put(sysParamPrefix + PARAM_STDERR, outputStdErr);
				LOGGER.debug("\tStdErr: \n" + outputStdErr);
				String outputStdOut = getOutput(process.getInputStream());
				retval.put(sysParamPrefix + PARAM_STDOUT, outputStdOut);
				LOGGER.debug("\tStdOut: \n" + outputStdOut);
				int retcode = process.waitFor();
				if (retcode != 0) {
					throw new TechnicalException(
							"The command was executed with error: " + retcode
									+ " command: " + commandParsed);
				}

				LOGGER.info("\tRunner SQL finished OK");
			} catch (InterruptedException ex) {
				LOGGER.info("\tRunner SQL finished ERROR: " + ex.getMessage());
				throw new TechnicalException("Cannot execute the command: "
						+ commandParsed, ex);
			} catch (IOException ex) {
				LOGGER.info("\tRunner SQL finished ERROR: " + ex.getMessage());
				throw new TechnicalException("Cannot execute the command: "
						+ commandParsed, ex);
			}

			LOGGER.trace("OK");
			return retval;
		} finally {
			LOGGER.trace("END");
		}
	}

	private String getOutput(InputStream inputStream)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			if (inputStream == null) {
				LOGGER.trace("OK");
				return "";
			}
			
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			try {
				inputStreamReader = new InputStreamReader(inputStream);
				bufferedReader = new BufferedReader(
						inputStreamReader);
				StringBuffer buffer = new StringBuffer();
				
				char[] buf = new char[1024]; 
				int readChars;
				
				while ((readChars = bufferedReader.read(buf, 0, 1024)) != -1) {
					buffer.append(buf, 0, readChars);
				}
				bufferedReader.close();
				inputStreamReader.close();

				LOGGER.trace("OK");
				return buffer.toString();
			} catch (IOException ex) {
				throw new TechnicalException("Cannot read the stream", ex);
			} finally {
				UtilsFile.getInstance().close(bufferedReader);
				UtilsFile.getInstance().close(inputStreamReader);
			}
		} finally {
			LOGGER.trace("END");
		}
	}
}
