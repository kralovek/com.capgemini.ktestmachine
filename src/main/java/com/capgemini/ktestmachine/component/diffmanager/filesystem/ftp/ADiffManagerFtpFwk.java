package com.capgemini.ktestmachine.component.diffmanager.filesystem.ftp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager;
import com.capgemini.ktestmachine.component.diffmanager.filesystem.DirInfo;
import com.capgemini.ktestmachine.exception.ConfigurationException;


public abstract class ADiffManagerFtpFwk implements DiffManager {
	protected static final String PATH_SEPARATOR = "/";
	protected static final String UNIX_PATH_SEPARATOR = "/";

	private boolean configured;

	protected String name;
	protected List<DirInfo> dirInfos;

	protected String ftpHost;
	protected Integer ftpPort;
	protected String ftpLogin;
	protected String ftpPassword;

	public void config() throws ConfigurationException {
		configured = false;
		if (dirInfos == null) {
			dirInfos = new ArrayList<DirInfo>();
		} else {
			for (DirInfo dirInfo : dirInfos) {
				if (dirInfo.getPath() == null) {
					throw new ConfigurationException(getClass().getSimpleName()
							+ "." + dirInfo.getClass().getSimpleName()
							+ ": Parameter dirPath is not configured");
				}
				if (!dirInfo.getPath().endsWith(PATH_SEPARATOR)) {
					dirInfo.setPath(dirInfo.getPath() + PATH_SEPARATOR);
				}
				if (dirInfo.getName() == null) {
					dirInfo.setName(dirInfo.getPath());
				}
			}
		}
		if (name == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter name is not configured");
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	public List<DirInfo> getDirInfos() {
		return dirInfos;
	}

	public void setDirInfos(List<DirInfo> dirInfos) {
		this.dirInfos = dirInfos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpLogin() {
		return ftpLogin;
	}

	public void setFtpLogin(String ftpLogin) {
		this.ftpLogin = ftpLogin;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	protected static class ItemImpl implements Item, Cloneable {
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

		public ItemImpl clone() {
			ItemImpl item = new ItemImpl(name);
			item.index = index;
			item.status = status;
			item.getParameters().putAll(parameters);
			return item;
		}
	}

	protected static class GroupImpl implements Group, Comparable<Group> {
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
}
