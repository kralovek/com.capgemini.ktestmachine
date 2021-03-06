package com.capgemini.ktestmachine.component.lancer.m2o_importfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager;
import com.capgemini.ktestmachine.component.filemanager.FileManager;
import com.capgemini.ktestmachine.component.lancer.Lancer;
import com.capgemini.ktestmachine.component.runner.Runner;
import com.capgemini.ktestmachine.data.TestInput;
import com.capgemini.ktestmachine.data.TestOutput;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.capgemini.ktestmachine.utils.collections.OrderFiFoMap;
import com.capgemini.ktestmachine.utils.errors.StopException;
import com.capgemini.ktestmachine.utils.file.UtilsFile;
import com.capgemini.ktestmachine.utils.parameters.UtilsParameters;

public class LancerM2OImportFile extends ALancerM2OImportFileFwk implements
		Lancer {
	private static final Logger LOGGER = Logger
			.getLogger(LancerM2OImportFile.class);

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");

	private static final String PARAM_EXCEPTION_CLASS = "EXCEPTION/CLASS";

	private static final String PARAM_EXCEPTION_MESSAGE = "EXCEPTION/MESSAGE";

	private static final String PARAM_EXCEPTION_DETAIL = "EXCEPTION/DETAIL";

	private static final String PARAM_TEST_ID = "TEST_ID";

	private static final String PARAM_DATA_INPUT = "DATA_INPUT";

	private static final String PARAM_DATA_OUTPUT = "DATA_OUTPUT";

	private static final String PARAM_TIME_BEGIN = "TIME";

	private static final String PARAM_TIME_END = "TIME_END";

	private static final String PARAM_TIME_LENGTH = "TIME_LENGTH";

	private static final String PARAM_OUTPUT_PARAMETERS = "PARAMETERS";

	private static final String PARAM_INPUT_DATA_FILE_NAME = "INPUT/DATA_FILE_NAME";

	private static final String PARAM_INPUT_DATA_FILE_GZ = "INPUT/DATA_FILE_GZ";

	private static final String PARAM_INPUT_DATA_NO_FILE = "INPUT/DATA_NO_FILE";

	private static final String PARAM_INPUT_DATA_NO_RUN = "INPUT/DATA_NO_RUN";

	private static final String PARAM_INPUT_DATA_FILE_ENCODING = "INPUT/DATA_FILE_ENCODING";

	public TestOutput lance(TestInput testInput) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			final Map<String, Object> resultMap = new OrderFiFoMap<String, Object>();
			try {
				final Map<String, Object> inputParameters = createInputParameters(testInput);

				//
				// DATA FILE
				//
				createDataFile(testInput, inputParameters, resultMap);

				//
				// Choose Diff Managers
				//
				List<DiffManager> diffManagers = chooseDiffManagers(testInput
						.getType());

				//
				// LOCK
				//
				lock();

				//
				// CURRENT
				//
				Map<String, List<DiffManager.Group>> diffGroupsCurrent = createCurrents(diffManagers);

				//
				// RUN
				//
				Date dateBegin = new Date();
				runTache(testInput, inputParameters, resultMap);
				Date dateEnd = new Date();

				//
				// STATISTICS
				//
				toParamStatistics(dateBegin, dateEnd, resultMap);

				//
				// DIFF
				//
				createDiff(diffManagers, diffGroupsCurrent, resultMap);

				unlock();

				//
				// Parameters to parametes
				//
				toParamParameters(resultMap);

			} catch (Exception ex) {
				resultMap.put(sysParamPrefix + PARAM_EXCEPTION_CLASS, ex
						.getClass().getSimpleName());
				resultMap.put(sysParamPrefix + PARAM_EXCEPTION_MESSAGE,
						ex.getMessage());
				resultMap.put(sysParamPrefix + PARAM_EXCEPTION_DETAIL,
						toStringException(ex));
			} finally {
				unlock();
			}

			TestOutputImpl testOutput = new TestOutputImpl(
					testInput.getSource(), testInput.getType(),
					testInput.getId());

			testOutput.getDataOutput().putAll(resultMap);

			LOGGER.trace("OK");

			return testOutput;
		} catch (StopException ex) {
			LOGGER.trace("OK");
			throw ex;
		} finally {
			LOGGER.trace("END");
		}
	}

	private Map<String, List<DiffManager.Group>> createCurrents(
			List<DiffManager> diffManagers) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			Map<String, List<DiffManager.Group>> map = new HashMap<String, List<DiffManager.Group>>();
			for (DiffManager diffManager : diffManagers) {
				List<DiffManager.Group> groups = diffManager.loadCurrents();
				map.put(diffManager.getName(), groups);
			}
			LOGGER.trace("OK");
			return map;
		} finally {
			LOGGER.trace("END");
		}
	}

	private void createDiff(List<DiffManager> diffManagers,
			Map<String, List<DiffManager.Group>> diffGroupsCurrent,
			Map<String, Object> resultMap) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			Map<String, List<DiffManager.Group>> diffGroupsDiff = new HashMap<String, List<DiffManager.Group>>();
			for (DiffManager diffManager : diffManagers) {
				List<DiffManager.Group> groupsCurrent = diffGroupsCurrent
						.get(diffManager.getName());
				List<DiffManager.Group> groupsDiff = diffManager
						.loadDiffs(groupsCurrent);
				diffGroupsDiff.put(diffManager.getName(), groupsDiff);
			}

			String resultXML = createResultDataXML(diffGroupsDiff);
			resultMap.put(sysParamPrefix + PARAM_DATA_OUTPUT, resultXML);

			generateTraceFileDiff(resultXML, new Date());

			final Map<String, Object> resultParserMapData = resultParser
					.parse(resultXML);
			resultMap.putAll(resultParserMapData);
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void toParamParameters(Map<String, Object> resultMap) {
		LOGGER.trace("BEGIN");
		try {
			Set<String> keys = new TreeSet<String>(resultMap.keySet());
			StringBuffer buffer = new StringBuffer();
			buffer.append(sysParamPrefix + PARAM_OUTPUT_PARAMETERS)
					.append('\n');
			for (String key : keys) {
				buffer.append(key).append('\n');
			}
			resultMap.put(sysParamPrefix + PARAM_OUTPUT_PARAMETERS,
					buffer.toString());

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void toParamStatistics(Date dateBegin, Date dateEnd,
			Map<String, Object> resultMap) {
		LOGGER.trace("BEGIN");
		try {
			if (dateBegin != null) {
				resultMap.put(sysParamPrefix + PARAM_TIME_BEGIN,
						DATE_FORMAT.format(dateBegin));
				if (dateEnd != null) {
					// Time length in seconds
					final long timeLengthMS = dateEnd.getTime()
							- dateBegin.getTime();
					final double timeLengthS = ((double) timeLengthMS) / 1000.0;
					resultMap.put(sysParamPrefix + PARAM_TIME_LENGTH,
							String.valueOf(timeLengthS));
					resultMap.put(sysParamPrefix + PARAM_TIME_END,
							DATE_FORMAT.format(dateEnd));
				}
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void runTache(TestInput testInput,
			Map<String, Object> inputParameters, Map<String, Object> resultMap)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			Boolean noRun = UtilsParameters.getStringBoolean(inputParameters,
					sysParamPrefix + PARAM_INPUT_DATA_NO_RUN, false);

			if (!noRun) {
				Runner runner = chooseRunner(testInput.getType());
				Map<String, Object> outputParameters = runner
						.run(inputParameters);
				resultMap.putAll(outputParameters);
			} else {
				LOGGER.info("Tache run was skiped for the test: ["
						+ testInput.getId() + "]-{" + testInput.getType() + "}");
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void createDataFile(TestInput testInput,
			Map<String, Object> inputParameters, Map<String, Object> resultMap)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			Boolean noFile = UtilsParameters.getStringBoolean(inputParameters,
					sysParamPrefix + PARAM_INPUT_DATA_NO_FILE, false);

			if (!noFile) {
				String templateData = templateArchiv.loadTemplate(testInput
						.getType() + ".data");

				String templateDataParsed = templateParser.parse(templateData,
						inputParameters);
				resultMap.put(sysParamPrefix + PARAM_DATA_INPUT,
						templateDataParsed);

				String filename = UtilsParameters.getStringParam(
						inputParameters, sysParamPrefix
								+ PARAM_INPUT_DATA_FILE_NAME);
				if (filename == null) {
					if (destinationFilenamePattern != null) {
						filename = destinationFilenamePattern
								.format(new Date());
					} else {
						throw new ConfigurationException(
								"The generated file is not specified by parameters nor configured");
					}
				}
				String fileEncoding = UtilsParameters.getStringParam(
						inputParameters, sysParamPrefix
								+ PARAM_INPUT_DATA_FILE_ENCODING);
				Boolean gz = UtilsParameters.getStringBoolean(inputParameters,
						sysParamPrefix + PARAM_INPUT_DATA_FILE_GZ, false);

				FileManager fileManager = chooseFileManager(testInput.getType());
				String dirReception = chooseDirReception(testInput.getType());
				if (gz) {
					fileManager.contentToGzFile(templateDataParsed, filename,
							fileEncoding, dirReception);
				} else {
					fileManager.contentToFile(templateDataParsed, filename,
							fileEncoding, dirReception);
				}
			} else {
				LOGGER.info("No file will be generated for the test: ["
						+ testInput.getId() + "]-{" + testInput.getType() + "}");
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private Map<String, Object> createInputParameters(TestInput pTestUnit) {
		final Map<String, Object> inputParameters = new HashMap<String, Object>();
		inputParameters.put(PARAM_TEST_ID, pTestUnit.getId());
		inputParameters.putAll(pTestUnit.getDataInput());
		return inputParameters;
	}

	private String chooseDirReception(final String pSource)
			throws ABaseException {
		String dirReception = UtilsParameters.chooseByTypePattern(
				this.dirDestination, dirDestinationByTypePattern, pSource);
		if (dirReception == null) {
			throw new ConfigurationException(
					"No directory Reception is defined for the source: "
							+ pSource);
		}
		return dirReception;
	}

	private FileManager chooseFileManager(final String pSource)
			throws ABaseException {
		FileManager fileManager = UtilsParameters.chooseByTypePattern(
				this.fileManager, fileManagerByTypePattern, pSource);
		if (fileManager == null) {
			throw new ConfigurationException(
					"No FileManager is defined for the source: " + pSource);
		}
		return fileManager;
	}

	private List<DiffManager> chooseDiffManagers(final String pSource)
			throws ABaseException {
		List<DiffManager> diffManagers = UtilsParameters.chooseByTypePattern(
				this.diffManagers, diffManagersByTypePattern, pSource);
		// No test is necessary
		return diffManagers;
	}

	private Runner chooseRunner(final String pSource) throws ABaseException {
		Runner runner = UtilsParameters.chooseByTypePattern(this.runner,
				runnerByTypePattern, pSource);
		if (runner == null) {
			throw new ConfigurationException(
					"No Runner is defined for the source: " + pSource);
		}
		return runner;
	}

	private String createResultDataXML(
			Map<String, List<DiffManager.Group>> namesGroups) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<DIFFS>").append('\n');
		for (Map.Entry<String, List<DiffManager.Group>> entry : namesGroups
				.entrySet()) {
			String diffTagName = adaptXmlTag(entry.getKey());
			List<DiffManager.Group> groups = entry.getValue();
			buffer.append("    ").append("<" + diffTagName + ">").append('\n');
			for (DiffManager.Group group : groups) {
				String groupTagName = adaptXmlTag(group.getName());
				buffer.append("        ").append("<" + groupTagName + ">")
						.append('\n');
				if (group.getItems() != null) {
					for (DiffManager.Item item : group.getItems()) {
						buffer.append("            ")
								.append("<ITEM ")
								.append("name=\"" + item.getName() + "\" ")
								.append("status=\"" + item.getStatus() + "\" ")
								.append("index=\"" + item.getIndex().toString()
										+ "\" ").append(">").append("\n");
						if (item.getParameters() != null) {
							for (Map.Entry<String, String> entryParameter : item
									.getParameters().entrySet()) {
								String value = adaptXmlValue(entryParameter
										.getValue());
								buffer.append("                ")
										.append("<" + entryParameter.getKey()
												+ ">")
										//
										// TODO: CDATA
										//
										.append(value)
										.append("</" + entryParameter.getKey()
												+ ">").append("\n");
							}
						}
						buffer.append("            ").append("</ITEM>")
								.append("\n");
					}
				}
				buffer.append("        ").append("</" + groupTagName + ">")
						.append('\n');
			}
			buffer.append("    ").append("</" + diffTagName + ">").append('\n');
		}
		buffer.append("</DIFFS>").append('\n');

		return buffer.toString();
	}

	private static String adaptXmlValue(String value) {
		if (value == null) {
			return value;
		}
		String valueMod = value.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;");
		return valueMod;
	}

	private static String adaptXmlTag(String tag) {
		if (tag == null) {
			return null;
		}
		String tagAdapted = tag.replace(":", "_").replace("/", "_")
				.replace("\\", "_");
		return tagAdapted;
	}

	private void generateTraceFileDiff(String diff, Date date)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			if (traceDiffPattern != null) {
				String path = traceDiffPattern.format(date);
				File file = new File(path);
				LOGGER.debug("Logging the DIFF to: " + file.toString());
				if (file.getParentFile() != null
						&& !file.getParentFile().isDirectory()
						&& !file.getParentFile().mkdirs()) {
					throw new TechnicalException(
							"Cannot create the directory: "
									+ file.getParentFile().getAbsolutePath());
				}
				UtilsFile.getInstance().contentToFile(diff, file);
			}
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private String toStringException(final Exception pException) {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		pException.printStackTrace(printStream);
		printStream.close();
		return byteArrayOutputStream.toString();
	}

	private void lock() throws ABaseException {
		if (locker != null) {
			locker.lock();
		}
	}

	private void unlock() throws ABaseException {
		if (locker != null) {
			locker.unlock();
		}
	}
}
