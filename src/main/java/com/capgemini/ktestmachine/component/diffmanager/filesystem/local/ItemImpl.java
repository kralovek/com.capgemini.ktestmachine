package com.capgemini.ktestmachine.component.diffmanager.filesystem.local;

import java.util.Map;
import java.util.TreeMap;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Index;
import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Item;
import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Status;

public class ItemImpl implements Item {
	private String name;
	private Index index;
	private Status status;
	private Map<String, String> parameters = new TreeMap<String, String>();

	public ItemImpl(Item item) {
		name = item.getName();
		index = item.getIndex();
		status = item.getStatus();
		parameters.putAll(item.getParameters());
	}

	public ItemImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Index getIndex() {
		return index;
	}

	public Status getStatus() {
		return status;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
}
