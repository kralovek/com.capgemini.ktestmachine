package com.capgemini.ktestmachine.component.testloader.excel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelUtils;
import com.capgemini.ktestmachine.utils.excel.ExcelValueManager;

public abstract class ATestExcelFwk {

	private boolean configured;

	protected Set<String> ignoredSheets;

	private String columnParameter;
	protected int idColumnParameter;

	private String columnIO;
	protected int idColumnIO;

	private Integer rowCode;
	protected int idRowCode;

	private Integer rowActiv;
	protected int idRowActive;

	private Set<Integer> ignoredRows;
	protected Set<Integer> ignoredIdColumns;

	private Set<String> ignoredColumns;
	protected Set<Integer> ignoredIdRows;

	private String dateFormat;
	private DateFormat dateFormatPattern;

	protected ExcelValueManager excelValueManager;

	public void config() throws ConfigurationException {
		configured = false;

		if (ignoredSheets == null) {
			ignoredSheets = new HashSet<String>();
		}

		if (ignoredRows == null) {
			ignoredRows = new HashSet<Integer>();
		}

		if (ignoredColumns == null) {
			ignoredColumns = new HashSet<String>();
		}

		if (columnParameter == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter columnParameter is not configured");
		}
		if (columnIO == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter columnIO is not configured");
		}
		if (rowCode == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter rowCode is not configured");
		}
		if (rowActiv == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter rowActiv is not configured");
		}

		idRowCode = ExcelUtils.getInstance().adaptAndCheckRowId(rowCode,
				"Parameter rowCode");
		idRowActive = ExcelUtils.getInstance().adaptAndCheckRowId(rowActiv,
				"Parameter rowActiv");

		if (idRowCode == idRowActive) {
			throw new ConfigurationException(
					getClass().getSimpleName()
							+ ": Parameters rowActiv and rowCode may not be in the same column: "
							+ rowCode);
		}

		idColumnParameter = ExcelUtils.getInstance().adaptAndCheckColumnId(
				columnParameter, "Parameter columnParameter");
		idColumnIO = ExcelUtils.getInstance().adaptAndCheckColumnId(columnIO,
				"Parameter columnIO");

		if (idColumnParameter == idColumnIO) {
			throw new ConfigurationException(
					getClass().getSimpleName()
							+ ": Parameters columnIO and columnParameter may not be in the same column: "
							+ columnParameter);
		}

		// Adapt ignored rows
		ignoredIdRows = new HashSet<Integer>();
		int i = 0;
		for (final Integer ignoredRow : ignoredRows) {
			final Integer ignoredIdRow = ExcelUtils.getInstance()
					.adaptAndCheckRowId(ignoredRow,
							"Parameter ignoredRow[" + i + "]");
			ignoredIdRows.add(ignoredIdRow);
		}
		ignoredIdRows.add(idRowCode);
		ignoredIdRows.add(idRowActive);

		// Adapt ignored rows
		ignoredIdColumns = new HashSet<Integer>();
		for (final String ignoredColumn : ignoredColumns) {
			final Integer ignoredIdColumn = ExcelUtils.getInstance()
					.adaptAndCheckColumnId(ignoredColumn,
							"Parameter ignoredColumn[" + i + "]");
			ignoredIdColumns.add(ignoredIdColumn);
		}
		ignoredIdColumns.add(idColumnParameter);
		ignoredIdColumns.add(idColumnIO);

		if (dateFormat == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'dateFormat' is not configured");
		} else {
			try {
				dateFormatPattern = new SimpleDateFormat(dateFormat);
				dateFormatPattern.format(new Date());
			} catch (Exception ex) {
				throw new ConfigurationException(
						getClass().getSimpleName()
								+ ": Parameter 'dateFormat' is not a valid date format pattern: ");
			}
		}

		excelValueManager = new ExcelValueManager(dateFormatPattern);

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Set<Integer> getIgnoredRows() {
		return ignoredRows;
	}

	public void setIgnoredRows(Set<Integer> ignoredRows) {
		this.ignoredRows = ignoredRows;
	}

	public Set<String> getIgnoredColumns() {
		return ignoredColumns;
	}

	public void setIgnoredColumns(Set<String> ignoredColumns) {
		this.ignoredColumns = ignoredColumns;
	}

	public String getColumnParameter() {
		return columnParameter;
	}

	public void setColumnParameter(String columnParameter) {
		this.columnParameter = columnParameter;
	}

	public String getColumnIO() {
		return columnIO;
	}

	public void setColumnIO(String columnIO) {
		this.columnIO = columnIO;
	}

	public Integer getRowCode() {
		return rowCode;
	}

	public void setRowCode(Integer rowCode) {
		this.rowCode = rowCode;
	}

	public Set<String> getIgnoredSheets() {
		return ignoredSheets;
	}

	public void setIgnoredSheets(Set<String> ignoredSheets) {
		this.ignoredSheets = ignoredSheets;
	}

	public Integer getRowActiv() {
		return rowActiv;
	}

	public void setRowActiv(Integer rowActiv) {
		this.rowActiv = rowActiv;
	}
}
