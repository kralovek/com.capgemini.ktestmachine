package com.capgemini.ktestmachine.component.executant.webservice.requeaws;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.capgemini.ktestmachine.component.templateparser.TemplateParser;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;



public abstract class AExecutantWebServiceRequeaFwk {

	private static final String ENCODE_UTF_8 = "UTF-8";

	private static final String PARAM_SOAP_PATH = "SOAP_PATH";
	private static final String PARAM_SOAP_HOST = "SOAP_HOST";
	private static final String PARAM_SOAP_PORT = "SOAP_PORT";

	private boolean configured;
	
	protected URL url;
	protected String encoding;
	
	protected File fileTemplate;
	protected TemplateParser templateParser;
	protected Map<String, String> parameters;

	public void config() throws ABaseException {
		configured = false;
		if (parameters == null) {
			parameters = new HashMap<String, String>();
		}
		if (url == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter url is not configured");
		} else if (!"http".equals(url.getProtocol())
				&& !"https".equals(url.getProtocol())) {
			throw new ConfigurationException(
					getClass().getSimpleName()
							+ ": Parameter url contains unsupported protocol (supported are http|https): "
							+ url.getProtocol());
		} else {
			if (!parameters.containsKey(PARAM_SOAP_PATH)) {
				parameters.put(PARAM_SOAP_PATH, url.getHost());
			}
			if (!parameters.containsKey(PARAM_SOAP_HOST)) {
				parameters.put(PARAM_SOAP_HOST, url.getHost());
			}
			if (!parameters.containsKey(PARAM_SOAP_PORT)) {
				parameters.put(PARAM_SOAP_PORT, url.getHost());
			}
		}
		if (fileTemplate == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter fileTemplate is not configured");
		}
		if (templateParser == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter templateParser is not configured");
		}
		if (encoding == null) {
			encoding = ENCODE_UTF_8;
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(this.getClass().getName()).append("]\n");
		buffer.append("    ").append("url").append("=")
				.append(url == null ? "" : url.toString()).append("\n");
		return buffer.toString();
	}
	
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public File getFileTemplate() {
		return fileTemplate;
	}

	public void setFileTemplate(File fileTemplate) {
		this.fileTemplate = fileTemplate;
	}

	public TemplateParser getTemplateParser() {
		return templateParser;
	}

	public void setTemplateParser(TemplateParser templateParser) {
		this.templateParser = templateParser;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}
