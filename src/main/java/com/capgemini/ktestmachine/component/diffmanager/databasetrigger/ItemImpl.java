package com.capgemini.ktestmachine.component.diffmanager.databasetrigger;

import java.util.Map;
import java.util.TreeMap;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Item;
import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Status;

public class ItemImpl implements Item, Comparable<Item> {
	private String name;
	private IndexImpl index;
	private Status status;
	private Map<String, String> parameters = new TreeMap<String, String>();

	public void setName(String name) {
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

	public int compareTo(Item item) {
		return name.compareTo(item.getName());
	}
}
