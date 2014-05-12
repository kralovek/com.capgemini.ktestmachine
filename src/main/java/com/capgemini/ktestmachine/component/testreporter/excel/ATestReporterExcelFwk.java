package com.capgemini.ktestmachine.component.testreporter.excel;

import java.io.File;

import com.capgemini.ktestmachine.component.exceladapter.ExcelAdapter;
import com.capgemini.ktestmachine.component.exceladapter.ExcelStylesFactory;
import com.capgemini.ktestmachine.component.testloader.excel.ATestExcelFwk;
import com.capgemini.ktestmachine.component.testloader.excel.StructureSheetGeneric;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.excel.ExcelIdCell;
import com.capgemini.ktestmachine.utils.excel.ExcelUtils;

public abstract class ATestReporterExcelFwk extends ATestExcelFwk {
	private boolean configured;

	protected ExcelAdapter excelAdapter;
	protected File dir;
	protected StructureSheetGeneric structureSheet;

	protected ExcelStylesFactory excelStylesFactory;
	
	private Integer rowStatus;
	protected Integer idRowStatus;
	private Integer rowColour;
	protected Integer idRowColour;
	private String cellStatusSheet;
	protected ExcelIdCell idCellStatusSheet;
	private String cellColourSheet;
	protected ExcelIdCell idCellColourSheet;

	protected Boolean review;
	protected String reviewSheet;
	private Integer reviewRowFirst;
	protected Integer idReviewRowFirst;
	private String reviewColumnSheetName;
	protected Integer idReviewColumnSheetName;
	private String reviewColumnStatus;
	protected Integer idReviewColumnStatus;
	private String reviewColumnColour;
	protected Integer idReviewColumnColour;

	public void config() throws ConfigurationException {
		configured = false;
		super.config();
		if (excelAdapter == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'excelAdapter' is not configured");
		}

		if (excelStylesFactory == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'excelStylesFactory' is not configured");
		}

		structureSheet = new StructureSheetGeneric(excelAdapter);

		idRowStatus = ExcelUtils.getInstance().adaptAndCheckRowId(rowStatus,
				"Parameter 'rowStatus'");
		idRowColour = ExcelUtils.getInstance().adaptAndCheckRowId(rowColour,
				"Parameter 'rowColour'");

		idCellColourSheet = ExcelUtils.getInstance().adaptAndCheckCellId(
				cellColourSheet, "Parameter 'cellColourSheet'");
		idCellStatusSheet = ExcelUtils.getInstance().adaptAndCheckCellId(
				cellStatusSheet, "Parameter 'cellStatusSheet'");

		if (review == null) {
			review = false;
		}
		if (review) {
			if (isEmpty(reviewSheet)) {
				throw new ConfigurationException(
						getClass().getSimpleName()
								+ ": Review is adtivated and the parameter 'reviewSheet' is not configured");
			}
			if (reviewRowFirst == null) {
				throw new ConfigurationException(
						getClass().getSimpleName()
								+ ": Review is adtivated and the parameter 'reviewRowFirst' is not configured");
			}
			if (reviewColumnSheetName == null) {
				throw new ConfigurationException(
						getClass().getSimpleName()
								+ ": Review is adtivated and the parameter 'reviewColumnSheetName' is not configured");
			}
			if (reviewColumnStatus != null) {
				idReviewColumnStatus = ExcelUtils.getInstance()
						.adaptAndCheckColumnId(reviewColumnStatus,
								"Parameter idReviewColumnStatus");
			}
			if (reviewColumnColour != null) {
				idReviewColumnColour = ExcelUtils.getInstance()
						.adaptAndCheckColumnId(reviewColumnColour,
								"Parameter idReviewColumnColour");
			}

			idReviewRowFirst = ExcelUtils.getInstance().adaptAndCheckRowId(
					reviewRowFirst, "Parameter idReviewRowFirst");
			idReviewColumnSheetName = ExcelUtils.getInstance()
					.adaptAndCheckColumnId(reviewColumnSheetName,
							"Parameter idReviewColumnSheet");
		}

		configured = true;
	}

	public void testConfigured() {
		super.testConfigured();
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": The componet is not configured.");
		}
	}

	public Integer getRowStatus() {
		return rowStatus;
	}

	public void setRowStatus(Integer rowStatus) {
		this.rowStatus = rowStatus;
	}

	public Integer getRowColour() {
		return rowColour;
	}

	public void setRowColour(Integer rowColour) {
		this.rowColour = rowColour;
	}

	public String getCellStatusSheet() {
		return cellStatusSheet;
	}

	public void setCellStatusSheet(String cellStatusSheet) {
		this.cellStatusSheet = cellStatusSheet;
	}

	public String getCellColourSheet() {
		return cellColourSheet;
	}

	public void setCellColourSheet(String cellColourSheet) {
		this.cellColourSheet = cellColourSheet;
	}

	public Boolean getReview() {
		return review;
	}

	public void setReview(Boolean review) {
		this.review = review;
	}

	public String getReviewSheet() {
		return reviewSheet;
	}

	public void setReviewSheet(String reviewSheet) {
		this.reviewSheet = reviewSheet;
	}

	public Integer getReviewRowFirst() {
		return reviewRowFirst;
	}

	public void setReviewRowFirst(Integer reviewRowFirst) {
		this.reviewRowFirst = reviewRowFirst;
	}

	public String getReviewColumnSheetName() {
		return reviewColumnSheetName;
	}

	public void setReviewColumnSheetName(String reviewColumnSheetName) {
		this.reviewColumnSheetName = reviewColumnSheetName;
	}

	public String getReviewColumnStatus() {
		return reviewColumnStatus;
	}

	public void setReviewColumnStatus(String reviewColumnStatus) {
		this.reviewColumnStatus = reviewColumnStatus;
	}

	public String getReviewColumnColour() {
		return reviewColumnColour;
	}

	public void setReviewColumnColour(String reviewColumnColour) {
		this.reviewColumnColour = reviewColumnColour;
	}

	public ExcelAdapter getExcelAdapter() {
		return excelAdapter;
	}

	public void setExcelAdapter(ExcelAdapter excelAdapter) {
		this.excelAdapter = excelAdapter;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	protected boolean isEmpty(final String pValue) {
		return pValue == null || pValue.isEmpty();
	}

	public ExcelStylesFactory getExcelStylesFactory() {
		return excelStylesFactory;
	}

	public void setExcelStylesFactory(ExcelStylesFactory excelStylesFactory) {
		this.excelStylesFactory = excelStylesFactory;
	}
}
