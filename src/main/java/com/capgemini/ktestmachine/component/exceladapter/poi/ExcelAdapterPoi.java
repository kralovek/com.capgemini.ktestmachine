package com.capgemini.ktestmachine.component.exceladapter.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.capgemini.ktestmachine.component.exceladapter.ExcelAdapter;
import com.capgemini.ktestmachine.component.exceladapter.TCell;
import com.capgemini.ktestmachine.component.exceladapter.TSheet;
import com.capgemini.ktestmachine.component.exceladapter.TStyle;
import com.capgemini.ktestmachine.component.exceladapter.TWorkbook;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.capgemini.ktestmachine.utils.file.UtilsFile;

public class ExcelAdapterPoi extends AExcelAdapterPoiFwk implements
		ExcelAdapter {
	private static final Logger LOGGER = Logger
			.getLogger(ExcelAdapterPoi.class);

	private static final long MS_1900_1970 = 2209078800000L;

	public TWorkbook cloneWorkbook(TWorkbook genWorkbookSource, File file)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			if (file == null) {
				throw new IllegalArgumentException(
						"The parameter file may not be null");
			}

			TWorkbookPoi poiWorkbookSource = (TWorkbookPoi) genWorkbookSource;
			TWorkbookPoi poiWorkbookTarget = new TWorkbookPoi();
			poiWorkbookTarget.setFile(file);
			poiWorkbookTarget.setWorkbook(poiWorkbookSource.getWorkbook());
			saveWorkbook(poiWorkbookTarget);
			closeWorkbook(poiWorkbookTarget);

			TWorkbookPoi genWorkbookTarget = readWorkbook(file);

			setDateTimeFormat(genWorkbookTarget);

			// createColorSheet(((TWorkbookPoi)
			// genWorkbookTarget).getWorkbook());

			LOGGER.trace("OK");
			return genWorkbookTarget;
		} finally {
			LOGGER.trace("END");
		}
	}

	public TWorkbook createWorkbook(File file) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			if (file == null) {
				throw new IllegalArgumentException(
						"The parameter file may not be null");
			}

			Workbook workbook = null;
			if (file.getName().toLowerCase().endsWith(".xlsx")) {
				workbook = new XSSFWorkbook();
			} else if (file.getName().toLowerCase().endsWith(".xls")) {
				workbook = new HSSFWorkbook();
			} else {
				new TechnicalException(
						"The extension of the file must be .xlsx or .xls");
			}

			TWorkbookPoi poiWorkbook = new TWorkbookPoi();
			poiWorkbook.setFile(file);
			poiWorkbook.setWorkbook(workbook);

			setDateTimeFormat(poiWorkbook);

			LOGGER.trace("OK");
			return poiWorkbook;
		} finally {
			LOGGER.trace("END");
		}
	}

	private void setDateTimeFormat(TWorkbookPoi poiWorkbook) {
		short dateFormat = poiWorkbook.getWorkbook().getCreationHelper()
				.createDataFormat().getFormat(datePattern.toPattern());
		short timeFormat = poiWorkbook.getWorkbook().getCreationHelper()
				.createDataFormat().getFormat(timePattern.toPattern());

		poiWorkbook.setDateFormat(dateFormat);
		poiWorkbook.setTimeFormat(timeFormat);
	}

	public void saveWorkbook(TWorkbook genWorkbook) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();

			TWorkbookPoi poiWorkbook = (TWorkbookPoi) genWorkbook;
			Workbook workbook = poiWorkbook.getWorkbook();

			createFileDirectory(poiWorkbook.getFile());

			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(poiWorkbook.getFile());
				workbook.write(outputStream);
				outputStream.close();
				outputStream = null;
			} catch (IOException ex) {
				throw new TechnicalException("Cannot write the file: "
						+ poiWorkbook.getFile().getAbsolutePath(), ex);
			} finally {
				UtilsFile.getInstance().close(outputStream);
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	public TWorkbookPoi readWorkbook(File file) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			if (file == null) {
				throw new IllegalArgumentException(
						"The parameter file may not be null");
			}

			TWorkbookPoi poiWorkbook = null;
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(file);
				Workbook workbook = WorkbookFactory.create(fileInputStream);

				FormulaEvaluator evaluator;
				if (workbook instanceof XSSFWorkbook) {
					evaluator = new XSSFFormulaEvaluator(
							(XSSFWorkbook) workbook);
				} else if (workbook instanceof HSSFWorkbook) {
					evaluator = new HSSFFormulaEvaluator(
							(HSSFWorkbook) workbook);
				} else {
					throw new TechnicalException("Unknown type of Workbook: "
							+ workbook.getClass().getSimpleName());
				}

				poiWorkbook = new TWorkbookPoi();
				poiWorkbook.setFile(file);
				poiWorkbook.setWorkbook(workbook);
				poiWorkbook.setInputStream(fileInputStream);
				poiWorkbook.setEvaluator(evaluator);

				LOGGER.trace("OK");
				return poiWorkbook;
			} catch (FileNotFoundException ex) {
				throw new TechnicalException("The excel file does not exist: "
						+ file.getAbsolutePath(), ex);
			} catch (IOException ex) {
				throw new TechnicalException("Cannot read the excel file: "
						+ file.getAbsolutePath(), ex);
			} catch (InvalidFormatException ex) {
				throw new TechnicalException("Bad format of the excel file: "
						+ file.getAbsolutePath(), ex);
			} finally {
				if (poiWorkbook == null) {
					UtilsFile.getInstance().close(fileInputStream);
				}
			}

		} finally {
			LOGGER.trace("END");
		}
	}

	public void closeWorkbook(TWorkbook genWorkbook) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			if (genWorkbook == null) {
				throw new IllegalArgumentException(
						"The parameter workbook may not be null");
			}

			TWorkbookPoi poiWorkbook = (TWorkbookPoi) genWorkbook;

			InputStream inputStream = poiWorkbook.getInputStream();

			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException ex) {
				throw new TechnicalException("Cannot close the workbook: "
						+ poiWorkbook.getFile().getAbsolutePath(), ex);
			} finally {
				UtilsFile.getInstance().close(inputStream);
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	public TSheetPoi getSheet(TWorkbook genWorkbook, String name) {
		testConfigured();
		try {
			if (genWorkbook == null) {
				return null;
			}
			TWorkbookPoi poiWorkbook = (TWorkbookPoi) genWorkbook;
			Workbook workbook = poiWorkbook.getWorkbook();
			Sheet sheet = workbook.getSheet(name);
			if (sheet == null) {
				return null;
			}
			TSheetPoi retval = new TSheetPoi(poiWorkbook);
			retval.setSheet(sheet);
			retval.setName(sheet.getSheetName());
			retval.setEvaluator(poiWorkbook.getEvaluator());
			evaluateSheetRange(retval);
			return retval;
		} catch (Exception ex) {
			throw new IllegalArgumentException(
					"Incorrect genWorkbook parameter", ex);
		}
	}

	public TSheetPoi getSheet(TWorkbook genWorkbook, int index) {
		testConfigured();
		try {
			if (genWorkbook == null) {
				return null;
			}
			TWorkbookPoi poiWorkbook = (TWorkbookPoi) genWorkbook;
			Workbook workbook = poiWorkbook.getWorkbook();

			Sheet sheet = workbook.getSheetAt(index);
			if (sheet == null) {
				return null;
			}
			TSheetPoi retval = new TSheetPoi(poiWorkbook);
			retval.setSheet(sheet);
			retval.setName(sheet.getSheetName());
			retval.setEvaluator(poiWorkbook.getEvaluator());
			evaluateSheetRange(retval);
			return retval;
		} catch (Exception ex) {
			throw new IllegalArgumentException(
					"Incorrect genWorkbook parameter", ex);
		}
	}

	public int getSheetCount(TWorkbook genWorkbook) {
		testConfigured();
		try {
			if (genWorkbook == null) {
				return 0;
			}
			TWorkbookPoi poiWorkbook = (TWorkbookPoi) genWorkbook;
			Workbook workbook = poiWorkbook.getWorkbook();

			return workbook.getNumberOfSheets();
		} catch (Exception ex) {
			throw new IllegalArgumentException(
					"Incorrect genWorkbook parameter", ex);
		}
	}

	public TSheet createSheet(TWorkbook genWorkbook, String name) {
		testConfigured();
		try {
			TSheet genSheet = getSheet(genWorkbook, name);
			if (genSheet != null) {
				return genSheet;
			}
			TWorkbookPoi poiWorkbook = (TWorkbookPoi) genWorkbook;
			Workbook workbook = poiWorkbook.getWorkbook();

			Sheet sheet = workbook.createSheet(name);
			TSheetPoi poiSheet = new TSheetPoi(poiWorkbook);

			poiSheet.setSheet(sheet);
			return poiSheet;
		} catch (Exception ex) {
			throw new IllegalArgumentException(
					"Incorrect genWorkbook parameter", ex);
		}
	}

	public TCellPoi getCell(TSheet genSheet, int irow, int icolumn) {
		testConfigured();
		try {
			if (genSheet == null) {
				return null;
			}
			TSheetPoi poiSheet = (TSheetPoi) genSheet;
			Sheet sheet = poiSheet.getSheet();

			TCellPoi retval = new TCellPoi(poiSheet);
			retval.setColumn(icolumn);
			retval.setRow(irow);
			retval.setEvaluator(poiSheet.getEvaluator());

			Cell cell = null;
			Row row = sheet.getRow(irow);
			if (row != null) {
				cell = row.getCell(icolumn);
			}
			retval.setCell(cell);
			return retval;
		} catch (Exception ex) {
			throw new IllegalArgumentException("Incorrect genSheet parameter",
					ex);
		}
	}

	public Object getValue(TCell genCell) {
		testConfigured();
		if (genCell == null) {
			return null;
		}
		TCellPoi poiCell = (TCellPoi) genCell;
		Cell cell = poiCell.getCell();

		if (cell == null) {
			return null;
		}

		FormulaEvaluator evaluator = poiCell.getEvaluator();
		CellValue cellValue = evaluator.evaluate(cell);

		if (cellValue == null) {
			return null;
		}

		switch (cellValue.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cellValue.getBooleanValue();
		case Cell.CELL_TYPE_NUMERIC: {
			Date date = toDate(cell, cellValue);
			if (date == null) {
				Double valueDouble = cellValue.getNumberValue();
				long valueLong = valueDouble.longValue();
				Double valueLongDouble = new Double(valueLong);
				if (valueDouble.equals(valueLongDouble)) {
					return valueLong;
				} else {
					return valueDouble;
				}
			} else {
				return date;
			}
		}
		case Cell.CELL_TYPE_STRING:
		default:
			return cellValue.getStringValue();
		}
	}

	private Date toDate(Cell cell, CellValue cellValue) {
		if (cellValue == null) {
			return null;
		}
		CellStyle cellStyle = cell.getCellStyle();
		String dataFormatString = cellStyle.getDataFormatString();

		if (dataFormatString == null || dataFormatString.isEmpty()) {
			return null;
		}

		if (dataFormatString.matches(".*[dMyhmsS].*")) {
			double value = cellValue.getNumberValue();
			long ms = (long) (value * 24 * 60 * 60 * 1000);
			Date date = new Date(ms - MS_1900_1970);
			return date;
		} else {
			return null;
		}
	}

	public String getStringValue(TCell genCell) {
		testConfigured();
		Object object = getValue(genCell);
		if (object == null) {
			return null;
		}

		return String.valueOf(object);
	}

	public void setCellStyle(TCell genCell, TStyle genStyle) {
		TCellPoi poiCell = (TCellPoi) genCell;
		Cell cell = getCellFromPoi(poiCell, genCell.getRow(),
				genCell.getColumn());

		TStylePoi poiStyle = (TStylePoi) genStyle;
		CellStyle cellStyle = poiStyle.getCellStyle();

		CellStyle cellStyleOrg = cell.getCellStyle();
		cellStyleOrg.getDataFormat();

		cell.setCellStyle(cellStyle);
		cellStyle.setDataFormat(cellStyleOrg.getDataFormat());
	}

	private Cell getCellFromPoi(TCellPoi poiCell, int idRow, int idColumn) {
		Cell cell = poiCell.getCell();
		if (cell == null) {
			TSheetPoi poiSheet = poiCell.getPoiSheet();
			Sheet sheet = poiSheet.getSheet();
			Row row = sheet.getRow(idRow);
			if (row == null) {
				row = sheet.createRow(idRow);
			}
			cell = row.getCell(idColumn);
			if (cell == null) {
				cell = row.createCell(idColumn, Cell.CELL_TYPE_BLANK);
			}
		}
		return cell;
	}

	public void copyCellStyle(TCell genCellSource, TCell genCellTarget) {
		if (genCellTarget != null && genCellSource != null) {
			TCellPoi poiCellSource = (TCellPoi) genCellSource;
			Cell cellSource = poiCellSource.getCell();
			TCellPoi poiCellTarget = (TCellPoi) genCellTarget;
			Cell cellTarget = poiCellTarget.getCell();

			cellTarget.setCellStyle(cellSource.getCellStyle());
		}
	}

	public void setValue(TSheet genSheet, TCell genCell, TCell genCellTemplate, Object value) {
		setValue(genSheet, genCell, value);
		if (genCellTemplate != null) {
			TCellPoi poiCellTemplate = (TCellPoi) genCellTemplate;
			Cell cellTemplate = poiCellTemplate.getCell();
			
			TCellPoi poiCell = (TCellPoi) genCell;
			Cell cell = poiCell.getCell();

			if (cell != null && cellTemplate != null) {
				CellStyle cellStyle = cell.getCellStyle();
				CellStyle cellStyleTemplate = cellTemplate.getCellStyle();
				
				cellStyle.setDataFormat(cellStyleTemplate.getDataFormat());
			}
			
		}
	}

	public void setValue(TSheet genSheet, TCell genCell, Object value) {
		TSheetPoi poiSheet = (TSheetPoi) genSheet;
		Sheet sheet = poiSheet.getSheet();
		TCellPoi poiCell = (TCellPoi) genCell;
		Cell cell = poiCell.getCell();
		TWorkbookPoi poiWorkbook = poiSheet.getPoiWorkbook();

		if (cell == null) {
			Row row = sheet.getRow(genCell.getRow());
			if (row == null) {
				row = sheet.createRow(genCell.getRow());
			}
			cell = row.getCell(genCell.getColumn());
			if (cell == null) {
				cell = row.createCell(genCell.getColumn());
				poiCell.setCell(cell);
			}
		}

		if (value == null) {
			cell.setCellValue((String) null);
		} else {
			if (value instanceof Date) {
				Date valueDate = (Date) value;
				cell.setCellValue(valueDate);
				CellStyle cellStyle = cell.getCellStyle();
				cellStyle.setDataFormat(poiWorkbook.getDateFormat());
			} else if (value instanceof Number) {
				Number valueNumber = (Number) value;
				cell.setCellValue(valueNumber.doubleValue());
			} else if (value instanceof Boolean) {
				Boolean valueBoolean = (Boolean) value;
				cell.setCellValue(valueBoolean);
			} else {
				String valueString = (String) value;
				cell.setCellValue(valueString);
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

	private void evaluateSheetRange(TSheetPoi poiSheet) {
		Sheet sheet = poiSheet.getSheet();
		if (sheet == null) {
			poiSheet.setRowMin(0);
			poiSheet.setRowMax(0);
			poiSheet.setColumnMin(0);
			poiSheet.setColumnMax(0);
		} else {
			poiSheet.setRowMin(sheet.getFirstRowNum());
			poiSheet.setRowMax(sheet.getLastRowNum());
			Short columnMin = null;
			Short columnMax = null;
			for (int ir = sheet.getFirstRowNum(); ir <= sheet.getLastRowNum(); ir++) {
				Row row = sheet.getRow(ir);
				if (row == null) {
					continue;
				}
				if (columnMin == null) {
					columnMin = row.getFirstCellNum();
				}
				if (columnMax == null) {
					columnMax = row.getLastCellNum();
				}
				if (row.getFirstCellNum() < columnMin) {
					columnMin = row.getFirstCellNum();
				}
				if (row.getLastCellNum() > columnMax) {
					columnMax = row.getLastCellNum();
				}
			}
			if (columnMin == null) {
				columnMin = 0;
			}
			if (columnMax == null) {
				columnMax = 0;
			}
			poiSheet.setColumnMin(columnMin);
			poiSheet.setColumnMax(columnMax);
		}
	}

	private CellStyle loadStyle(Workbook workbook) {
		Sheet sheet = workbook.getSheet("STYLE");
		Row row = sheet.getRow(0);
		Cell cell = row.getCell(0);
		return cell.getCellStyle();
	}

	private void stylePokus(Workbook workbook) {
		LOGGER.trace("BEGIN");
		try {
			Sheet sheet = workbook.getSheet("STYLE");

			Row row = sheet.getRow(0);

			Cell cell = row.getCell(0);

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void createColorSheet(Workbook workbook) {
		LOGGER.trace("BEGIN");
		try {
			Sheet sheet = workbook.createSheet("COLORS");

			Row row = sheet.createRow((short) 1);
			CellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			Cell cell = row.createCell((short) 2);
			cell.setCellValue("X");
			cell.setCellStyle(style);

			Row roww = sheet.getRow((short) 0);
			if (roww == null) {
				roww = sheet.createRow((short) 0);
			}
			Cell cellIndex = row.getCell(0);
			if (cellIndex == null) {
				cellIndex = roww.createCell(0, Cell.CELL_TYPE_NUMERIC);
				cellIndex.setCellValue("X");
			}

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN
					.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// for (short ip = 0; ip <= 0; ip++) {
			Cell cellColor = roww.getCell(1);
			if (cellColor == null) {
				cellColor = roww.createCell(1, Cell.CELL_TYPE_STRING);
			}
			cellColor.setCellValue("value");
			cellColor.setCellStyle(cellStyle);
			// }

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void printCellStyle(XSSFCellStyle cellStyle) {
		LOGGER.trace("BEGIN");
		try {
			cellStyle.setFillBackgroundColor((short) 64);
			cellStyle.setFillForegroundColor((short) 64);
			cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

			// cellStyle = (XSSFCellStyle) cellStyleFile;

			LOGGER.debug("getAlignment: " + cellStyle.getAlignment());
			LOGGER.debug("getAlignmentEnum: " + cellStyle.getAlignmentEnum());
			LOGGER.debug("getBorderBottom: " + cellStyle.getBorderBottom());
			LOGGER.debug("getBorderBottomEnum: "
					+ cellStyle.getBorderBottomEnum());
			// cellStyle.getBorderColor(side);
			LOGGER.debug("getBorderLeft: " + cellStyle.getBorderLeft());
			LOGGER.debug("getBorderLeftEnum: " + cellStyle.getBorderLeftEnum());
			LOGGER.debug("getBorderRight: " + cellStyle.getBorderRight());
			LOGGER.debug("getBorderRightEnum: "
					+ cellStyle.getBorderRightEnum());
			LOGGER.debug("getBorderTop: " + cellStyle.getBorderTop());
			LOGGER.debug("getBorderTopEnum: " + cellStyle.getBorderTopEnum());
			LOGGER.debug("getBottomBorderColor: "
					+ cellStyle.getBottomBorderColor());
			LOGGER.debug("getBottomBorderXSSFColor: "
					+ cellStyle.getBottomBorderXSSFColor());
			LOGGER.debug("getCoreXf: " + cellStyle.getCoreXf());
			LOGGER.debug("getDataFormat: " + cellStyle.getDataFormat());
			LOGGER.debug("getDataFormatString: "
					+ cellStyle.getDataFormatString());
			LOGGER.debug("getFillBackgroundColor: "
					+ cellStyle.getFillBackgroundColor());
			LOGGER.debug("getFillBackgroundColorColor: "
					+ cellStyle.getFillBackgroundColorColor());
			LOGGER.debug("getFillBackgroundXSSFColor: "
					+ cellStyle.getFillBackgroundXSSFColor());
			LOGGER.debug("getFillForegroundColor: "
					+ cellStyle.getFillForegroundColor());
			LOGGER.debug("getFillForegroundColorColor: "
					+ cellStyle.getFillForegroundColorColor());
			LOGGER.debug("getFillForegroundXSSFColor: "
					+ cellStyle.getFillForegroundXSSFColor());
			LOGGER.debug("getFillPattern: " + cellStyle.getFillPattern());
			LOGGER.debug("getFillPatternEnum: "
					+ cellStyle.getFillPatternEnum());
			LOGGER.debug("getFont: " + cellStyle.getFont());
			LOGGER.debug("getFontIndex: " + cellStyle.getFontIndex());
			LOGGER.debug("getHidden: " + cellStyle.getHidden());
			LOGGER.debug("getIndention: " + cellStyle.getIndention());
			LOGGER.debug("getIndex: " + cellStyle.getIndex());
			LOGGER.debug("getLeftBorderColor: "
					+ cellStyle.getLeftBorderColor());
			LOGGER.debug("getLeftBorderXSSFColor: "
					+ cellStyle.getLeftBorderXSSFColor());
			LOGGER.debug("getLocked: " + cellStyle.getLocked());
			LOGGER.debug("getRightBorderColor: "
					+ cellStyle.getRightBorderColor());
			LOGGER.debug("getRightBorderXSSFColor: "
					+ cellStyle.getRightBorderXSSFColor());
			LOGGER.debug("getRotation: " + cellStyle.getRotation());
			LOGGER.debug("getShrinkToFit: " + cellStyle.getShrinkToFit());
			LOGGER.debug("getStyleXf: " + cellStyle.getStyleXf());
			LOGGER.debug("getTopBorderColor: " + cellStyle.getTopBorderColor());
			LOGGER.debug("getTopBorderXSSFColor: "
					+ cellStyle.getTopBorderXSSFColor());
			LOGGER.debug("getVerticalAlignment: "
					+ cellStyle.getVerticalAlignment());
			LOGGER.debug("getVerticalAlignmentEnum: "
					+ cellStyle.getVerticalAlignmentEnum());
			LOGGER.debug("getWrapText: " + cellStyle.getWrapText());
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}
}
