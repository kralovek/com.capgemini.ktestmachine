package com.capgemini.ktestmachine.component.ktestmachine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.lancer.Lancer;
import com.capgemini.ktestmachine.data.TestInput;
import com.capgemini.ktestmachine.data.TestOutput;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.errors.StopException;
import com.capgemini.ktestmachine.utils.errors.TreatErrors;
import com.capgemini.ktestmachine.utils.parameters.UtilsParameters;

public class KTestMachineGeneric extends AKTestMachineGenericFwk implements
		KTestMachine {
	private static final Logger LOGGER = Logger
			.getLogger(KTestMachineGeneric.class);

	private static final String PARAM_INPUT_DATA_STOP = "INPUT/DATA_STOP";

	private static final DateFormat BATCH_ID_DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd-HHmmss-SSS");

	public boolean test(String source) {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			final String batchId = BATCH_ID_DATE_FORMAT.format(new Date());
			LOGGER.info("==============================");
			LOGGER.info("RUN Batch [" + batchId + "]");
			LOGGER.info("==============================");
			final Map<String, Map<String, Map<Integer, Object[]>>> mapSources = new TreeMap<String, Map<String, Map<Integer, Object[]>>>();

			boolean resultAll = true;
			final List<TestInput> testUnits = testLoader.loadTests(source);
			if (testUnits != null) {
				LOGGER.trace("BEGIN");
				try {
					final int testsCount = testUnits.size();
					int iTest = 0;
					boolean stoped = false;
					// To liberate the memory
					final Iterator<TestInput> iterator = testUnits.iterator();
					while (iterator.hasNext()) {
						final TestInput testInput = iterator.next();
						iterator.remove();
						if (!stoped && stopFile != null && stopFile.exists()) {
							stoped = true;
							if (!stopFile.delete()) {
								stopFile.deleteOnExit();
							}
							LOGGER.info("=========================================");
							LOGGER.info("EXECUTION OF THE TESTS STOPED BY THE USER");
							LOGGER.info("=========================================");
						}

						Boolean resultTest = null;
						iTest++;

						if (!stoped) {
							stoped = checkStop(testInput);
							LOGGER.trace("BEGIN");
							try {
								LOGGER.info("..............................");
								LOGGER.info("Test:" + " (" + iTest + "/"
										+ testsCount + ")" + " [ID: "
										+ testInput.getId() + "]" + " [TYPE: "
										+ testInput.getType() + "]"
										+ " [SOURCE: " + testInput.getSource()
										+ "]");

								TestOutput testOutput = null;
								Lancer lancer = chooseLancer(testInput
										.getType());
								try {
									testOutput = lancer.lance(testInput);
								} catch (StopException ex) {
									stoped = true;
								}
								resultTest = testReporter.reportTest(
										testOutput, // TODO: check testOutput =
													// null
										batchId);
								resultAll = resultAll && resultTest;
								if (resultTest) {
									LOGGER.info(" * OK *");
								} else {
									LOGGER.info("## KO ##");
								}
								LOGGER.trace("OK");
							} finally {
								LOGGER.trace("END");
							}
						}

						Map<String, Map<Integer, Object[]>> mapTypes = mapSources
								.get(testInput.getSource());
						if (mapTypes == null) {
							mapTypes = new TreeMap<String, Map<Integer, Object[]>>();
							mapSources.put(testInput.getSource(), mapTypes);
						}
						Map<Integer, Object[]> mapTestIds = mapTypes
								.get(testInput.getType());
						if (mapTestIds == null) {
							mapTestIds = new TreeMap<Integer, Object[]>();
							mapTypes.put(testInput.getType(), mapTestIds);
						}
						mapTestIds.put(iTest, new Object[] { resultTest,
								testInput.getId() });
					}
					LOGGER.trace("OK");
				} finally {
					LOGGER.trace("END");
				}
			}
			printReport(mapSources, batchId);

			LOGGER.trace("OK");
			return resultAll;
		} catch (final Exception ex) {
			TreatErrors.treatException(ex);
			LOGGER.trace("OK");
			return false;
		} finally {
			LOGGER.trace("END");
		}
	}

	private void printReport(
			final Map<String, Map<String, Map<Integer, Object[]>>> pMapSources,
			final String pBatchId) {
		LOGGER.info("==============================");
		LOGGER.info("REVIEW Batch [" + pBatchId + "]");
		LOGGER.info("==============================");
		int countKoAll = 0;
		int countOkAll = 0;

		for (final Map.Entry<String, Map<String, Map<Integer, Object[]>>> entrySource : pMapSources
				.entrySet()) {
			LOGGER.info("\tSource [" + (String) entrySource.getKey() + "]");
			int countKoSource = 0;
			int countOkSource = 0;
			for (final Map.Entry<String, Map<Integer, Object[]>> entryType : entrySource
					.getValue().entrySet()) {
				LOGGER.info("\t\tType [" + (String) entryType.getKey() + "]");
				int countKoType = 0;
				int countOkType = 0;
				for (final Map.Entry<Integer, Object[]> entryTest : entryType
						.getValue().entrySet()) {
					final Object[] testId = entryTest.getValue();
					final Boolean resultTest = (Boolean) testId[0];
					final String id = (String) testId[1];
					if (resultTest != null) {
						countKoType += (resultTest ? 0 : 1);
						countOkType += (resultTest ? 1 : 0);
						if (resultTest)
							LOGGER.info("\t\t\t * OK *  - Test [" + id + "]");
						else {
							LOGGER.info("\t\t\t## KO ## - Test [" + id + "]");
						}
					} else {
						LOGGER.info("\t\t\tTest [" + id + "] : SKIPPED");
					}
				}
				countKoSource += countKoType;
				countOkSource += countOkType;
				if (countKoType == 0)
					LOGGER.info("\t\t * OK (" + countOkType + ") *");
				else {
					LOGGER.info("\t\t## KO (" + countKoType + "/"
							+ (countOkType + countKoType) + ") ##");
				}
			}
			countKoAll += countKoSource;
			countOkAll += countOkSource;

			if (countKoSource == 0)
				LOGGER.info("\t * OK (" + countOkSource + ") *");
			else {
				LOGGER.info("\t## KO (" + countKoSource + "/"
						+ (countOkSource + countKoSource) + ") ##");
			}
		}
		if (countKoAll == 0) {
			LOGGER.info("------------------------------");
			LOGGER.info("* OK (" + countOkAll + ") *");
			LOGGER.info("All tests finished SUCCESSFULY");
			LOGGER.info("------------------------------");
		} else {
			LOGGER.info("##############################");
			LOGGER.info("# KO (" + countKoAll + "/" + (countOkAll + countKoAll)
					+ ") #");
			LOGGER.info("Some tests finished in ERROR");
			LOGGER.info("##############################");
		}
	}

	private Lancer chooseLancer(String key) throws ABaseException {
		Lancer lancer = UtilsParameters.chooseByTypePattern(this.lancer,
				lancerByTypePattern, key);
		if (lancer == null) {
			throw new ConfigurationException("No Lancer is specified for: "
					+ key);
		}
		return lancer;
	}

	private boolean checkStop(TestInput pTestUnit) throws ABaseException {
		return UtilsParameters.getStringBoolean(pTestUnit.getDataInput(),
				sysParamPrefix + PARAM_INPUT_DATA_STOP, false);
	}
}
