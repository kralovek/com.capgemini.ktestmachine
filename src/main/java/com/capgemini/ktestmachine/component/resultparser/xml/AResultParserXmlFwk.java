package com.capgemini.ktestmachine.component.resultparser.xml;

import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;



/**
 * AResultParserXmlFwk
 * 
 * @author KRALOVEC-99999
 */
public abstract class AResultParserXmlFwk {
    private boolean configured;

	/**
	 * Encoding norm
	 */
	private static final String ENCODE_UTF_8 = "UTF-8";

	protected Boolean useTagPrefix;

	protected Boolean useAttributePrefix;

	protected Boolean useValuePrefix;

	protected String encoding;

	protected String sysParamPrefix;

	public void config() throws ABaseException {
		configured = false;

		useTagPrefix = Boolean.TRUE.equals(useTagPrefix);
		useAttributePrefix = Boolean.TRUE.equals(useAttributePrefix);
		useValuePrefix = Boolean.TRUE.equals(useValuePrefix);

		if (sysParamPrefix == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter sysParamPrefix is not configured");
		} else if (!sysParamPrefix.isEmpty() && !sysParamPrefix.endsWith("/")) {
			sysParamPrefix += "/";
		}

		if (encoding == null) {
			encoding = ENCODE_UTF_8;
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
		}
	}

	public Boolean getUseValuePrefix() {
		return useValuePrefix;
	}

	public void setUseValuePrefix(final Boolean pUseValuePrefix) {
		this.useValuePrefix = pUseValuePrefix;
	}

	public Boolean getUseTagPrefix() {
		return useTagPrefix;
	}

	public void setUseTagPrefix(final Boolean pUseTagPrefix) {
		this.useTagPrefix = pUseTagPrefix;
	}

	public Boolean getUseAttributePrefix() {
		return useAttributePrefix;
	}

	public void setUseAttributePrefix(final Boolean pUseAttributePrefix) {
		this.useAttributePrefix = pUseAttributePrefix;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(final String pEncoding) {
		this.encoding = pEncoding;
	}

	public String getSysParamPrefix() {
		return sysParamPrefix;
	}

	public void setSysParamPrefix(String sysParamPrefix) {
		this.sysParamPrefix = sysParamPrefix;
	}
}
