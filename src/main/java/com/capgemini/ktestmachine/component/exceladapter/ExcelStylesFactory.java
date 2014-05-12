package com.capgemini.ktestmachine.component.exceladapter;

import com.capgemini.ktestmachine.exception.ABaseException;

public interface ExcelStylesFactory {

	ExcelStyles getStyles(TWorkbook workbook) throws ABaseException; 
}
