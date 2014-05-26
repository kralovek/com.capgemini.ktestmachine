package com.capgemini.ktestmachine.component.testreporter.excel;

import com.capgemini.ktestmachine.data.TestResult;

public class TestResultImpl implements TestResult {

	private String source;

	private String type;

	private String id;

	private Boolean result;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
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
}
