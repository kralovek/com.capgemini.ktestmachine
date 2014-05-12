package com.capgemini.ktestmachine.component.lancer.template;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.executant.Executant;
import com.capgemini.ktestmachine.component.lancer.Lancer;
import com.capgemini.ktestmachine.data.TestInput;
import com.capgemini.ktestmachine.data.TestOutput;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.file.UtilsFile;

/**
 * LancerTemplateBased
 * 
 * @author KRALOVEC-99999
 */
public class LancerTemplateBased extends ALancerTemplateBasedFwk implements
		Lancer {
	private static final Logger LOGGER = Logger
			.getLogger(LancerTemplateBased.class);

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd-HHmmss-SSS");

	/**
	 * Parameter used for result content
	 */
	private static final String PARAM_TEST_ID = "SYSTEM/TEST_ID";

	/**
	 * Parameter used for response content
	 */
	private static final String PARAM_RESPONSE = "SYSTEM/RESPONSE";

	/**
	 * Parameter used for request content
	 */
	private static final String PARAM_REQUEST = "SYSTEM/REQUEST";

	/**
	 * Parameter used for time
	 */
	private static final String PARAM_TIME = "SYSTEM/TIME";

	/**
	 * Parameter used for time
	 */
	private static final String PARAM_TIME_LENGTH = "SYSTEM/TIME_LENGTH";

	private static final String PARAM_EXCEPTION_CLASS = "EXCEPTION/CLASS";

	private static final String PARAM_EXCEPTION_MESSAGE = "EXCEPTION/MESSAGE";

	private static final String PARAM_EXCEPTION_DETAIL = "EXCEPTION/DETAIL";

	public TestOutput lance(TestInput testInput) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			final Map<String, Object> resultMap = new TreeMap<String, Object>();

			final String nameRoot = nameRoot(testInput);

			validateTestUnit(testInput);

			final String template = templateArchiv.loadTemplate(testInput
					.getType());

			final Map<String, Object> inputParameters = new HashMap<String, Object>();
			inputParameters.putAll(systemInputParameters(testInput));
			inputParameters.putAll(testInput.getDataInput());

			final String templateParsed = templateParser.parse(template,
					inputParameters);
			resultMap.put(PARAM_REQUEST, templateParsed);

			if (traceSource) {
				final File fileTemplateParsed = new File(dirTrace, nameRoot
						+ "_" + "REQUEST.txt");
				UtilsFile.getInstance().contentToFile(templateParsed,
						fileTemplateParsed);
			}

			final Executant executant = chooseExecutant(testInput.getType());
			if (executant == null) {
				throw new ConfigurationException(
						"No executant configured for the test type: "
								+ testInput.getType());
			}

			String result = null;
			ABaseException aBaseException = null;
			Date dateBegin = null;
			Date dateEnd = null;
			try {
				boolean body = false;
				for (int i = 0; i < repeatUnsuccessful && !body; i++) {
					//
					// Withouot this the WS call is KO
					//
					if (i != 0) {
						pause(100);
					}

					dateBegin = new Date();
					result = executant.execute(templateParsed);
					resultMap.put(PARAM_RESPONSE, result);

					dateEnd = new Date();
					body = containsBody(result);
					if (!body) {
						dateBegin = null;
						dateEnd = null;
					}
					if (traceResult) {
						final File fileResult = new File(dirTrace, nameRoot
								+ "_" + "RESPONSE"
								+ (body ? "" : "_KO[" + i + "]") + ".txt");
						UtilsFile.getInstance().contentToFile(result,
								fileResult);
					}
				}
			} catch (final ABaseException ex) {
				aBaseException = ex;
			}

			final Map<String, Object> resultParserMap = resultParser
					.parse(result);

			if (aBaseException != null) {
				resultMap.put(PARAM_EXCEPTION_CLASS, aBaseException.getClass()
						.getSimpleName());
				resultMap.put(PARAM_EXCEPTION_MESSAGE,
						aBaseException.getMessage());
				resultMap
						.put(PARAM_EXCEPTION_DETAIL, aBaseException.toString());
			}

			resultMap.putAll(resultParserMap);

			if (dateBegin != null) {
				resultMap.put(PARAM_TIME, DATE_FORMAT.format(dateBegin));
				if (dateEnd != null) {
					// Time length in seconds
					final long timeLengthMS = dateEnd.getTime()
							- dateBegin.getTime();
					final double timeLengthS = ((double) timeLengthMS) / 1000.0;
					resultMap.put(PARAM_TIME_LENGTH,
							String.valueOf(timeLengthS));
				}
			}

			validateResult(resultMap);

			TestOutputImpl testOutput = new TestOutputImpl(
					testInput.getSource(), testInput.getType(),
					testInput.getId());

			testOutput.getDataOutput().putAll(resultMap);

			LOGGER.trace("OK");
			return testOutput;
		} finally {
			LOGGER.trace("END");
		}
	}

	private boolean containsBody(String pContent) {
		return pContent.contains("<?xml") || pContent.contains("xmlns");
	}

	private void validateResult(final Map<String, Object> pResultMap)
			throws ABaseException {
	}

	private void validateTestUnit(TestInput testUnit) throws ABaseException {
	}

	private String nameRoot(TestInput pTestUnit) {
		return DATE_FORMAT.format(new Date()) + "_" + pTestUnit.getType() + "_"
				+ pTestUnit.getId();
	}

	private Executant chooseExecutant(final String pSource) {
		if (executantByType != null && executantByType.containsKey(pSource)) {
			return executantByType.get(pSource);
		}
		return executant;
	}

	private void pause(final int pMilis) {
		try {
			Thread.sleep(pMilis);
		} catch (final InterruptedException ex) {
			//
		}
	}

	private Map<String, Object> systemInputParameters(TestInput pTestUnit) {
		final Map<String, Object> inputParameters = new HashMap<String, Object>();
		inputParameters.put(PARAM_TEST_ID, pTestUnit.getId());
		return inputParameters;
	}
}
