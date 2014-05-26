package com.capgemini.ktestmachine.utils.xstream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



public class UtilsXStream {
	private static UtilsXStream utilsXStream = new UtilsXStream();
	
	public static UtilsXStream getInstance() {
		return utilsXStream;
	}

	public void toXMLFile(final Object pObject, final File pFile) throws ABaseException {
		final String xml = toXML(pObject);
		PrintStream printStream = null;
		try {
			printStream = new PrintStream(new FileOutputStream(pFile));
			printStream.print(xml);
		} catch (final Exception ex) {
			throw new TechnicalException("Cannot create the file: " + pFile.getAbsolutePath(), ex);
		} finally {
			try {
				printStream.close();
			} catch (final Exception ex) {
			}
		}
	}

	public String toXML(final Object pObject) {
		XStream xstream = new XStream(new DomDriver());

		final ByteArrayOutputStream byteArrayOutputStream = //
		new ByteArrayOutputStream();

		xstream.toXML(pObject, byteArrayOutputStream);

		final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ byteArrayOutputStream.toString();

		return xml;
	}
}
