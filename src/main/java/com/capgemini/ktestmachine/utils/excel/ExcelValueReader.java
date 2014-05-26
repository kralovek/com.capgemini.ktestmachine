package com.capgemini.ktestmachine.utils.excel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.capgemini.ktestmachine.exception.ABaseException;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;

public class ExcelValueReader {

	private static ExcelValueReader excelValueReader = new ExcelValueReader();

	public static ExcelValueReader getInstance() {
		return excelValueReader;
	}

	/**
	 * Value date format
	 */
	private static final DateFormat VALUE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.FRANCE);

	public Map<String, Value> readValues(final ExcelPosition pExcelPosition,
			final Sheet pSheet, final int pColumn,
			final Map<Integer, String> pParameters) throws ABaseException {
		final Map<String, Value> values = new HashMap<String, Value>();

		for (Map.Entry<Integer, String> entry : pParameters.entrySet()) {
			final Integer row = entry.getKey();
			final String parameter = entry.getValue();

			final Cell cell = pSheet.getCell(pColumn, row);
			try {
				final Value value = cellToValue(cell);
				values.put(parameter, value);
			} catch (final Exception ex) {
				pExcelPosition.setRow(row);
				pExcelPosition.setColumn(pColumn);
				throw new ExcelConfigurationException(pExcelPosition,
						ex.getMessage());
			}
		}
		return values;
	}

	private Value cellToValue(final Cell pCell) throws Exception {
		if (pCell instanceof NumberCell) {
			return cellToValueNumber((NumberCell) pCell);
		} else if (pCell instanceof DateCell) {
			return cellToValueDate((DateCell) pCell);
		} else if (pCell instanceof BooleanCell) {
			return cellToValueBoolean((BooleanCell) pCell);
		} else {
			return cellToValueString(pCell);
		}
	}

	private static Value cellToValueNumber(final NumberCell pNumberCell) {
		final double value = pNumberCell.getValue();
		String valueString = String.valueOf(value).replace(",", ".");
		if (valueString.indexOf('.') != -1) {
			valueString = valueString.replaceAll("0*$", "");
			valueString = valueString.replaceAll("\\.$", "");
		}
		Object object = new Object[] { valueString };
		Value retval = new Value();
		retval.setValue(object);
		return retval;
	}

	private static Value cellToValueBoolean(final BooleanCell pBooleanCell) {
		Object object = new Object[] { String.valueOf(pBooleanCell.getValue()) };
		Value retval = new Value();
		retval.setValue(object);
		return retval;
	}

	private static Value cellToValueDate(final DateCell pDateCell) {
		Object object = new Object[] { VALUE_DATE_FORMAT.format(pDateCell
				.getDate()) };
		Value retval = new Value();
		retval.setValue(object);
		return retval;
	}

	private static Value cellToValueString(final Cell pCell) throws Exception {
		String value = pCell.getContents().trim();
		Value retval = ValueParser.parseValue(value);
		return retval;
	}
	
}
