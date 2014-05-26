package com.capgemini.ktestmachine.component.diffmanager.filesystem.local;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.ktestmachine.component.diffmanager.filesystem.DirInfo;
import com.capgemini.ktestmachine.exception.ConfigurationException;



public abstract class ADiffManagerFilesystemFwk {
    private boolean configured;

	protected String name;
	protected List<DirInfo> dirInfos;

	public void config() throws ConfigurationException {
		configured = false;
		if (dirInfos == null) {
			dirInfos = new ArrayList<DirInfo>();
		} else {
			for (DirInfo dirInfo : dirInfos) {
				if (dirInfo.getPath() == null) {
					throw new ConfigurationException(getClass().getSimpleName() + "." + dirInfo.getClass().getSimpleName()
							+ ": Parameter dir is not configured");
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
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
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
}
