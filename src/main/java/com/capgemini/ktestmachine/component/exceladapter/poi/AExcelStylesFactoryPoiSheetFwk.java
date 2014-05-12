package com.capgemini.ktestmachine.component.exceladapter.poi;

import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelIdCell;
import com.capgemini.ktestmachine.utils.excel.ExcelUtils;

public abstract class AExcelStylesFactoryPoiSheetFwk {
	private boolean configured;

	protected String sheet;
	private String cellTitleOK;
	protected ExcelIdCell idCellTitleOK;
	private String cellTitleKO;
	protected ExcelIdCell idCellTitleKO;
	private String cellStatOK;
	protected ExcelIdCell idCellStatOK;
	private String cellStatKO;
	protected ExcelIdCell idCellStatKO;
	private String cellFieldExpectOK;
	protected ExcelIdCell idCellFieldExpectOK;
	private String cellFieldExpectKO;
	protected ExcelIdCell idCellFieldExpectKO;
	private String cellFieldOutputOK;
	protected ExcelIdCell idCellFieldOutputOK;
	private String cellFieldOutputKO;
	protected ExcelIdCell idCellFieldOutputKO;

	public void config() throws ConfigurationException {
		configured = false;
		if (cellTitleOK == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'cellTitleOK' is not configured");
		} else {
			idCellTitleOK = ExcelUtils.getInstance().adaptAndCheckCellId(
					cellTitleOK, "Parameter 'cellTitleOK'");
		}
		if (cellTitleKO == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'cellTitleKO' is not configured");
		} else {
			idCellTitleKO = ExcelUtils.getInstance().adaptAndCheckCellId(
					cellTitleKO, "Parameter 'cellTitleKO'");
		}

		if (cellStatOK == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'cellStatOK' is not configured");
		} else {
			idCellStatOK = ExcelUtils.getInstance().adaptAndCheckCellId(
					cellStatOK, "Parameter 'cellStatOK'");
		}
		if (cellStatKO == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'cellStatKO' is not configured");
		} else {
			idCellStatKO = ExcelUtils.getInstance().adaptAndCheckCellId(
					cellStatKO, "Parameter 'cellStatKO'");
		}

		if (cellFieldExpectOK == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'cellFieldExpectOK' is not configured");
		} else {
			idCellFieldExpectOK = ExcelUtils.getInstance().adaptAndCheckCellId(
					cellFieldExpectOK, "Parameter 'cellFieldExpectOK'");
		}
		if (cellFieldExpectKO == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'cellFieldExpectKO' is not configured");
		} else {
			idCellFieldExpectKO = ExcelUtils.getInstance().adaptAndCheckCellId(
					cellFieldExpectKO, "Parameter 'cellFieldExpectKO'");
		}

		if (cellFieldOutputOK == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'cellFieldOutputOK' is not configured");
		} else {
			idCellFieldOutputOK = ExcelUtils.getInstance().adaptAndCheckCellId(
					cellFieldOutputOK, "Parameter 'cellFieldOutputOK'");
		}
		if (cellFieldOutputKO == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'cellFieldOutputKO' is not configured");
		} else {
			idCellFieldOutputKO = ExcelUtils.getInstance().adaptAndCheckCellId(
					cellFieldOutputKO, "Parameter 'cellFieldOutputKO'");
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": the component must be configured.");
		}
	}

	public String getSheet() {
		return sheet;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	public String getCellTitleOK() {
		return cellTitleOK;
	}

	public void setCellTitleOK(String cellTitleOK) {
		this.cellTitleOK = cellTitleOK;
	}

	public String getCellTitleKO() {
		return cellTitleKO;
	}

	public void setCellTitleKO(String cellTitleKO) {
		this.cellTitleKO = cellTitleKO;
	}

	public String getCellStatOK() {
		return cellStatOK;
	}

	public void setCellStatOK(String cellStatOK) {
		this.cellStatOK = cellStatOK;
	}

	public String getCellStatKO() {
		return cellStatKO;
	}

	public void setCellStatKO(String cellStatKO) {
		this.cellStatKO = cellStatKO;
	}

	public String getCellFieldExpectOK() {
		return cellFieldExpectOK;
	}

	public void setCellFieldExpectOK(String cellFieldExpectOK) {
		this.cellFieldExpectOK = cellFieldExpectOK;
	}

	public String getCellFieldExpectKO() {
		return cellFieldExpectKO;
	}

	public void setCellFieldExpectKO(String cellFieldExpectKO) {
		this.cellFieldExpectKO = cellFieldExpectKO;
	}

	public String getCellFieldOutputOK() {
		return cellFieldOutputOK;
	}

	public void setCellFieldOutputOK(String cellFieldOutputOK) {
		this.cellFieldOutputOK = cellFieldOutputOK;
	}

	public String getCellFieldOutputKO() {
		return cellFieldOutputKO;
	}

	public void setCellFieldOutputKO(String cellFieldOutputKO) {
		this.cellFieldOutputKO = cellFieldOutputKO;
	}

}
