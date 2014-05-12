package com.capgemini.ktestmachine.component.exceladapter;

import java.io.File;

import com.capgemini.ktestmachine.exception.ABaseException;

public interface ExcelAdapter {
 
	TWorkbook cloneWorkbook(TWorkbook genWorkbookSource, File file) throws ABaseException;

	TWorkbook createWorkbook(File file) throws ABaseException;

	void closeWorkbook(TWorkbook genWorkbook) throws ABaseException;

	void saveWorkbook(TWorkbook genWorkbook) throws ABaseException;

	TWorkbook readWorkbook(File file) throws ABaseException;

	TSheet getSheet(TWorkbook workbook, String name);

	TSheet createSheet(TWorkbook workbook, String name);
	
	TSheet getSheet(TWorkbook workbook, int index);

	int getSheetCount(TWorkbook workbook);
	
	TCell getCell(TSheet sheet, int row, int column);
	
	Object getValue(TCell cell);

	String getStringValue(TCell cell);

	void setValue(TSheet genSheet, TCell genCell, TCell genCellTemplate, Object value);

	void setValue(TSheet sheet, TCell cell, Object value);

	void setCellStyle(TCell cell, TStyle genStyle);

	void copyCellStyle(TCell cellSource, TCell celltarget);
}
