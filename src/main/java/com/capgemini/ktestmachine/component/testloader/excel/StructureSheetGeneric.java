package com.capgemini.ktestmachine.component.testloader.excel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.exceladapter.ExcelAdapter;
import com.capgemini.ktestmachine.component.exceladapter.TCell;
import com.capgemini.ktestmachine.component.exceladapter.TSheet;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.utils.excel.ExcelConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelPosition;

public class StructureSheetGeneric {
	private static final Logger LOGGER = Logger
			.getLogger(StructureSheetGeneric.class);

	public static enum IO {
		INPUT, OUTPUT, EXPECT
	}

	public static final String IO_INPUT = "I";

	public static final String IO_OUTPUT = "O";

	public static final String IO_EXPECT = "E";

	public static final String ACTIVE_Y = "Y";

	public static final String ACTIVE_N = "N";

	public static final String STATUS_OK = "OK";

	public static final String STATUS_KO = "KO";

	private ExcelAdapter excelAdapter;

	public StructureSheetGeneric(ExcelAdapter excelAdapter) {
		this.excelAdapter = excelAdapter;
	}

	public Map<Integer, String> loadCodes(ExcelPosition genExcelPosition,
			TSheet sheet, int rowCode, int rowActive,
			Set<Integer> ignoredColumns) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			ExcelPosition excelPosition = genExcelPosition.clone();
			Map<Integer, String> codes = new HashMap<Integer, String>();

			for (int ic = 0; ic <= sheet.getColumnMax(); ic++) {
				if (ignoredColumns != null && ignoredColumns.contains(ic)) {
					continue;
				}
				TCell cellActive = excelAdapter.getCell(sheet, rowActive, ic);
				if (cellActive == null) {
					break;
				}
				String valueActive = excelAdapter.getStringValue(cellActive);
				if (isEmpty(valueActive)) {
					continue;
				}
				if (ACTIVE_N.equals(valueActive)) {
					continue;
				}
				if (!ACTIVE_Y.equals(valueActive)) {
					excelPosition.setRow(rowActive);
					excelPosition.setColumn(ic);
					throw new ExcelConfigurationException(excelPosition,
							"Bad value on the 'Activation' row. Only "
									+ ACTIVE_Y + " or " + ACTIVE_N
									+ " are allowed: '" + valueActive + "'");
				}

				TCell cellCode = excelAdapter.getCell(sheet, rowCode, ic);
				String valueCode = excelAdapter.getStringValue(cellCode);
				if (isEmpty(valueCode)) {
					excelPosition.setRow(rowCode);
					excelPosition.setColumn(ic);
					throw new ExcelConfigurationException(excelPosition,
							"The Test Code is not specified");
				}

				if (codes.containsValue(valueCode)) {
					excelPosition.setRow(rowCode);
					excelPosition.setColumn(ic);
					throw new ExcelConfigurationException(excelPosition,
							"The Test Code is not uniq on the sheet: '"
									+ valueCode + "'");
				}

				codes.put(ic, valueCode);
			}

			LOGGER.trace("OK");
			return codes;
		} finally {
			LOGGER.trace("END");
		}
	}

	public Map<Integer, Boolean> loadStatuses(ExcelPosition genExcelPosition,
			TSheet sheet, int rowStatus, int rowActive,
			Set<Integer> ignoredColumns) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			Map<Integer, Boolean> statuses = new HashMap<Integer, Boolean>();

			for (int ic = 0; ic <= sheet.getColumnMax(); ic++) {
				if (ignoredColumns != null && ignoredColumns.contains(ic)) {
					continue;
				}
				TCell cellStatus = excelAdapter.getCell(sheet, rowActive, ic);
				if (cellStatus == null) {
					break;
				}
				String valueStatus = excelAdapter.getStringValue(cellStatus);
				if (isEmpty(valueStatus)) {
					continue;
				}
				if (STATUS_OK.equals(valueStatus)) {
					statuses.put(ic, Boolean.TRUE);
				} else if (STATUS_KO.equals(valueStatus)) {
					statuses.put(ic, Boolean.FALSE);
				}
			}

			LOGGER.trace("OK");
			return statuses;
		} finally {
			LOGGER.trace("END");
		}
	}

	public Map<Integer, Integer> loadOrders(ExcelPosition excelPosition,
			TSheet sheet, Map<Integer, String> codes, int rowOrder)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			Map<Integer, Integer> orders = new HashMap<Integer, Integer>();

			for (Map.Entry<Integer, String> entry : codes.entrySet()) {
				TCell cellOrder = excelAdapter.getCell(sheet, rowOrder,
						entry.getKey());
				String valueOrder = excelAdapter.getStringValue(cellOrder);
				if (!isEmpty(valueOrder)) {
					Integer valueOrderInt = toInteger(valueOrder);
					if (valueOrderInt == null) {
						excelPosition.setRow(rowOrder);
						excelPosition.setColumn(entry.getKey());
						throw new ExcelConfigurationException(excelPosition,
								"The Test Order is not an integer: '"
										+ valueOrder + "' for the test: "
										+ entry.getValue());
					}

					if (orders.containsValue(valueOrderInt)) {
						excelPosition.setRow(rowOrder);
						excelPosition.setColumn(entry.getKey());
						throw new ExcelConfigurationException(excelPosition,
								"The Test Order is not uniq on the sheet: '"
										+ valueOrderInt + "' for the test: "
										+ entry.getValue());
					}

					orders.put(entry.getKey(), valueOrderInt);
				}
			}

			LOGGER.trace("OK");
			return orders;
		} finally {
			LOGGER.trace("END");
		}
	}

	public Map<Integer, String> loadNames(ExcelPosition excelPosition,
			TSheet sheet, Map<Integer, String> codes, int rowName)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			Map<Integer, String> names = new HashMap<Integer, String>();

			for (Map.Entry<Integer, String> entry : codes.entrySet()) {
				TCell cellName = excelAdapter.getCell(sheet, rowName,
						entry.getKey());
				String valueOrder = excelAdapter.getStringValue(cellName);
				if (!isEmpty(valueOrder)) {
					names.put(entry.getKey(), valueOrder);
				}
			}

			LOGGER.trace("OK");
			return names;
		} finally {
			LOGGER.trace("END");
		}
	}

	public Map<Integer, String> loadParameters(ExcelPosition excelPosition,
			TSheet sheet, int columnParameter, int columnIO,
			Set<Integer> ignoredRows, IO mode) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			String modeIO = //
			mode == IO.INPUT ? IO_INPUT : //
					mode == IO.OUTPUT ? IO_OUTPUT : IO_EXPECT;

			Map<Integer, String> data = new HashMap<Integer, String>();
			for (int ir = 0; ir <= sheet.getRowMax(); ir++) {
				if (ignoredRows != null && ignoredRows.contains(ir)) {
					continue;
				}

				TCell cellIO = excelAdapter.getCell(sheet, ir, columnIO);
				if (cellIO == null) {
					continue;
				}
				String valueIO = excelAdapter.getStringValue(cellIO);
				if (isEmpty(valueIO)) {
					continue;
				}

				if (!IO_OUTPUT.equals(valueIO) && !IO_INPUT.equals(valueIO)
						&& !IO_EXPECT.equals(valueIO)) {
					excelPosition.setRow(ir);
					excelPosition.setColumn(columnIO);
					throw new ExcelConfigurationException(excelPosition,
							"Bad value on the 'IO' column. Only " + IO_INPUT
									+ ", " + IO_EXPECT + " or " + IO_OUTPUT
									+ " are allowed: '" + valueIO + "'");
				}
				if (!modeIO.equals(valueIO)) {
					continue;
				}
				TCell cellParameter = excelAdapter.getCell(sheet, ir,
						columnParameter);
				String valueParameter = excelAdapter
						.getStringValue(cellParameter);
				if (isEmpty(valueParameter)) {
					excelPosition.setRow(ir);
					excelPosition.setColumn(columnParameter);
					throw new ExcelConfigurationException(excelPosition,
							"The Parameter name is not specified");
				}

				if (IO_INPUT.equals(valueIO)
						&& data.containsValue(valueParameter)) {
					excelPosition.setRow(ir);
					excelPosition.setColumn(columnParameter);
					throw new ExcelConfigurationException(excelPosition,
							"The Parameter is already used as INPUT: '"
									+ valueParameter + "'");
				}

				if (IO_EXPECT.equals(valueIO)
						&& data.containsValue(valueParameter)) {
					excelPosition.setRow(ir);
					excelPosition.setColumn(columnParameter);
					throw new ExcelConfigurationException(excelPosition,
							"The Parameter is already used as EXPECT: '"
									+ valueParameter + "'");
				}

				data.put(ir, valueParameter);
			}
			LOGGER.trace("OK");
			return data;
		} finally {
			LOGGER.trace("END");
		}
	}

	private boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	private Integer toInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			return null;
		}
	}
}
