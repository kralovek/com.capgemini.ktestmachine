package com.capgemini.ktestmachine.component.locker.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.locker.Locker;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.capgemini.ktestmachine.utils.errors.StopException;
import com.capgemini.ktestmachine.utils.file.UtilsFile;

public class LockerFile extends ALockerFileFwk implements Locker {
	private static final Logger LOGGER = Logger.getLogger(LockerFile.class);

	private static class Entity {
		private static final DateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss.SSS");
		private String name;
		private long time;

		public Entity(String name, long time) {
			super();
			this.name = name;
			this.time = time;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return "[" + name + " " + dateFormat.format(new Date(time)) + "]";
		}
	}

	private static final Comparator<File> comparatorFiles = new Comparator<File>() {
	    public int compare(File file1, File file2) {
	    	int result = new Long(file1.lastModified()).compareTo(file2.lastModified());
	    	if (result != 0) {
	    		return result;
	    	}
	    	return file1.getName().compareTo(file2.getName());
	    }
	};
	
	private final FileFilter fileFilterLock = new FileFilter() {
		public boolean accept(File file) {
			if (!file.isFile()) {
				return false;
			}
			Matcher matcher = whoamiFilterPattern.matcher(file.getName());
			if (!matcher.matches()) {
				return false;
			}
			return true;
		}
	};

	public void lock() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			checkDirectories();

			createLockFile();
			
			List<Entity> waitings = null;

			long waited = 0;
			int lastCount = 0;
			boolean locked = false;
			do {
				waitings = findEntities();

				if (waitings.get(0).getName().equals(file.getName())) {
					locked = true;
					break;
				}

				if (lastCount != waitings.size()) {
					printWaitings(waitings);
				}

				try {
					Thread.sleep(waitInterval);
				} catch (InterruptedException ex) {
					throw new TechnicalException(
							"Probleme to sleep the process", ex);
				}

				lastCount = waitings.size();

				waited += waitInterval;

				if (stopFile != null && stopFile.isFile()) {
					stopFile.delete();
					break;
				}
				
			} while (waitMax == null || waited <= waitMax);

			if (!locked) {
				LOGGER.warn("STOPPING the proces - I cannot wait more");
				printWaitings(waitings);
				throw new StopException("No more waiting");
			}
			
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	public void unlock() throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			checkDirectories();

			if (deleteLockFile()) {
				LOGGER.debug("UNLOCK");
			}
			
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void checkDirectories() throws ABaseException {
		if (!dir.isDirectory()) {
			if (!dir.mkdirs()) {
				throw new TechnicalException("Cannot create the lock directory");
			}
		}
	}

	private void createLockFile() throws ABaseException {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file, true);
			fileWriter.close();
			fileWriter = null;
			LOGGER.debug("LOCK");
		} catch (IOException ex) {
			throw new TechnicalException("Cannot create the lock file: " + file.getAbsolutePath(), ex);
		} finally {
			UtilsFile.getInstance().close(fileWriter);
		}
	}

	private boolean deleteLockFile() throws ABaseException {
		if (file.delete()) {
			return true;
		} else if (!file.isFile()) {
			return false;
		} else {
			throw new TechnicalException("Cannot delete the lock file: " + file.getAbsolutePath());
		}
		
	}

	private void printWaitings(List<Entity> waitings) {
		LOGGER.info("Waiting for: ");
		for (Entity entity : waitings) {
			if (entity.getName().equals(file.getName())) {
				break;
			}
			LOGGER.info(entity.toString());
		}
	}
	
	
	private List<Entity> findEntities() throws ABaseException {
		List<Entity> list = new ArrayList<LockerFile.Entity>();
		
		File[] files = dir.listFiles(fileFilterLock);
		Arrays.sort(files, comparatorFiles);
		
		boolean found = false;
		for (File file : files) {
			Entity entity = new Entity(file.getName(), file.lastModified());
			list.add(entity);
			if (file.getName().equals(this.file.getName())) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new TechnicalException("I cannot find the lock I have created before: " + file.getAbsolutePath());
		}
		
		return list;
	}
}
