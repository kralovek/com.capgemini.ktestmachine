package com.capgemini.ktestmachine.component.exceladapter.poi;

import com.capgemini.ktestmachine.component.exceladapter.TColor;

public class TColorPoi implements TColor {

	private short color;
	
	public TColorPoi(short color) {
		this.color = color;
	}
	
	public short getColor() {
		return color;
	}
}
