package com.capgemini.ktestmachine.component.diffmanager.database;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Group;
import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Item;

public class GroupImpl implements Group, Comparable<Group> {
	private String name;
	private long lastIndex;
	private List<Item> items = new ArrayList<Item>();

	public GroupImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(long lastIndex) {
		this.lastIndex = lastIndex;
	}

	public List<Item> getItems() {
		return items;
	}

	public int compareTo(Group group) {
		return name.compareTo(group.getName());
	}
	
	public String toString() {
		return name;
	}
}
