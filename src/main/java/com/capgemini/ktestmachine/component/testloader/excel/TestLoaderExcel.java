package com.capgemini.ktestmachine.component.testloader.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.exceladapter.TCell;
import com.capgemini.ktestmachine.component.exceladapter.TSheet;
import com.capgemini.ktestmachine.component.exceladapter.TWorkbook;
import com.capgemini.ktestmachine.component.testloader.TestLoader;
import com.capgemini.ktestmachine.data.TestInput;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.utils.excel.ExcelConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelPosition;

public class TestLoaderExcel extends ATestLoaderExcelFwk implements TestLoader {
	private static final Logger LOGGER = Logger
			.getLogger(TestLoaderExcel.class);

	public List<TestInput> loadTests(String source) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			File file = new File(source);
			TWorkbook workbook = null;
			try {
				workbook = excelAdapter.readWorkbook(file);
				ExcelPosition excelPosition = new ExcelPosition();
				excelPosition.setFile(file);

				List<TestInputImpl> testsWorkbook = workWorkbook(excelPosition,
						workbook, source);

				List<TestInput> sortedTests = sortTests(testsWorkbook);

				LOGGER.trace("OK");
				return sortedTests;
			} finally {

			}
		} finally {
			LOGGER.trace("END");
		}
	}

	private List<TestInput> sortTests(List<TestInputImpl> tests)
			throws ABaseException {
		Collections.sort(tests);
		List<TestInput> testsSorted = new ArrayList<TestInput>();
		testsSorted.addAll(tests);
		return testsSorted;
	}

	private List<TestInputImpl> workWorkbook(ExcelPosition excelPosition,
			TWorkbook workbook, String source) throws ABaseException {
		int sheetCount = excelAdapter.getSheetCount(workbook);

		List<TestInputImpl> testsWorkbook = new ArrayList<TestInputImpl>();

		for (int i = 0; i < sheetCount; i++) {
			TSheet sheet = excelAdapter.getSheet(workbook, i);

			if (isIgnoredSheet(sheet.getName())) {
				LOGGER.info("Sheet ignored: " + sheet.getName());
				continue;
			}

			List<TestInputImpl> testsSheet = workSheet(excelPosition, sheet, i,
					source);
			if (testsSheet != null) {
				testsWorkbook.addAll(testsSheet);
			}
		}

		return testsWorkbook;
	}

	private List<TestInputImpl> workSheet(ExcelPosition genExcelPosition,
			TSheet sheet, int sheetOrder, String source) throws ABaseException {
		List<TestInputImpl> testsSheet = new ArrayList<TestInputImpl>();

		ExcelPosition excelPosition = genExcelPosition.clone();
		excelPosition.setSheet(sheet.getName());

		Map<Integer, String> codes = structureSheet.loadCodes(excelPosition,
				sheet, idRowCode, idRowActive, ignoredIdColumns);
		Map<Integer, Integer> orders = idRowOrder == null ? null
				: structureSheet.loadOrders(excelPosition, sheet, codes,
						idRowOrder);

		Map<Integer, String> names = idRowName == null ? null : structureSheet
				.loadNames(excelPosition, sheet, codes, idRowName);

		Map<Integer, String> descriptions = idRowDescription == null ? null
				: structureSheet.loadNames(excelPosition, sheet, codes,
						idRowDescription);

		Map<Integer, String> parametersI = structureSheet.loadParameters(
				excelPosition, sheet, idColumnParameter, idColumnIO,
				ignoredIdRows, StructureSheetGeneric.IO.INPUT);

		validationSheet(excelPosition, sheet, codes);

		for (Map.Entry<Integer, String> entry : codes.entrySet()) {
			int ic = entry.getKey();

			TestInputImpl test = new TestInputImpl();
			test.setSource(source);
			test.setId(entry.getValue());
			test.setType(sheet.getName());
			test.setSheetOrder(sheetOrder);

			if (orders != null) {
				test.setOrder(orders.get(ic));
			}

			if (names != null) {
				test.setName(names.get(ic));
			}

			if (descriptions != null) {
				test.setDescription(descriptions.get(ic));
			}

			if (parametersI != null) {
				ExcelPosition excelPositionCell = excelPosition.clone();
				for (Map.Entry<Integer, String> entryParameter : parametersI
						.entrySet()) {
					int ir = entryParameter.getKey();

					TCell cell = excelAdapter.getCell(sheet, ir, ic);

					Object valueExcel = excelAdapter.getValue(cell);

					excelPositionCell.setColumn(ic);
					excelPositionCell.setRow(ir);
					Object valueTest = excelValueManager.excelToTestValue(
							excelPosition, valueExcel);

					test.getDataInput().put(entryParameter.getValue(),
							valueTest);
				}
			}
			testsSheet.add(test);
		}

		return testsSheet;
	}

	private boolean isIgnoredSheet(String sheetName) {
		if (ignoredSheets == null) {
			return false;
		}
		if (sheetName.startsWith("(N)")) {
			return false;
		}
		return ignoredSheets.contains(sheetName);
	}

	private void validationSheet(ExcelPosition excelPosition, TSheet sheet,
			Map<Integer, String> codes) throws ABaseException {
		Map<Integer, String> parametersE = structureSheet.loadParameters(
				excelPosition, sheet, idColumnParameter, idColumnIO,
				ignoredIdRows, StructureSheetGeneric.IO.EXPECT);
		Map<Integer, String> parametersO = structureSheet.loadParameters(
				excelPosition, sheet, idColumnParameter, idColumnIO,
				ignoredIdRows, StructureSheetGeneric.IO.OUTPUT);

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
}
