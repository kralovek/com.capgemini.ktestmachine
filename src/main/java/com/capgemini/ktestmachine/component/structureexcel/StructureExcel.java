package com.capgemini.ktestmachine.component.structureexcel;

import java.util.Map;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.utils.excel.ExcelConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelPosition;

import jxl.Cell;
import jxl.Sheet;

public class StructureExcel extends AStructureExcelFwk {
	private static final Logger LOGGER = Logger.getLogger(StructureExcel.class);

	private static final String IO_INPUT = "I";

	private static final String IO_OUTPUT = "O";

	private static final String IO_EXPECT = "E";

	private static final String ACTIF_Y = "Y";

	private static final String ACTIF_N = "N";

	public StructureSheet loadStructure(final Sheet pSheet,
			final ExcelPosition pExcelPosition) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();

			final StructureSheet structureSheet = new StructureSheet();

			if (idColumnIOAdapted + 1 > pSheet.getColumns()) {
				throw new ExcelConfigurationException(pExcelPosition,
						"The IO Column is not in the sheet. The sheet has only " + pSheet.getColumns() + " columns.");
			}

			if (idRowActivAdapted + 1 > pSheet.getRows()) {
				throw new ExcelConfigurationException(pExcelPosition,
						"The Activ Row is not in the sheet. The sheet has only " + pSheet.getRows() + " rows.");
			}

			final Cell[] columnIO = pSheet.getColumn(idColumnIOAdapted);
			final Cell[] rowActiv = pSheet.getRow(idRowActivAdapted);

			for (final Cell cell : columnIO) {
				if (isRowIgnored(cell.getRow())) {
					continue;
				}
				final String content = cell.getContents(); // remove trim()
				if (!content.isEmpty()) {
					final Cell cellParameter = pSheet.getCell(
							idColumnParametersAdapted, cell.getRow());
					if (cellParameter.getContents().isEmpty()) {
						pExcelPosition.setColumn(cellParameter.getColumn());
						pExcelPosition.setRow(cellParameter.getRow());
						throw new ExcelConfigurationException(pExcelPosition,
								"No parameter name is specified");
					}
					if (IO_INPUT.equals(content)) {
						if (structureSheet.getInput().containsValue(
								cellParameter.getContents())) {
							pExcelPosition.setColumn(cellParameter.getColumn());
							pExcelPosition.setRow(cellParameter.getRow());
							throw new ExcelConfigurationException(
									pExcelPosition,
									"INPUT parameter is not unique: "
											+ cellParameter.getContents());
						}
						structureSheet.getInput().put(cell.getRow(),
								cellParameter.getContents());
					} else if (IO_EXPECT.equals(content)) {
						if (structureSheet.getExpect().containsValue(
								cellParameter.getContents())) {
							pExcelPosition.setColumn(cellParameter.getColumn());
							pExcelPosition.setRow(cellParameter.getRow());
							throw new ExcelConfigurationException(
									pExcelPosition,
									"EXPECTED parameter is not unique: "
											+ cellParameter.getContents());
						}
						structureSheet.getExpect().put(cell.getRow(),
								cellParameter.getContents());
					} else if (IO_OUTPUT.equals(content)) {
						structureSheet.getOutput().put(cell.getRow(),
								cellParameter.getContents());
					} else {
						pExcelPosition.setColumn(cellParameter.getColumn());
						pExcelPosition.setRow(cellParameter.getRow());
						throw new ExcelConfigurationException(pExcelPosition,
								"Unknown IO identificator [" + IO_INPUT + "|"
										+ IO_EXPECT + "|" + IO_OUTPUT + "]: "
										+ content);
					}
				}
			}

			for (final Cell cell : rowActiv) {
				if (isColumnIgnored(cell.getColumn())) {
					continue;
				}
				final String content = cell.getContents(); // remove trim()
				if (content.length() != 0) {
					if (ACTIF_Y.equals(content)) {
						final Cell cellCode = pSheet.getCell(cell.getColumn(),
								idRowCodeAdapted);
						final String code = cellCode.getContents();
						final Cell cellOrder = pSheet.getCell(cell.getColumn(),
								idRowOrderAdapted);
						if (isEmpty(cellCode.getContents())) {
							pExcelPosition.setColumn(cellCode.getColumn());
							pExcelPosition.setRow(cellCode.getRow());
							throw new ExcelConfigurationException(
									pExcelPosition, "The test code is empty");
						} else if (structureSheet.getTestIds().containsValue(
								cellCode.getContents())) {
							pExcelPosition.setColumn(cellCode.getColumn());
							pExcelPosition.setRow(cellCode.getRow());
							throw new ExcelConfigurationException(
									pExcelPosition,
									"Duplicity in the test code: "
											+ cellCode.getContents());
						}
						structureSheet.getTestIds().put(cell.getColumn(), code);

						if (!isEmpty(cellOrder.getContents())) {
							final Integer order = toInteger(cellOrder
									.getContents());
							if (order == null) {
								pExcelPosition.setColumn(cellOrder.getColumn());
								pExcelPosition.setRow(cellOrder.getRow());
								throw new ExcelConfigurationException(
										pExcelPosition,
										"The order is specified but it's not an integer: "
												+ cellOrder.getContents());
							} else if (structureSheet.getTestOrders()
									.containsValue(order)) {
								pExcelPosition.setColumn(cellOrder.getColumn());
								pExcelPosition.setRow(cellOrder.getRow());
								throw new ExcelConfigurationException(
										pExcelPosition,
										"The order of the test "
												+ code
												+ " is already specified on this sheet");
							}
							structureSheet.getTestOrders().put(
									cell.getColumn(), order);
						} else {
							structureSheet.getTestOrders().put(
									cell.getColumn(), null);
						}

					} else if (ACTIF_N.equals(content)) {
					} else {
						pExcelPosition.setColumn(cell.getColumn());
						pExcelPosition.setRow(cell.getRow());
						throw new ExcelConfigurationException(pExcelPosition,
								"Unknown Actif identificator [Y|N]: " + content);
					}
				}
			}

			validateStructure(structureSheet, pExcelPosition);

			LOGGER.trace("OK");
			return structureSheet;
		} finally {
			LOGGER.trace("END");
		}
	}

	private boolean isRowIgnored(final int pIndex) {
		for (final Integer ignoredRow : ignoredRowsAdapted) {
			if (pIndex == ignoredRow) {
				return true;
			}
		}
		return pIndex == idRowActivAdapted || pIndex == idRowCodeAdapted
				|| pIndex == idRowOrderAdapted;
	}

	private boolean isColumnIgnored(final int pIndex) {
		for (final Integer ignoredColumn : ignoredColumnsAdapted) {
			if (pIndex == ignoredColumn) {
				return true;
			}
		}
		return //
		pIndex == idColumnIOAdapted || //
				pIndex == idColumnParametersAdapted;
	}

	private void validateStructure(final StructureSheet pStructure,
			final ExcelPosition pExcelPosition) throws ABaseException {
		for (final Map.Entry<Integer, String> entry : pStructure.getExpect()
				.entrySet()) {
			if (!pStructure.getOutput().containsValue(entry.getValue())) {
				throw new ExcelConfigurationException(pExcelPosition,
						"An EXPECTED parameter is not defined in the OUTPUT parameter list: "
								+ entry.getValue());
			}
		}
	}

	private boolean isEmpty(final String pContent) {
		return pContent == null || pContent.isEmpty(); // remove trim()
	}

	private Integer toInteger(final String pContent) {
		try {
			return Integer.parseInt(pContent);
		} catch (final NumberFormatException ex) {
			return null;
		}
	}
	
	public static SheetName createSheetName(String name, final ExcelPosition pExcelPosition) throws ABaseException {
		SheetName sheetName = new SheetName();
		
		if (name.startsWith("(N)")) {
			sheetName.setActiv(false);
			name = name.substring(3);
		}
		
//		sheetName.setName(name);
//		return sheetName;
		
		int iPosOpen = name.indexOf('(');
		int iPosClose = name.lastIndexOf(')');
		
		if (iPosOpen == -1 || iPosClose == -1 ) {
			sheetName.setName(name);
			return sheetName;
		}
		if (iPosOpen < 1 || iPosClose < 2 || iPosOpen > iPosClose || iPosOpen +1 == iPosClose) {
			throw new ExcelConfigurationException(pExcelPosition,
					"Bad format of the sheet name: bad position of index bracets(): "
							+ name);
		}
		String index = name.substring(iPosOpen + 1, iPosClose);
		name = name.substring(0, iPosOpen);

		if (name.indexOf('(') != -1 || name.indexOf(')') != -1 || index.indexOf('(') != -1 || index.indexOf(')') != -1) {
			throw new ExcelConfigurationException(pExcelPosition,
					"Bad format of the sheet name: bad position of index bracets(): "
							+ name);
		}
		
		sheetName.setName(name);
		sheetName.setIndex(index);
		return sheetName;
	}
}