package com.capgemini.ktestmachine.component.synthesereporter.textfile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.synthesereporter.SyntheseReporter;
import com.capgemini.ktestmachine.data.Synthese;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.capgemini.ktestmachine.utils.file.UtilsFile;

public class SyntheseReporterTextFile extends SyntheseReporterTextFileFwk implements
		SyntheseReporter {
	private static final Logger LOGGER = Logger
			.getLogger(SyntheseReporterTextFile.class);

	private static final Comparator<Synthese> COMPARATOR = new Comparator<Synthese>() {

		public int compare(Synthese synthese1, Synthese synthese2) {
			int result;
			result = synthese1.getSource().compareTo(synthese2.getSource());
			if (result != 0) {
				return result;
			}
			result = synthese1.getType().compareTo(synthese2.getType());
			if (result != 0) {
				return result;
			}
			result = synthese1.getId().compareTo(synthese2.getId());
			return result;
		}
	};

	public void reportSyntheses(String source, List<Synthese> syntheses)
			throws ABaseException {
		LOGGER.trace("BEGIN");
		try {
			File fileSource = new File(source);
			File fileReport = new File(dir, fileSource.getName() + ".txt");

			PrintWriter printWriter = null;
			try {
				UtilsFile.getInstance().createFileDirecctory(fileReport);
				
				printWriter = new PrintWriter(fileReport);
				if (sort) {
					Collections.sort(syntheses, COMPARATOR);
				}
				String lastSource = null;
				String lastType = null;

				for (Synthese synthese : syntheses) {
					if (patternIgnorName != null
							&& patternIgnorName.matcher(synthese.getName())
									.matches()) {
						continue;
					}

					if (lastSource == null
							|| lastSource.equals(synthese.getSource())) {
						printWriter.println("{" + synthese.getSource() + "}");
					}
					lastSource = synthese.getSource();
					if (lastType == null || lastType.equals(synthese.getType())) {
						printWriter.println("\t<" + synthese.getType() + ">");
					}
					lastType = synthese.getType();
					printWriter.print("\t\t[" + synthese.getType() + "]");

					if (synthese.getResult() != null) {
						String result = synthese.getResult() ? "OK" : "ERROR";
						printWriter.println(": " + result);
					} else {
						printWriter.println();
					}
					if (printName || printDescription) {
						if (printName) {
							printWriter.print(" Name: " + synthese.getName());
						}
						if (printDescription) {
							printWriter.print(" Description: "
									+ synthese.getDescription());
						}
						printWriter.println();
					}
				}

				printWriter.close();
				printWriter = null;
			} catch (IOException ex) {
				throw new TechnicalException(
						"Cannot create the synthese file: "
								+ fileReport.getAbsolutePath(), ex);
			} finally {
				UtilsFile.getInstance().close(printWriter);
			}

			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}
}
