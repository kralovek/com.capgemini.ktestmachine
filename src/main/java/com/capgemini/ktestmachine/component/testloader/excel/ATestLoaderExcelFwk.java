package com.capgemini.ktestmachine.component.testloader.excel;

import com.capgemini.ktestmachine.component.exceladapter.ExcelAdapter;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelUtils;

public abstract class ATestLoaderExcelFwk extends ATestExcelFwk {

	private boolean configured;

	protected ExcelAdapter excelAdapter;

	// Not configurable
	protected StructureSheetGeneric structureSheet;

	private Integer rowName;
	protected Integer idRowName;

	private Integer rowDescription;
	protected Integer idRowDescription;

	//
	// Format
	//
	private Integer rowOrder;
	protected Integer idRowOrder;

	public void config() throws ConfigurationException {
		configured = false;
		super.config();
		if (excelAdapter == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'excelAdapter' is not configured");
		}
		if (rowOrder != null) {
			idRowOrder = ExcelUtils.getInstance().adaptAndCheckRowId(rowOrder,
					"Parameter _idRowCode");
		}
		structureSheet = new StructureSheetGeneric(excelAdapter);

		idRowName = ExcelUtils.getInstance().adaptAndCheckRowId(rowName,
				"Parameter 'rowName'");
		idRowDescription = ExcelUtils.getInstance().adaptAndCheckRowId(
				rowDescription, "Parameter 'rowDescription'");

		configured = true;
	}

	public void testConfigured() {
		super.testConfigured();
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": The component is not configured.");
		}
	}

	public ExcelAdapter getExcelAdapter() {
		return excelAdapter;
	}

	public void setExcelAdapter(ExcelAdapter excelAdapter) {
		this.excelAdapter = excelAdapter;
	}

	public Integer getRowOrder() {
		return rowOrder;
	}

	public void setRowOrder(Integer rowOrder) {
		this.rowOrder = rowOrder;
	}

	public Integer getRowName() {
		return rowName;
	}

	public void setRowName(Integer rowName) {
		this.rowName = rowName;
	}

	public Integer getRowDescription() {
		return rowDescription;
	}

	public void setRowDescription(Integer rowDescription) {
		this.rowDescription = rowDescription;
	}
}
