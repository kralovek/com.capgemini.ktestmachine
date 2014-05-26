package com.capgemini.ktestmachine.component.diffmanager.database;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.capgemini.ktestmachine.component.diffmanager.database.checkerstatus.CheckerStatus;
import com.capgemini.ktestmachine.exception.ConfigurationException;

public abstract class ATableInfoFwk {

	private boolean configured;

	protected String name;
	private String columnName;
	private String columnSort;
	private List<String> columnsSortMore;
	private List<String> columns;
	protected Comparator<ItemCruid> comparator;
	protected CheckerStatus checkerStatus;

	public void config() throws ConfigurationException {
		configured = false;
		if (columnName == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter columnName is not configured");
		}
		if (columnsSortMore == null) {
			columnsSortMore = new ArrayList<String>();
		}
		if (columnSort == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter TableInfo.columnSort is not configured");
		}
		if (comparator == null) {
			// OK
		}
		if (checkerStatus == null) {
			// OK
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configuré.");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnSort() {
		return columnSort;
	}

	public void setColumnSort(String columnSort) {
		this.columnSort = columnSort;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<String> getColumnsSortMore() {
		return columnsSortMore;
	}

	public void setColumnsSortMore(List<String> columnsSortMore) {
		this.columnsSortMore = columnsSortMore;
	}

	public Comparator<ItemCruid> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<ItemCruid> comparator) {
		this.comparator = comparator;
	}

	public CheckerStatus getCheckerStatus() {
		return checkerStatus;
	}

	public void setCheckerStatus(CheckerStatus checkerStatus) {
		this.checkerStatus = checkerStatus;
	}
}
