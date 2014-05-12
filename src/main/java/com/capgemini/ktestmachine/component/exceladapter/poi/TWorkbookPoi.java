package com.capgemini.ktestmachine.component.exceladapter.poi;

import java.io.File;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;

import com.capgemini.ktestmachine.component.exceladapter.TWorkbook;

public class TWorkbookPoi implements TWorkbook {

	private File file;
	private Workbook workbook;
	private InputStream inputStream;
	private FormulaEvaluator evaluator;
	private short dateFormat;
	private short timeFormat;

	public FormulaEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(FormulaEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public short getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(short dateFormat) {
		this.dateFormat = dateFormat;
	}

	public short getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(short timeFormat) {
		this.timeFormat = timeFormat;
	}
	
	public String toString() {
		return file.getAbsolutePath();
	}
}
