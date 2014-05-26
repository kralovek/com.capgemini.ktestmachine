package com.capgemini.ktestmachine.component.synthesereporter.textfile;

import java.io.File;
import java.util.regex.Pattern;

import com.capgemini.ktestmachine.exception.ConfigurationException;

public abstract class SyntheseReporterTextFileFwk {
	private boolean configured;

	protected File dir;
	protected Boolean sort;
	protected Boolean printName;
	protected Boolean printDescription;
	protected Pattern patternIgnorName;
	private String maskIgnoreName;

	public void config() throws ConfigurationException {
		configured = false;

		if (dir == null) {
			dir = new File(".");
		}
		if (sort == null) {
			sort = false;
		}
		if (printName == null) {
			printName = false;
		}
		if (printDescription == null) {
			printDescription = false;
		}
		if (maskIgnoreName != null) {
			try {
				patternIgnorName = Pattern.compile(maskIgnoreName);
			} catch (Exception ex) {
				throw new ConfigurationException(
						"Parameter 'maskIgnoreName' has bad vallue: "
								+ maskIgnoreName);
			}
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": The componet is not configured.");
		}
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public Boolean getSort() {
		return sort;
	}

	public void setSort(Boolean sort) {
		this.sort = sort;
	}

	public String getMaskIgnoreName() {
		return maskIgnoreName;
	}

	public void setMaskIgnoreName(String maskIgnoreName) {
		this.maskIgnoreName = maskIgnoreName;
	}

	public Boolean getPrintName() {
		return printName;
	}

	public void setPrintName(Boolean printName) {
		this.printName = printName;
	}

	public Boolean getPrintDescription() {
		return printDescription;
	}

	public void setPrintDescription(Boolean printDescription) {
		this.printDescription = printDescription;
	}
}
