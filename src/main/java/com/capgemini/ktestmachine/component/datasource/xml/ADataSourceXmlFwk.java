package com.capgemini.ktestmachine.component.datasource.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.file.UtilsFile;
import com.capgemini.ktestmachine.utils.string.UtilsString;



public abstract class ADataSourceXmlFwk {
    private boolean configured;

	private String fileRequeadb;
	protected String driver;
	protected String user;
	protected String password;
	protected String url;

	protected void config() throws ConfigurationException {
		configured = false;
		if (fileRequeadb == null) {
			throw new ConfigurationException(this.getClass().getName()
					+ ": Le param�tre 'fileRequeadb' n'est pas configur�.");
		} else {
			configDatabase();
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	private void configDatabase() throws ConfigurationException {
		InputStream inputStream = null;
		try {
			inputStream = openRessource(fileRequeadb);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(inputStream);

			Element docEle = dom.getDocumentElement();
			driver = getValueFromElementXML(docEle, "DbDriver");
			url = getValueFromElementXML(docEle, "DbURL");
			user = getValueFromElementXML(docEle, "DbUser");
			password = getValueFromElementXML(docEle, "DbPassword");
			
			inputStream.close();
			inputStream = null;
			
			if (UtilsString.isEmpty(driver)) {
				throw new ConfigurationException(this.getClass().getName()
						+ ": Le param�tre 'DbDriver' n'est pas configur� dans le fichier: " + fileRequeadb);
			}
			if (UtilsString.isEmpty(url)) {
				throw new ConfigurationException(this.getClass().getName()
						+ ": Le param�tre 'DbURL' n'est pas configur� dans le fichier: " + fileRequeadb);
			}
			if (UtilsString.isEmpty(user)) {
				throw new ConfigurationException(this.getClass().getName()
						+ ": Le param�tre 'DbUser' n'est pas configur� dans le fichier: " + fileRequeadb);
			}
			if (UtilsString.isEmpty(password)) {
				throw new ConfigurationException(this.getClass().getName()
						+ ": Le param�tre 'DbPassword' n'est pas configur� dans le fichier: " + fileRequeadb);
			}

		} catch (IOException ex) {
			throw new ConfigurationException(
					this.getClass().getName()
							+ ": Probl�me de lire le fichier de configuration de la base de requea a un mauvais format: "
							+ fileRequeadb, ex);
		} catch (Exception ex) {
			throw new ConfigurationException(
					this.getClass().getName()
							+ ": Le fichier de configuration de la base de requea a un mauvais format: "
							+ fileRequeadb, ex);
		} finally {
			if (inputStream != null) {
				UtilsFile.getInstance().close(inputStream);
				inputStream = null;
			}
		}
	}

	private InputStream openRessource(String path) throws ConfigurationException {
		InputStream inputStream = null;
		File file = new File(path);
		if (file.exists()) {
			try {
				inputStream = new FileInputStream(file);
			} catch (IOException ex) {
				throw new ConfigurationException(
						this.getClass().getName()
								+ ": Impossible de lire le fichier de configuration de la base: "
								+ file.getAbsolutePath(), ex);
			}
		} else {
			ClassLoader classLoader = this.getClass().getClassLoader();
			inputStream = classLoader.getResourceAsStream(fileRequeadb);
			while ((classLoader = classLoader.getParent()) != null) {
				inputStream = classLoader.getResourceAsStream(fileRequeadb);
				if (inputStream != null) {
					break;
				}
			}
			if (inputStream == null) {
				
				throw new ConfigurationException(
						this.getClass().getName()
								+ ": Impossible de lire le fichier de configuration de la base depuis le classpath: "
								+ file);
			}
		}
		return inputStream;
	}

	private static String getValueFromElementXML(Element element, String field) {
		String textVal = null;
		NodeList nl = element.getElementsByTagName(field);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	public String getFileRequeadb() {
		return fileRequeadb;
	}

	public void setFileRequeadb(String fileRequeadb) {
		this.fileRequeadb = fileRequeadb;
	}
}
