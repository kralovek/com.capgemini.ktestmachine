package com.capgemini.ktestmachine.component.exceladapter.poi;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.capgemini.ktestmachine.exception.ConfigurationException;

public class AExcelAdapterPoiFwk {
	private boolean configured;

	private String dateFormat;
	protected SimpleDateFormat datePattern;

	private String timeFormat;
	protected SimpleDateFormat timePattern;

	public void config() throws ConfigurationException {
		configured = false;
		if (dateFormat == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter 'datePattern' is not configured");
		} else {
			try {
				datePattern = new SimpleDateFormat(dateFormat);
				datePattern.format(new Date());
			} catch (Exception ex) {
				throw new ConfigurationException(getClass().getSimpleName()
						+ ": Parameter 'datePattern' has bad value: "
						+ dateFormat);
			}
		}
		if (timeFormat == null) {
			timeFormat = dateFormat;
			timePattern = datePattern;
		} else {
			try {
				timePattern = new SimpleDateFormat(timeFormat);
				timePattern.format(new Date());
			} catch (Exception ex) {
				throw new ConfigurationException(getClass().getSimpleName()
						+ ": Parameter 'timePattern' has bad value: "
						+ timeFormat);
			}
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": the component must be configured.");
		}
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
}
