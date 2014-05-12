package com.capgemini.ktestmachine.component.exceladapter.poi;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.capgemini.ktestmachine.component.exceladapter.ExcelStyles;
import com.capgemini.ktestmachine.component.exceladapter.ExcelStylesFactory;
import com.capgemini.ktestmachine.component.exceladapter.TWorkbook;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.utils.excel.ExcelConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelIdCell;
import com.capgemini.ktestmachine.utils.excel.ExcelPosition;

public class ExcelStylesFactoryPoiSheet extends AExcelStylesFactoryPoiSheetFwk
		implements ExcelStylesFactory {
	private static final Logger LOGGER = Logger
			.getLogger(ExcelStylesFactoryPoiSheet.class);

	public ExcelStyles getStyles(TWorkbook genWorkbook)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();

			TWorkbookPoi poiWorkbook = (TWorkbookPoi) genWorkbook;
			Workbook workbook = poiWorkbook.getWorkbook();

			ExcelStylesPoi excelStyles = new ExcelStylesPoi();

			ExcelPosition excelPosition = new ExcelPosition();
			excelPosition.setFile(genWorkbook.getFile());

			Sheet styleSheet = workbook.getSheet(sheet);
			if (styleSheet == null) {
				throw new ExcelConfigurationException(excelPosition,
						"The sheet does not exist in the file: " + sheet);
			}

			excelPosition.setSheet(sheet);

			TStylePoi style;
			style = getStyle(excelPosition, styleSheet, idCellTitleOK);
			excelStyles.setStyleTitleOK(style);
			style = getStyle(excelPosition, styleSheet, idCellTitleKO);
			excelStyles.setStyleTitleKO(style);
			style = getStyle(excelPosition, styleSheet, idCellStatOK);
			excelStyles.setStyleStatOK(style);
			style = getStyle(excelPosition, styleSheet, idCellStatKO);
			excelStyles.setStyleStatKO(style);
			style = getStyle(excelPosition, styleSheet, idCellFieldExpectOK);
			excelStyles.setStyleFieldExpectOK(style);
			style = getStyle(excelPosition, styleSheet, idCellFieldExpectKO);
			excelStyles.setStyleFieldExpectKO(style);
			style = getStyle(excelPosition, styleSheet, idCellFieldOutputOK);
			excelStyles.setStyleFieldOutputOK(style);
			style = getStyle(excelPosition, styleSheet, idCellFieldOutputKO);
			excelStyles.setStyleFieldOutputKO(style);

			LOGGER.trace("OK");
			return excelStyles;
		} finally {
			LOGGER.trace("END");
		}
	}

	private TStylePoi getStyle(ExcelPosition excelPosition, Sheet sheet,
			ExcelIdCell idCell) throws ABaseException {
		Row row = sheet.getRow(idCell.getRow());
		if (row == null) {
			throw new ExcelConfigurationException(excelPosition, "The cell does not exist in the sheet: " + idCell.toString());
		}
		Cell cell = row.getCell(idCell.getColumn());
		if (cell == null) {
			throw new ExcelConfigurationException(excelPosition, "The cell does not exist in the sheet: " + idCell.toString());
		}
		CellStyle cellStyle = cell.getCellStyle();
		TStylePoi poiStyle = new TStylePoi(cellStyle);
		return poiStyle;
	}

	public void stylesNwExcelStylesPoi(TWorkbook genWorkbook) {
		TWorkbookPoi poiWorkbook = (TWorkbookPoi) genWorkbook;

		TStylePoi styleTitleOK = new TStylePoi(poiWorkbook.getWorkbook(),
				IndexedColors.GREEN.getIndex());
		TStylePoi styleTitleKO = new TStylePoi(poiWorkbook.getWorkbook(),
				IndexedColors.RED.getIndex());
		TStylePoi styleFieledExpOK = new TStylePoi(poiWorkbook.getWorkbook(),
				IndexedColors.LIGHT_GREEN.getIndex());
		TStylePoi styleFieledExpKO = new TStylePoi(poiWorkbook.getWorkbook(),
				IndexedColors.CORAL.getIndex());
		TStylePoi styleFieledOutOK = new TStylePoi(poiWorkbook.getWorkbook(),
				IndexedColors.LIGHT_GREEN.getIndex());
		TStylePoi styleFieledOutKO = new TStylePoi(poiWorkbook.getWorkbook(),
				IndexedColors.TAN.getIndex());
	}
}
