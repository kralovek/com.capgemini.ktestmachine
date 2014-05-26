package com.capgemini.ktestmachine.component.diffmanager.filesystem.ftp;

import java.util.Map;
import java.util.TreeMap;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Item;
import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Status;

public class ItemImpl implements Item, Cloneable {
	private String name;
	private IndexImpl index;
	private Status status;
	private Map<String, String> parameters = new TreeMap<String, String>();

	public ItemImpl(Item item) {
		name = item.getName();
		index = (IndexImpl) item.getIndex();
		status = item.getStatus();
		parameters.putAll(item.getParameters());
	}

	public ItemImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public IndexImpl getIndex() {
		return index;
	}

	public Status getStatus() {
		return status;
	}

	public void setIndex(IndexImpl index) {
		this.index = index;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public ItemImpl clone() {
		ItemImpl item = new ItemImpl(name);
		item.index = index;
		item.status = status;
		item.getParameters().putAll(parameters);
		return item;
	}
}
