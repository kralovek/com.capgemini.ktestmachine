package com.capgemini.ktestmachine.component.exceladapter.poi;

import com.capgemini.ktestmachine.component.exceladapter.ExcelStyles;
import com.capgemini.ktestmachine.component.exceladapter.TStyle;

public class ExcelStylesPoi implements ExcelStyles {

	private TStylePoi styleTitleOK;
	private TStylePoi styleTitleKO;
	private TStylePoi styleStatOK;
	private TStylePoi styleStatKO;
	private TStylePoi styleFieldExpectOK;
	private TStylePoi styleFieldExpectKO;
	private TStylePoi styleFieldOutputOK;
	private TStylePoi styleFieldOutputKO;

	public TStylePoi getStyleStatKO() {
		return styleStatKO;
	}

	public TStylePoi getStyleStatOK() {
		return styleStatOK;
	}

	public TStylePoi getStyleTestKO() {
		return styleTitleKO;
	}

	public TStylePoi getStyleTestOK() {
		return styleTitleOK;
	}

	public TStylePoi getStyleFieldExpectKO() {
		return styleFieldExpectKO;
	}

	public TStylePoi getStyleFieldExpectOK() {
		return styleFieldExpectOK;
	}

	public TStylePoi getStyleTitleOK() {
		return styleTitleOK;
	}

	public void setStyleTitleOK(TStylePoi styleTitleOK) {
		this.styleTitleOK = styleTitleOK;
	}

	public TStylePoi getStyleTitleKO() {
		return styleTitleKO;
	}

	public void setStyleTitleKO(TStylePoi styleTitleKO) {
		this.styleTitleKO = styleTitleKO;
	}

	public TStylePoi getStyleFieledExpectOK() {
		return styleFieldExpectOK;
	}

	public void setStyleFieldExpectOK(TStylePoi styleFieldExpectOK) {
		this.styleFieldExpectOK = styleFieldExpectOK;
	}

	public TStylePoi getStyleFieledExpectKO() {
		return styleFieldExpectKO;
	}
	
	public void setStyleFieldExpectKO(TStylePoi styleFieldExpectKO) {
		this.styleFieldExpectKO = styleFieldExpectKO;
	}

	public TStylePoi getStyleFieldOutputOK() {
		return styleFieldOutputOK;
	}

	public void setStyleFieldOutputOK(TStylePoi styleFieledOutputOK) {
		this.styleFieldOutputOK = styleFieledOutputOK;
	}

	public void setStyleFieldOutputKO(TStylePoi styleFieldOutputKO) {
		this.styleFieldOutputKO = styleFieldOutputKO;
	}

	public void setStyleStatOK(TStylePoi styleStatOK) {
		this.styleStatOK = styleStatOK;
	}

	public void setStyleStatKO(TStylePoi styleStatKO) {
		this.styleStatKO = styleStatKO;
	}

	public TStylePoi getStyleFieldOutputKO() {
		return styleFieldOutputKO;
	}
}
