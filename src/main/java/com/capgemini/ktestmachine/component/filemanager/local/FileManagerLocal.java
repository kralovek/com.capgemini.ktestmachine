package com.capgemini.ktestmachine.component.filemanager.local;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.filemanager.FileManager;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.exception.TechnicalException;

public class FileManagerLocal extends AFileManagerLocalFwk implements FileManager {
	private static final Logger LOGGER = Logger
			.getLogger(FileManagerLocal.class);


	public void contentToFile(String content, String filename, String encoding,
			String dir) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			File dirTarget = new File(dir);
			File file = new File(dirTarget, filename);
			contentToFile(content, file, encoding);

			File fileTrace = generateTraceFile(content, new Date(), filename,
					encoding);
			if (fileTrace != null) {
				contentToFile(content, fileTrace, encoding);
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	public void contentToGzFile(String content, String filename,
			String encoding, String dir) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			testConfigured();
			File dirTarget = new File(dir);
			File file = new File(dirTarget, filename);
			contentToGzFile(content, file, encoding);

			File fileTrace = generateTraceFile(content, new Date(), filename,
					encoding);
			if (fileTrace != null) {
				contentToGzFile(content, fileTrace, encoding);
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void contentToFile(String content, File file, String encoding)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			try {
				fos = new FileOutputStream(file);
				if ("UTF-8".equals(encoding)) {
					writeBomUtf8(fos);
				}

				osw = new OutputStreamWriter(fos, encoding);

				osw.write(content);

				osw.close();
				osw = null;
				fos.close();
				fos = null;
			} catch (IOException ex) {
				throw new TechnicalException("Impossible to create the file: "
						+ file.getAbsolutePath(), ex);
			} finally {
				closeResource(osw);
				closeResource(fos);
			}
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private void contentToGzFile(String content, File file, String encoding)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			GZIPOutputStream gzos = null;
			try {
				fos = new FileOutputStream(file);

				gzos = new GZIPOutputStream(fos);

				if ("UTF-8".equals(encoding)) {
					writeBomUtf8(gzos);
				}

				osw = new OutputStreamWriter(gzos, encoding);

				osw.write(content);

				osw.close();
				osw = null;
				gzos.close();
				gzos = null;
				fos.close();
				fos = null;
			} catch (IOException ex) {
				throw new TechnicalException("Impossible to create the file: "
						+ file.getAbsolutePath(), ex);
			} finally {
				closeResource(osw);
				closeResource(gzos);
				closeResource(fos);
			}
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	private static void closeResource(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ex) {
			}
		}
	}

	private static void writeBomUtf8(OutputStream fos) throws IOException {
		fos.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
		fos.flush();
	}

	private File generateTraceFile(String content, Date date, String filename,
			String encoding) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			File file = null;
			if (traceDataFile != null) {
				String traceDataFileAdapted = traceDataFile.replace("%0",
						filename);

				DateFormat traceDataPattern = null;
				try {
					traceDataPattern = new SimpleDateFormat(
							traceDataFileAdapted);
				} catch (Exception ex) {
					throw new ConfigurationException(getClass().getSimpleName()
							+ ": Parameter traceDataFile has bad value: "
							+ ex.getMessage());
				}
				String path = traceDataPattern.format(date);
				file = new File(path);
				LOGGER.debug("Logging the Data file to: " + file.toString());
				if (file.getParentFile() != null
						&& !file.getParentFile().isDirectory()
						&& !file.getParentFile().mkdirs()) {
					throw new TechnicalException(
							"Cannot create the directory: "
									+ file.getParentFile().getAbsolutePath());
				}

				contentToFile(content, file, encoding);
			}

			LOGGER.trace("OK");
			return file;
		} finally {
			LOGGER.trace("END");
		}
	}
}
