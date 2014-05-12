package com.capgemini.ktestmachine.component.testreporter.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.exceladapter.ExcelStyles;
import com.capgemini.ktestmachine.component.exceladapter.TCell;
import com.capgemini.ktestmachine.component.exceladapter.TSheet;
import com.capgemini.ktestmachine.component.exceladapter.TStyle;
import com.capgemini.ktestmachine.component.exceladapter.TWorkbook;
import com.capgemini.ktestmachine.component.testloader.excel.StructureSheetGeneric;
import com.capgemini.ktestmachine.component.testreporter.TestReporter;
import com.capgemini.ktestmachine.data.TestOutput;
import com.capgemini.ktestmachine.data.TestResult;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.capgemini.ktestmachine.utils.excel.ExcelConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelPosition;
import com.capgemini.ktestmachine.utils.excel.Value;

public class TestReporterExcel extends ATestReporterExcelFwk implements
		TestReporter {
	private static final Logger LOGGER = Logger
			.getLogger(TestReporterExcel.class);

	private static class Status {
		int all;
		int err;
	}

	public List<TestResult> loadResults(String source, String batchID)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			List<TestResult> retval = new ArrayList<TestResult>();

			File fileResult = generateTargetFile(source, batchID);
			if (!fileResult.isFile()) {
				LOGGER.warn("No result: " + fileResult.getAbsolutePath());
				LOGGER.trace("OK");
				return null;
			}

			ExcelPosition excelPosition = new ExcelPosition();
			excelPosition.setFile(fileResult);

			TWorkbook workbook = excelAdapter.readWorkbook(fileResult);

			int countSheet = excelAdapter.getSheetCount(workbook);

			for (int i = 0; i < countSheet; i++) {
				TSheet sheet = excelAdapter.getSheet(workbook, i);
				if (ignoredSheets.contains(sheet.getName())) {
					continue;
				}

				excelPosition.setSheet(sheet.getName());

				Map<Integer, String> codes = structureSheet.loadCodes(
						excelPosition, sheet, idRowCode, idRowActive,
						ignoredIdColumns);

				Map<Integer, Boolean> statuses = structureSheet.loadStatuses(
						excelPosition, sheet, idRowStatus, idRowActive,
						ignoredIdColumns);

				for (Map.Entry<Integer, Boolean> entry : statuses.entrySet()) {
					String code = codes.get(entry.getKey());
					Boolean result = entry.getValue();

					TestResultImpl testResult = new TestResultImpl();
					testResult.setSource(source);
					testResult.setType(sheet.getName());
					testResult.setId(code);
					testResult.setResult(result);
					retval.add(testResult);
				}
			}

			excelAdapter.closeWorkbook(workbook);

			LOGGER.trace("OK");
			return retval;
		} finally {
			LOGGER.trace("END");
		}
	}

	public boolean reportTest(TestOutput test, String batchID)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			File fileSource = new File(test.getSource());
			File fileTarget = generateTargetFile(test.getSource(), batchID);
			File fileTmp = generateTempFile(fileTarget);

			TWorkbook workbookSource;
			if (fileTarget.exists()) {
				workbookSource = excelAdapter.readWorkbook(fileTarget);
			} else {
				workbookSource = excelAdapter.readWorkbook(fileSource);
			}
			TWorkbook workbookTarget = excelAdapter.cloneWorkbook(
					workbookSource, fileTmp);

			boolean resultTest = workWorkbook(workbookTarget, test);

			excelAdapter.saveWorkbook(workbookTarget);

			excelAdapter.closeWorkbook(workbookTarget);

			excelAdapter.closeWorkbook(workbookSource);

			renameFile(fileTmp, fileTarget);

			LOGGER.trace("OK");
			return resultTest;
		} finally {
			LOGGER.trace("END");
		}
	}

	private boolean workWorkbook(TWorkbook workbook, TestOutput test)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			ExcelPosition excelPosition = new ExcelPosition();
			excelPosition.setFile(workbook.getFile());

			TSheet sheet = excelAdapter.getSheet(workbook, test.getType());
			if (sheet == null) {
				throw new ExcelConfigurationException(excelPosition,
						"The excel file does not contain a sheet: "
								+ test.getType());
			}

			ExcelStyles excelStyles = excelStylesFactory.getStyles(workbook);

			boolean resultTest = workSheet(excelPosition, workbook, sheet, test, excelStyles);
			LOGGER.trace("OK");
			return resultTest;
		} finally {
			LOGGER.trace("END");
		}
	}

	private void writeStatusReview(ExcelPosition genExcelPosition,
			TWorkbook workbook, String sheetName, boolean result,
			ExcelStyles excelStyles) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			if (review) {
				ExcelPosition excelPosition = genExcelPosition.clone();
				excelPosition.setSheet(reviewSheet);
				TSheet sheet = excelAdapter.getSheet(workbook, reviewSheet);
				if (sheet == null) {
					sheet = excelAdapter.createSheet(workbook, reviewSheet);
				}
				Integer row = null;
				for (int ir = idReviewRowFirst;; ir++) {
					TCell cell = excelAdapter.getCell(sheet, ir,
							idReviewColumnSheetName);
					String sheetNameExist = excelAdapter.getStringValue(cell);
					if (sheetName.equals(sheetNameExist)) {
						row = ir;
						break;
					}
					if (isEmpty(sheetNameExist)) {
						excelAdapter.setValue(sheet, cell, sheetName);
						row = ir;
						break;
					}
				}
				if (row == null) {
					throw new ExcelConfigurationException(excelPosition,
							"The is no place for the review of the sheet "
									+ sheetName + " in the rewie sheet.");
				}
				TCell cellStatus = excelAdapter.getCell(sheet, row,
						idReviewColumnStatus);
				String valueStatus = excelAdapter.getStringValue(cellStatus);
				Status status = getStatus(excelPosition, valueStatus);
				status.all++;
				if (!result) {
					status.err++;
				}

				String statusString = status.err != 0 ? status.err + "/" + status.all
						: "" + status.all;

				excelAdapter.setValue(sheet, cellStatus, "" + statusString);
				if (idReviewColumnColour != null) {
					TCell cellColor = excelAdapter.getCell(sheet, row,
							idReviewColumnColour);
					TStyle style = status.err == 0 ? excelStyles
							.getStyleStatOK() : excelStyles.getStyleStatKO();
					excelAdapter.setCellStyle(cellColor, style);
				}
			}
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private boolean workSheet(ExcelPosition genExcelPosition,
			TWorkbook workbook, TSheet sheet, TestOutput test,
			ExcelStyles excelStyles) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			ExcelPosition excelPosition = genExcelPosition.clone();
			excelPosition.setSheet(sheet.getName());

			Map<Integer, String> codes = structureSheet.loadCodes(
					excelPosition, sheet, idRowCode, idRowActive,
					ignoredIdColumns);

			Integer column = findPosition(test.getId(), codes);
			if (column == null) {
				throw new ExcelConfigurationException(excelPosition,
						"The test does not exist in the file: " + test.getId());
			}

			Map<Integer, String> parametersE = structureSheet.loadParameters(
					excelPosition, sheet, idColumnParameter, idColumnIO,
					ignoredIdRows, StructureSheetGeneric.IO.EXPECT);
			Map<Integer, String> parametersO = structureSheet.loadParameters(
					excelPosition, sheet, idColumnParameter, idColumnIO,
					ignoredIdRows, StructureSheetGeneric.IO.OUTPUT);

			Set<String> parametersTreated = new HashSet<String>();

			validationSheet(excelPosition, sheet, codes, parametersE,
					parametersO);

			Map<String, Object> dataTestO = new HashMap<String, Object>(
					test.getDataOutput());
			Map<String, Value> dataTestE = loadDataExpect(excelPosition, sheet,
					parametersE, column);

			boolean resultTest = true;
			for (Map.Entry<Integer, String> entry : parametersE.entrySet()) {
				int rowE = entry.getKey();
				String parameter = entry.getValue();
				List<Integer> rowsO = findPositions(parameter, parametersO);

				Value valueE = dataTestE.get(parameter);
				Object valueO = dataTestO.get(parameter);
				boolean resultParameter = excelValueManager.compareTestValues(
						valueE, valueO);
				resultTest &= resultParameter;

				writeTestDataCompared(genExcelPosition, sheet, column, rowE,
						rowsO, valueO, resultParameter, excelStyles);

				parametersTreated.add(parameter);
			}

			for (Map.Entry<Integer, String> entry : parametersO.entrySet()) {
				String parameter = entry.getValue();
				List<Integer> rowsO = findPositions(parameter, parametersO);
				Object valueO = dataTestO.get(parameter);
				if (parametersTreated.contains(parameter)) {
					continue;
				}
				writeTestData(genExcelPosition, sheet, column, rowsO, valueO,
						excelStyles);
			}

			writeStatusTest(genExcelPosition, sheet, column, resultTest,
					excelStyles);

			writeStatusSheet(genExcelPosition, sheet, resultTest, excelStyles);

			writeStatusReview(genExcelPosition, workbook, sheet.getName(),
					resultTest, excelStyles);

			LOGGER.trace("OK");
			return resultTest;
		} finally {
			LOGGER.trace("END");
		}
	}

	private void writeStatusSheet(ExcelPosition genExcelPosition, TSheet sheet,
			Boolean result, ExcelStyles excelStyles) throws ABaseException {
		if (idCellStatusSheet != null) {
			ExcelPosition excelPosition = genExcelPosition.clone();
			excelPosition.setColumn(idCellStatusSheet.getColumn());
			excelPosition.setRow(idCellStatusSheet.getRow());
			TCell cellStatus = excelAdapter.getCell(sheet,
					idCellStatusSheet.getRow(), idCellStatusSheet.getColumn());
			String statusValue = excelAdapter.getStringValue(cellStatus);
			Status status = getStatus(genExcelPosition, statusValue);
			status.all++;
			if (!result) {
				status.err++;
			}
			if (status.err != 0) {
				excelAdapter.setValue(sheet, cellStatus, "" + status.err + "/"
						+ status.all);
			} else {
				excelAdapter.setValue(sheet, cellStatus, status.all);
			}
			if (idCellColourSheet != null) {
				TCell cellColour = excelAdapter.getCell(sheet,
						idCellColourSheet.getRow(),
						idCellColourSheet.getColumn());
				TStyle styleColour = status.err == 0 ? excelStyles
						.getStyleStatOK() : excelStyles.getStyleStatKO();
				excelAdapter.setCellStyle(cellColour, styleColour);
			}
		}
	}

	private void writeStatusTest(ExcelPosition genExcelPosition, TSheet sheet,
			int column, Boolean result, ExcelStyles excelStyles)
			throws ABaseException {

		if (idRowStatus != null) {
			TCell cellStatus = excelAdapter.getCell(sheet, idRowStatus, column);
			String value = result ? StructureSheetGeneric.STATUS_OK
					: StructureSheetGeneric.STATUS_KO;
			excelAdapter.setValue(sheet, cellStatus, value);
		}
		if (idRowColour != null) {
			TCell cellColour = excelAdapter.getCell(sheet, idRowColour, column);
			TStyle styleColour = result ? excelStyles.getStyleTestOK()
					: excelStyles.getStyleTestKO();
			excelAdapter.setCellStyle(cellColour, styleColour);
		}
	}

	private Status getStatus(ExcelPosition genExcelPosition, String value)
			throws ABaseException {
		if (value == null || value.trim().isEmpty()) {
			return new Status();
		}
		String[] parts = value.split("/");
		try {
			Status status = new Status();
			if (parts.length == 1) {
				status.all = Integer.parseInt(value);
				status.err = 0;
			} else if (parts.length == 2) {
				status.all = Integer.parseInt(parts[1]);
				status.err = Integer.parseInt(parts[0]);
			} else {
				throw new Exception();
			}
			return status;
		} catch (Exception ex) {
			throw new ExcelConfigurationException(genExcelPosition,
					"The value of the status counter is not valid: " + value);
		}
	}

	private void writeTestDataCompared(ExcelPosition genExcelPosition,
			TSheet sheet, int column, Integer rowE, List<Integer> rowsO,
			Object valueTestO, Boolean result, ExcelStyles excelStyles)
			throws ABaseException {
		ExcelPosition excelPosition = genExcelPosition.clone();
		excelPosition.setColumn(column);
		TCell cellE = excelAdapter.getCell(sheet, rowE, column);
		Object valueExcelO = excelValueManager.testToExcelValue(excelPosition,
				valueTestO);
		List<TCell> cellsO = new ArrayList<TCell>();
		TStyle styleE = result ? excelStyles.getStyleFieldExpectOK()
				: excelStyles.getStyleFieldExpectKO();
		TStyle styleO = result ? excelStyles.getStyleFieldOutputOK()
				: excelStyles.getStyleFieldOutputKO();
		for (Integer rowO : rowsO) {
			TCell cellO = excelAdapter.getCell(sheet, rowO, column);
			excelAdapter.setCellStyle(cellO, styleO);
			excelAdapter.setValue(sheet, cellO, cellE, valueExcelO);
			cellsO.add(cellO);
		}
		excelAdapter.setCellStyle(cellE, styleE);
	}

	private void writeTestData(ExcelPosition genExcelPosition, TSheet sheet,
			int column, List<Integer> rowsO, Object valueTestO,
			ExcelStyles excelStyles) throws ABaseException {
		ExcelPosition excelPosition = genExcelPosition.clone();
		excelPosition.setColumn(column);
		Object valueExcelO = excelValueManager.testToExcelValue(excelPosition,
				valueTestO);
		List<TCell> cellsO = new ArrayList<TCell>();
		for (Integer rowO : rowsO) {
			TCell cellO = excelAdapter.getCell(sheet, rowO, column);
			excelAdapter.setValue(sheet, cellO, valueExcelO);
			cellsO.add(cellO);
		}
	}

	private Map<String, Value> loadDataExpect(ExcelPosition genExcelPosition,
			TSheet sheet, Map<Integer, String> parametersE, int column)
			throws ABaseException {
		Map<String, Value> dataE = new HashMap<String, Value>();
		for (Map.Entry<Integer, String> entry : parametersE.entrySet()) {
			TCell cell = excelAdapter.getCell(sheet, entry.getKey(), column);
			Object excelValue = excelAdapter.getValue(cell);
			Value testValue = excelValueManager.excelToFlagTestValue(
					genExcelPosition, excelValue);
			dataE.put(entry.getValue(), testValue);
		}
		return dataE;
	}

	private List<Integer> findPositions(String parameter,
			Map<Integer, String> parameters) {
		List<Integer> columns = new ArrayList<Integer>();
		for (Map.Entry<Integer, String> entry : parameters.entrySet()) {
			if (parameter.equals(entry.getValue())) {
				columns.add(entry.getKey());
			}
		}
		return columns;
	}

	private Integer findPosition(String parameter,
			Map<Integer, String> parameters) {
		for (Map.Entry<Integer, String> entry : parameters.entrySet()) {
			if (parameter.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	private void renameFile(File fileSource, File fileTarget)
			throws ABaseException {
		createFileDirectory(fileTarget);
		if (fileTarget.exists() && !fileTarget.delete()) {
			throw new TechnicalException("Cannot delete the file '"
					+ fileSource.getAbsolutePath() + "' to '"
					+ fileTarget.getAbsolutePath() + "'");
		}
		if (!fileSource.renameTo(fileTarget)) {
			throw new TechnicalException("Cannot rename the temp file '"
					+ fileSource.getAbsolutePath() + "' to '"
					+ fileTarget.getAbsolutePath() + "'");
		}
	}

	private File generateTargetFile(String source, String batchID) {
		File fileSource = new File(source);
		return new File(dir, (batchID != null ? batchID : "XXX") + "_"
				+ fileSource.getName());
	}

	private File generateTempFile(File filetarget) {
		File fileTmp = new File(filetarget.getParentFile(), "TMP_"
				+ filetarget.getName());
		return fileTmp;
	}

	private void validationSheet(ExcelPosition excelPosition, TSheet sheet,
			Map<Integer, String> codes, Map<Integer, String> parametersE,
			Map<Integer, String> parametersO) throws ABaseException {
		Collection<String> valuesParametersO = parametersO.values();
		for (Map.Entry<Integer, String> entry : parametersE.entrySet()) {
			if (!valuesParametersO.contains(entry.getValue())) {
				excelPosition.setRow(entry.getKey());
				excelPosition.setColumn(idColumnIO);
				throw new ExcelConfigurationException(excelPosition,
						"The EXPECT parameter is not present as OUTPUT: '"
								+ entry.getValue() + "'");
			}
		}
	}

	private void createFileDirectory(File file) throws TechnicalException {
		if (file != null) {
			File dir = file.getParentFile();
			if (dir != null && !dir.isDirectory() && !dir.mkdirs()) {
				throw new TechnicalException("Cannot create the directory: "
						+ dir.getAbsolutePath());
			}
		}
	}
}
