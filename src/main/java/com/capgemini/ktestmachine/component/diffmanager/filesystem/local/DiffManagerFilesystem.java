package com.capgemini.ktestmachine.component.diffmanager.filesystem.local;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager;
import com.capgemini.ktestmachine.component.diffmanager.filesystem.DirInfo;
import com.capgemini.ktestmachine.exception.ABaseException;


public class DiffManagerFilesystem extends ADiffManagerFilesystemFwk implements DiffManager {
	private static final Logger LOGGER = Logger
			.getLogger(DiffManagerFilesystem.class);

	private static final String UNIX_PATH_SEPARATOR = "/";

	private static Comparator<Item> comparatorItem = new Comparator<Item>() {
		public int compare(Item item1, Item item2) {
			return item1.getName().compareTo(item2.getName());
		}
	};

	private static class ItemImpl implements Item {
		private String name;
		private long index;
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

		public long getIndex() {
			return index;
		}

		public Status getStatus() {
			return status;
		}

		public void setIndex(long index) {
			this.index = index;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public Map<String, String> getParameters() {
			return parameters;
		}
	}

	private static class GroupImpl implements Group, Comparable<Group> {
		private String name;
		private long lastIndex;
		private List<Item> items = new ArrayList<Item>();

		public GroupImpl(String name) {
			this.name = name;
		}

		public List<Item> getItems() {
			return items;
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

		public int compareTo(Group group) {
			return name.compareTo(group.getName());
		}
	}

	public List<Group> loadDiffs(List<Group> groupStates) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			List<Group> groups = new ArrayList<Group>();

			for (DirInfo dirInfo : dirInfos) {
				Group groupState = findGroup(groupStates, dirInfo.getName());
				Group group = loadDiff(dirInfo, groupState);
				groups.add(group);
			}

			LOGGER.trace("OK");
			return groups;
		} finally {
			LOGGER.trace("END");
		}
	}

	private Group loadDiff(DirInfo dirInfo, Group groupState)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			File dir = new File(dirInfo.getPath());
			long date = groupState != null ? groupState.getLastIndex()
					: 0;

			List<Item> items = null;

			if (dirInfo.isContent() && groupState != null
					&& groupState.getItems() != null) {
				List<ItemImpl> existingItems = getItems(dir,
						dir, 0, dirInfo.getPattern());

				items = new ArrayList<Item>();

				for (Item itemState : groupState.getItems()) {
					ItemImpl existingItem = findItem(existingItems,
							itemState.getName());
					if (existingItem != null) {
						if (existingItem.getIndex() <= itemState.getIndex()) {
							existingItems.remove(existingItem);
						} else {
							existingItem.setStatus(Status.UPD);
						}
					} else {
						ItemImpl newItem = new ItemImpl(itemState);
						newItem.setStatus(Status.DEL);
						items.add(newItem);
					}
				}

				items.addAll(existingItems);
				Collections.sort(items, comparatorItem);
			} else {
				List<ItemImpl> existingItems = getItems(dir,
						dir, date, dirInfo.getPattern());
				items = new ArrayList<Item>();
				items.addAll(existingItems);
			}

			long lastModified = getLastModified(items);
			GroupImpl group = new GroupImpl(dirInfo.getName());
			group.setLastIndex(lastModified);
			group.getItems().addAll(items);
			LOGGER.trace("OK");
			return group;
		} finally {
			LOGGER.trace("END");
		}
	}

	private static ItemImpl findItem(List<ItemImpl> items, String name) {
		for (ItemImpl item : items) {
			if (item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

	private static Group findGroup(List<Group> groups, String name) {
		if (groups != null) {
			for (Group group : groups) {
				if (name.equals(group.getName())) {
					return group;
				}
			}
		}
		return null;
	}

	public List<Group> loadCurrents() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();

			List<Group> groups = new ArrayList<Group>();

			for (DirInfo dirInfo : dirInfos) {
				if (dirInfo.isContent()) {
					Group group = loadDiff(dirInfo, null);
					groups.add(group);
				} else {
					Group group = loadCurrent(dirInfo);
					groups.add(group);
				}
			}

			LOGGER.trace("OK");
			return groups;
		} finally {
			LOGGER.trace("END");
		}
	}

	public Group loadCurrent(DirInfo dirInfo) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			File dir = new File(dirInfo.getPath());
			GroupImpl group = new GroupImpl(dirInfo.getName());
			long lastModified = getLastModifiedDirectory(dir,
					dir, 0, dirInfo.getPattern());
			group.setLastIndex(lastModified);
			LOGGER.trace("OK");
			return group;
		} finally {
			LOGGER.trace("END");
		}
	}

	private static List<ItemImpl> getItems(File dirRoot, File dir, long index,
			Pattern pattern) throws ABaseException {
		List<ItemImpl> items = new ArrayList<ItemImpl>();
		File[] files = dir.listFiles();

		if (files == null) {
			return items;
		}

		for (File file : files) {
			if (file.isFile()) {
				if (file.lastModified() <= index) {
					// Ignor old files
					continue;
				}
				String path = adaptPath(dirRoot, file);

				if (pattern != null && !pattern.matcher(path).matches()) {
					LOGGER.debug("Ignored file: " + file);
					continue;
				}

				ItemImpl item = new ItemImpl(path);
				item.setIndex(file.lastModified());
				item.setStatus(Status.NEW);
				items.add(item);
			} else if (file.isDirectory()) {
				List<ItemImpl> itemsLocal = getItems(dirRoot, file, index,
						pattern);
				items.addAll(itemsLocal);
			}
		}

		Collections.sort(items, comparatorItem);

		return items;
	}

	private static String adaptPath(File dir, File file) {
		String relativePath = file.getAbsolutePath().substring(
				dir.getAbsolutePath().length());
		if (relativePath.startsWith(File.separator)) {
			relativePath = relativePath.substring(File.separator.length());
		}
		relativePath = relativePath
				.replace(File.separator, UNIX_PATH_SEPARATOR);
		return relativePath;
	}

	private static long getLastModifiedDirectory(File dirRoot, File dir,
			long lastModified, Pattern pattern) throws ABaseException {
		File[] files = dir.listFiles();

		if (files == null) {
			return 0;
		}

		for (File file : files) {
			if (file.isFile()) {
				String path = adaptPath(dirRoot, file);

				if (pattern != null && !pattern.matcher(path).matches()) {
					LOGGER.debug("Ignored file: " + file);
					continue;
				}

				lastModified = lastModified >= file.lastModified() ? lastModified
						: file.lastModified();
			} else if (file.isDirectory()) {
				long lastModifiedLocal = getLastModifiedDirectory(dirRoot,
						file, lastModified, pattern);
				lastModified = lastModified >= lastModifiedLocal ? lastModified
						: lastModifiedLocal;
			}
		}

		return lastModified;
	}

	private static long getLastModified(List<Item> items) {
		long lastModified = 0;
		for (Item item : items) {
			// To je divny ... ta podminka ma byt naopak !!!
			if (lastModified > item.getIndex()) {
				lastModified = item.getIndex();
			}
		}
		return lastModified;
	}
}
