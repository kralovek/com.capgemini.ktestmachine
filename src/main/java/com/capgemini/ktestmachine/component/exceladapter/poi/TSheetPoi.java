package com.capgemini.ktestmachine.component.exceladapter.poi;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;

import com.capgemini.ktestmachine.component.exceladapter.TSheet;

public class TSheetPoi implements TSheet {
	private String name;

	private TWorkbookPoi poiWorkbook;
	private Sheet sheet;
	private FormulaEvaluator evaluator;

	private int rowMin;
	private int rowMax;
	private int columnMin;
	private int columnMax;

	public TSheetPoi(TWorkbookPoi poiWorkbook) {
		this.poiWorkbook = poiWorkbook;
	}

	public FormulaEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(FormulaEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public int getRowMin() {
		return rowMin;
	}

	public void setRowMin(int rowMin) {
		this.rowMin = rowMin;
	}

	public int getRowMax() {
		return rowMax;
	}

	public void setRowMax(int rowMax) {
		this.rowMax = rowMax;
	}

	public int getColumnMin() {
		return columnMin;
	}

	public void setColumnMin(int columnMin) {
		this.columnMin = columnMin;
	}

	public int getColumnMax() {
		return columnMax;
	}

	public void setColumnMax(int columnMax) {
		this.columnMax = columnMax;
	}

	public TWorkbookPoi getPoiWorkbook() {
		return poiWorkbook;
	}

	public void setPoiWorkbook(TWorkbookPoi poiWorkbook) {
		this.poiWorkbook = poiWorkbook;
	}
	
	public String toString() {
		return name;
	}
}
