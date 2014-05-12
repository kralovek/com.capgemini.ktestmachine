package com.capgemini.ktestmachine.component.structureexcel;

import java.util.HashSet;
import java.util.Set;

import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelUtils;



public abstract class AStructureExcelFwk {
    private boolean configured;

	private String idColumnParameters;

	private String idColumnIO;

	private Integer idRowCode;

	private Integer idRowActiv;

	private Integer idRowOrder;

	private Set<Integer> ignoredRows;

	private Set<String> ignoredColumns;

	//
	// Adapted parameters
	//

	protected int idColumnParametersAdapted;

	protected int idColumnIOAdapted;

	protected int idRowCodeAdapted;

	protected int idRowActivAdapted;

	protected Integer idRowOrderAdapted;

	protected Set<Integer> ignoredColumnsAdapted;

	protected Set<Integer> ignoredRowsAdapted;

	public void config() throws ConfigurationException {
		configured = false;

		if (ignoredRows == null) {
			ignoredRows = new HashSet<Integer>();
		}

		if (ignoredColumns == null) {
			ignoredColumns = new HashSet<String>();
		}

		if (idColumnParameters == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter idColumnParameter is not configured");
		}
		if (idColumnIO == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter idColumnIO is not configured");
		}
		if (idRowCode == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter idRowCode is not configured");
		}
		if (idRowActiv == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter idRowActive is not configured");
		}
		if (idRowOrder != null) {
			idRowOrderAdapted = ExcelUtils.getInstance().adaptAndCheckRowId(
					idRowOrder, "Parameter idRowCode");
		}

		idRowCodeAdapted = ExcelUtils.getInstance().adaptAndCheckRowId(
				idRowCode, "Parameter idRowCode");
		idRowActivAdapted = ExcelUtils.getInstance().adaptAndCheckRowId(
				idRowActiv, "Parameter idRowActive");

		idColumnParametersAdapted = ExcelUtils.getInstance()
				.adaptAndCheckColumnId(idColumnParameters,
						"Parameter idColumnParameter");
		idColumnIOAdapted = ExcelUtils.getInstance().adaptAndCheckColumnId(
				idColumnIO, "Parameter idColumnIO");

		// Adapt ignored rows
		ignoredRowsAdapted = new HashSet<Integer>();
		int i = 0;
		for (final Integer ignoredRow : ignoredRows) {
			final Integer ignoredRowAdapted = ExcelUtils.getInstance()
					.adaptAndCheckRowId(ignoredRow,
							"Parameter ignoredRow[" + i + "]");
			ignoredRowsAdapted.add(ignoredRowAdapted);
		}

		// Adapt ignored rows
		ignoredColumnsAdapted = new HashSet<Integer>();
		for (final String ignoredColumn : ignoredColumns) {
			final Integer ignoredColumnAdapted = ExcelUtils.getInstance()
					.adaptAndCheckColumnId(ignoredColumn,
							"Parameter ignoredColumn[" + i + "]");
			ignoredColumnsAdapted.add(ignoredColumnAdapted);
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
		}
	}

	public String getIdColumnParameters() {
		return idColumnParameters;
	}

	public void setIdColumnParameters(String idColumnParameters) {
		this.idColumnParameters = idColumnParameters;
	}

	public String getIdColumnIO() {
		return idColumnIO;
	}

	public void setIdColumnIO(String idColumnIO) {
		this.idColumnIO = idColumnIO;
	}

	public Integer getIdRowCode() {
		return idRowCode;
	}

	public void setIdRowCode(Integer idRowCode) {
		this.idRowCode = idRowCode;
	}

	public Integer getIdRowActiv() {
		return idRowActiv;
	}

	public void setIdRowActiv(Integer idRowActiv) {
		this.idRowActiv = idRowActiv;
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

	public Integer getIdRowOrder() {
		return idRowOrder;
	}

	public void setIdRowOrder(Integer idRowOrder) {
		this.idRowOrder = idRowOrder;
	}
}
