package com.capgemini.ktestmachine.component.diffmanager.databasetrigger;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Group;
import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Item;

public class GroupImpl implements Group, Comparable<Group> {
	private String name;
	private IndexImpl lastIndex;
	private List<Item> items = new ArrayList<Item>();

	public GroupImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public IndexImpl getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(IndexImpl lastIndex) {
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
