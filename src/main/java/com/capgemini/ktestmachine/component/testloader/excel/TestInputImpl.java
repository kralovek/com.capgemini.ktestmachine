package com.capgemini.ktestmachine.component.testloader.excel;

import java.util.HashMap;
import java.util.Map;

import com.capgemini.ktestmachine.data.TestInput;

public class TestInputImpl implements TestInput, Comparable<TestInputImpl> {

	private String source;
	private String type;
	private String id;
	private Integer order;
	private int column;
	private int sheetOrder;
	private Map<String, Object> dataInput = new HashMap<String, Object>();

	private String name;
	private String description;

	public int getSheetOrder() {
		return sheetOrder;
	}

	public void setSheetOrder(int sheetOrder) {
		this.sheetOrder = sheetOrder;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getDataInput() {
		return dataInput;
	}

	public int compareTo(TestInputImpl test) {
		int result = 0;
		//
		// NULL
		//
		if (test == null) {
			return -1;
		}

		//
		// SOURCE
		//
		result = source.compareTo(test.source);
		if (result != 0) {
			return result;
		}

		//
		// ORDER
		//
		// Priorities:
		// 1. Order positif
		// 2. No order
		// 3. Ordr negatif
		//
		Integer orderType1 = //
		order == null ? 2 : //
				order >= 0 ? 1 : 3;
		Integer orderType2 = //
		test.order == null ? 2 : //
				test.order >= 0 ? 1 : 3;

		if (orderType1 != orderType2) {
			return orderType1.compareTo(orderType2);
		}

		if (order == null) {

		} else if (order >= 0) {
			result = order.compareTo(test.order);
			if (result != 0) {
				return result;
			}
		} else if (order < 0) {
			result = -(order.compareTo(test.order));
			if (result != 0) {
				return result;
			}
		}

		//
		// TYPE
		//

		result = new Integer(sheetOrder).compareTo(test.sheetOrder);
		if (result != 0) {
			return result;
		}

		//
		// COLUMN
		//
		return Integer.valueOf(column).compareTo(test.column);
	}

	public String toString() {
		return "{" + source + "} [" + type + "] " + id;
	}
}
