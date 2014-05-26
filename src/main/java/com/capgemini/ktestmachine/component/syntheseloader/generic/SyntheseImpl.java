package com.capgemini.ktestmachine.component.syntheseloader.generic;

import java.util.Map;

import com.capgemini.ktestmachine.data.Synthese;
import com.capgemini.ktestmachine.data.TestInput;

public class SyntheseImpl implements Synthese {

	private TestInput testInput;

	private Boolean result;

	public SyntheseImpl(TestInput testInput) {
		this.testInput = testInput;
	}

	public String getSource() {
		return testInput != null ? testInput.getSource() : null;
	}

	public String getType() {
		return testInput != null ? testInput.getType() : null;
	}

	public String getId() {
		return testInput != null ? testInput.getId() : null;
	}

	public String getName() {
		return testInput != null ? testInput.getName() : null;
	}

	public String getDescription() {
		return testInput != null ? testInput.getDescription() : null;
	}

	public Map<String, Object> getDataInput() {
		return testInput != null ? testInput.getDataInput() : null;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}
}
