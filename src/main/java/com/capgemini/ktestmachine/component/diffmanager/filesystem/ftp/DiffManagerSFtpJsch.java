package com.capgemini.ktestmachine.component.diffmanager.filesystem.ftp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.diffmanager.filesystem.DirInfo;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class DiffManagerSFtpJsch extends ADiffManagerFtpFwk {
	private static final String UNIX_PATH_SEPARATOR = "/";

	private static final Logger LOGGER = Logger
			.getLogger(DiffManagerSFtpJsch.class);

	private static Comparator<Item> comparatorItem = new Comparator<Item>() {
		public int compare(Item item1, Item item2) {
			return item1.getName().compareTo(item2.getName());
		}
	};

	private static class Client {
		private Session session;
		private ChannelSftp channel;

		public Session getSession() {
			return session;
		}

		public void setSession(Session session) {
			this.session = session;
		}

		public ChannelSftp getChannel() {
			return channel;
		}

		public void setChannel(ChannelSftp channel) {
			this.channel = channel;
		}
	}

	public List<Group> loadDiffs(List<Group> groupStates) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			List<Group> groups = new ArrayList<Group>();

			if (dirInfos.isEmpty()) {
				LOGGER.trace("OK");
				return groups;
			}
			Client client = connect();

			try {
				for (DirInfo dirInfo : dirInfos) {
					Group groupState = findGroup(groupStates, dirInfo.getName());
					Group group = loadDiff(client, dirInfo, groupState);
					groups.add(group);
				}
			} finally {
				disconnect(client);
			}
			LOGGER.trace("OK");
			return groups;
		} finally {
			LOGGER.trace("END");
		}
	}

	private Group loadDiff(Client client, DirInfo dirInfo, Group groupState)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			LOGGER.debug("dir: " + dirInfo.getPath());
			long date = groupState != null ? ((IndexImpl) groupState
					.getLastIndex()).getMs() : 0;
			List<Item> items = null;
			if (dirInfo.isContent() && groupState != null
					&& groupState.getItems() != null) {
				List<ItemImpl> existingItems = getItems(client,
						dirInfo.getPath(), dirInfo.getPath(), 0,
						dirInfo.getPattern());

				items = new ArrayList<Item>();

				for (Item itemState : groupState.getItems()) {
					ItemImpl existingItem = findItem(existingItems,
							itemState.getName());
					if (existingItem != null) {
						if (((IndexImpl) existingItem.getIndex()).getMs() <= ((IndexImpl) itemState
								.getIndex()).getMs()) {
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
				List<ItemImpl> existingItems = getItems(client,
						dirInfo.getPath(), dirInfo.getPath(), date,
						dirInfo.getPattern());
				items = new ArrayList<Item>();
				items.addAll(existingItems);
			}

			long lastModified = getLastModified(items);
			GroupImpl group = new GroupImpl(dirInfo.getName());
			IndexImpl indexImpl = new IndexImpl();
			indexImpl.setMs(lastModified);
			group.setLastIndex(indexImpl);
			group.getItems().addAll(items);
			LOGGER.trace("OK");
			return group;
		} finally {
			LOGGER.trace("END");
		}
	}

	private ItemImpl findItem(List<ItemImpl> items, String name) {
		for (ItemImpl item : items) {
			if (item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

	private static long getLastModified(List<Item> items) {
		long lastModified = 0;
		for (Item item : items) {
			// To je divny ... ta podminka ma byt naopak !!!
			if (lastModified > ((IndexImpl) item.getIndex()).getMs()) {
				lastModified = ((IndexImpl) item.getIndex()).getMs();
			}
		}
		return lastModified;
	}

	private static List<ItemImpl> getItems(Client client, String dirRoot,
			String dir, long index, Pattern pattern) throws ABaseException {
		List<ItemImpl> items = new ArrayList<ItemImpl>();

		Vector<LsEntry> files = null;
		try {
			files = client.getChannel().ls(dir);
		} catch (SftpException ex) {
			throw new TechnicalException(
					"Cannot read the content of the directory: " + dir, ex);
		}

		if (files == null) {
			return items;
		}

		for (LsEntry file : files) {
			if (!file.getAttrs().isDir()) {
				long ms = 1000L * (long) file.getAttrs().getATime();
				if (ms <= index) {
					// Ignor old files
					continue;
				}
				String path = adaptPath(dirRoot, dir + file.getFilename());

				if (pattern != null && !pattern.matcher(path).matches()) {
					LOGGER.debug("Ignored file: " + file);
					continue;
				}

				ItemImpl item = new ItemImpl(path);
				IndexImpl indexImpl = new IndexImpl();
				indexImpl.setMs(ms);
				item.setIndex(indexImpl);
				item.setStatus(Status.NEW);
				items.add(item);
			} else if (file.getAttrs().isDir()) {
				if (".".equals(file.getFilename())
						|| "..".equals(file.getFilename())) {
					continue;
				}
				String path = dir + file.getFilename() + PATH_SEPARATOR;
				List<ItemImpl> itemsLocal = getItems(client, dirRoot, path,
						index, pattern);
				items.addAll(itemsLocal);
			}
		}

		Collections.sort(items, comparatorItem);

		return items;
	}

	public List<Group> loadCurrents() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			LOGGER.debug("diff: " + name);
			List<Group> groups = new ArrayList<Group>();

			if (dirInfos.isEmpty()) {
				LOGGER.trace("OK");
				return groups;
			}
			Client client = connect();

			try {
				for (DirInfo dirInfo : dirInfos) {
					if (dirInfo.isContent()) {
						Group group = loadDiff(client, dirInfo, null);
						groups.add(group);
					} else {
						Group group = loadCurrent(client, dirInfo);
						groups.add(group);
					}
				}
			} finally {
				disconnect(client);
			}
			LOGGER.trace("OK");
			return groups;
		} finally {
			LOGGER.trace("END");
		}
	}

	private Group loadCurrent(Client client, DirInfo dirInfo)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			long lastModified = getLastModifiedDirectory(client,
					dirInfo.getPath(), dirInfo.getPath(), 0,
					dirInfo.getPattern());
			GroupImpl group = new GroupImpl(dirInfo.getName());
			IndexImpl indexImpl = new IndexImpl();
			indexImpl.setMs(lastModified);
			group.setLastIndex(indexImpl);
			LOGGER.trace("OK");
			return group;
		} finally {
			LOGGER.trace("END");
		}
	}

	private static long getLastModifiedDirectory(Client client, String dirRoot,
			String dir, long lastModified, Pattern pattern)
			throws ABaseException {

		Vector<LsEntry> files = null;
		try {
			files = client.getChannel().ls(dir);
		} catch (SftpException ex) {
			throw new TechnicalException(
					"Cannot read the content of the directory: " + dir, ex);
		}

		if (files == null) {
			return 0;
		}

		for (LsEntry file : files) {
			if (!file.getAttrs().isDir()) {
				LOGGER.debug("file: " + file.getFilename());

				String path = adaptPath(dirRoot, dir + file.getFilename());
				if (pattern != null && !pattern.matcher(path).matches()) {
					LOGGER.debug("Ignored file: " + file);
					continue;
				}
				long ms = 1000L * (long) file.getAttrs().getATime();
				lastModified = lastModified >= ms ? lastModified : ms;
			} else if (file.getAttrs().isDir()) {
				String path = dir + file.getFilename() + PATH_SEPARATOR;
				long lastModifiedLocal = getLastModifiedDirectory(client,
						dirRoot, path, lastModified, pattern);
				lastModified = lastModified >= lastModifiedLocal ? lastModified
						: lastModifiedLocal;
			}
		}

		return lastModified;
	}

	private static String adaptPath(String dir, String file) {
		String relativePath = file.substring(dir.length());
		if (relativePath.startsWith(PATH_SEPARATOR)) {
			relativePath = relativePath.substring(PATH_SEPARATOR.length());
		}
		relativePath = relativePath
				.replace(PATH_SEPARATOR, UNIX_PATH_SEPARATOR);
		return relativePath;
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

	private Client connect() throws ABaseException {
		Session session = null;
		ChannelSftp sftpChannel = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(ftpLogin, ftpHost, ftpPort);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(ftpPassword);
			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			Client client = new Client();
			client.setSession(session);
			client.setChannel(sftpChannel);
			return client;
		} catch (JSchException ex) {
			throw new TechnicalException("SFTP Cannot connect to the server: "
					+ ftpHost + ":" + ftpPort, ex);
		}
	}

	private void disconnect(Client client) {
		if (client == null) {
			return;
		}
		if (client.getChannel() != null) {
			client.getChannel().disconnect();
		}
		if (client.getSession() != null) {
			client.getSession().disconnect();
		}
	}
}
