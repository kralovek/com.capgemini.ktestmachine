package com.capgemini.ktestmachine.component.exceladapter.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.capgemini.ktestmachine.component.exceladapter.TCell;

public class TCellPoi implements TCell {

	private int row;
	private int column;

	private TSheetPoi poiSheet;

	private Cell cell;
	private FormulaEvaluator evaluator;

	public TCellPoi(TSheetPoi poiSheet) {
		this.poiSheet = poiSheet;
	}

	public FormulaEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(FormulaEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public TSheetPoi getPoiSheet() {
		return poiSheet;
	}

	public void setPoiSheet(TSheetPoi poiSheet) {
		this.poiSheet = poiSheet;
	}

	public String toString() {
		return "[" + row + ":" + column + "]";
	}
}
