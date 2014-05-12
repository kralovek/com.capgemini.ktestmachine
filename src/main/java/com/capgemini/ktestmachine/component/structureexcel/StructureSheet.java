package com.capgemini.ktestmachine.component.structureexcel;

import java.util.Map;
import java.util.TreeMap;

public class StructureSheet {
	
	protected Map<Integer, String> input = new TreeMap<Integer, String>();

	protected Map<Integer, String> expect = new TreeMap<Integer, String>();

	protected Map<Integer, String> output = new TreeMap<Integer, String>();

	protected Map<Integer, String> testIds = new TreeMap<Integer, String>();

	protected Map<Integer, Integer> testOrders = new TreeMap<Integer, Integer>();

	public Map<Integer, String> getInput() {
		return this.input;
	}

	public Map<Integer, String> getExpect() {
		return this.expect;
	}

	public Map<Integer, String> getOutput() {
		return this.output;
	}

	public Map<Integer, String> getTestIds() {
		return this.testIds;
	}

	public Map<Integer, Integer> getTestOrders() {
		return this.testOrders;
	}
}
