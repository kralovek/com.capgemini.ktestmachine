package com.capgemini.ktestmachine.component.lancer.m2o_importfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager;
import com.capgemini.ktestmachine.component.filemanager.FileManager;
import com.capgemini.ktestmachine.component.locker.Locker;
import com.capgemini.ktestmachine.component.resultparser.ResultParser;
import com.capgemini.ktestmachine.component.runner.Runner;
import com.capgemini.ktestmachine.component.templatearchiv.TemplateArchiv;
import com.capgemini.ktestmachine.component.templateparser.TemplateParser;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.parameters.UtilsParameters;



public abstract class ALancerM2OImportFileFwk {
	private boolean configured;

	protected TemplateArchiv templateArchiv;

	protected TemplateParser templateParser;

	protected FileManager fileManager;
	private Map<String, FileManager> fileManagerByType;
	protected Map<Pattern, FileManager> fileManagerByTypePattern;

	protected Runner runner;
	private Map<String, Runner> runnerByType;
	protected Map<Pattern, Runner> runnerByTypePattern;

	protected List<DiffManager> diffManagers;
	private Map<String, List<DiffManager>> diffManagersByType;
	protected Map<Pattern, List<DiffManager>> diffManagersByTypePattern;

	protected ResultParser resultParser;

	protected String dirDestination;
	private Map<String, String> dirDestinationByType;
	protected Map<Pattern, String> dirDestinationByTypePattern;

	private String traceDiffFile;
	protected DateFormat traceDiffPattern;

	private String destinationFilename;
	protected DateFormat destinationFilenamePattern;
	
    protected String sysParamPrefix;

    protected Locker locker;

	public void config() throws ABaseException {
		configured = false;
		if (templateArchiv == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter templateArchiv is not configured");
		}
		if (templateParser == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter templateParser is not configured");
		}

		if (runnerByType == null) {
			runnerByType = new HashMap<String, Runner>();
		}
		if (runner == null && (runnerByType.isEmpty())) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Neither parameter runner nor runnerByType are configured");
		}
		runnerByTypePattern = UtilsParameters.toByTypePattern(getClass(), runnerByType);
		
		if (fileManagerByType == null) {
			fileManagerByType = new LinkedHashMap<String, FileManager>();
		}
		if (fileManager == null
				&& fileManagerByType.isEmpty()) {
			throw new ConfigurationException(
					getClass().getSimpleName()
							+ ": Neither parameter fileManager nor fileManagerByType are configured");
		}
		if (diffManagersByType == null) {
			diffManagersByType = new LinkedHashMap<String, List<DiffManager>>();
		}
		if (diffManagers == null) {
			diffManagers = new ArrayList<DiffManager>();
		}
		diffManagersByTypePattern = UtilsParameters.toByTypePattern(getClass(), diffManagersByType);
		
		if (resultParser == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter resultParser is not configured");
		}
		if (dirDestinationByType == null) {
			dirDestinationByType = new LinkedHashMap<String, String>();
		}
		if (dirDestination == null && dirDestinationByType.isEmpty()) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter dirReception or at least one dirReceptionByType are not configured");
		}
		dirDestinationByTypePattern = UtilsParameters.toByTypePattern(getClass(), dirDestinationByType);
		
		if (traceDiffFile != null) {
			try {
				traceDiffPattern = new SimpleDateFormat(traceDiffFile);
			} catch (Exception ex) {
				throw new ConfigurationException(getClass().getSimpleName()
						+ ": Parameter traceDiffFile has bad value: "
						+ ex.getMessage());
			}
		}
		if (destinationFilename != null) {
			try {
				destinationFilenamePattern = new SimpleDateFormat(destinationFilename);
			} catch (Exception ex) {
				throw new ConfigurationException(getClass().getSimpleName()
						+ ": Parameter destinationFilename has bad value: "
						+ ex.getMessage());
			}
		}
		if (sysParamPrefix == null) {
			throw new ConfigurationException(getClass().getSimpleName()
					+ ": Parameter sysParamPrefix is not configured");
		} else if (!sysParamPrefix.isEmpty() && !sysParamPrefix.endsWith("/")) {
			sysParamPrefix += "/";
		}
		if (locker == null) {
			// OK
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName()
					+ ": Le composant n'est pas configur�.");
		}
	}

	public TemplateArchiv getTemplateArchiv() {
		return templateArchiv;
	}

	public void setTemplateArchiv(final TemplateArchiv pTemplateArchiv) {
		this.templateArchiv = pTemplateArchiv;
	}

	public TemplateParser getTemplateParser() {
		return templateParser;
	}

	public void setTemplateParser(final TemplateParser pTemplateParser) {
		this.templateParser = pTemplateParser;
	}

	public ResultParser getResultParser() {
		return resultParser;
	}

	public void setResultParser(final ResultParser pResultParser) {
		this.resultParser = pResultParser;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public Map<String, FileManager> getFileManagerByType() {
		return fileManagerByType;
	}

	public void setFileManagerByType(Map<String, FileManager> fileManagerByType) {
		this.fileManagerByType = fileManagerByType;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	public Map<String, Runner> getRunnerByType() {
		return runnerByType;
	}

	public void setRunnerByType(Map<String, Runner> runnerByType) {
		this.runnerByType = runnerByType;
	}

	public List<DiffManager> getDiffManagers() {
		return diffManagers;
	}

	public void setDiffManagers(List<DiffManager> diffManagers) {
		this.diffManagers = diffManagers;
	}

	public String getDirDestination() {
		return dirDestination;
	}

	public void setDirDestination(String dirDestination) {
		this.dirDestination = dirDestination;
	}

	public String getTraceDiffFile() {
		return traceDiffFile;
	}

	public void setTraceDiffFile(String traceDiffFile) {
		this.traceDiffFile = traceDiffFile;
	}

	public String getSysParamPrefix() {
		return sysParamPrefix;
	}

	public void setSysParamPrefix(String sysParamPrefix) {
		this.sysParamPrefix = sysParamPrefix;
	}

	public Map<String, List<DiffManager>> getDiffManagersByType() {
		return diffManagersByType;
	}

	public void setDiffManagersByType(Map<String, List<DiffManager>> diffManagersByType) {
		this.diffManagersByType = diffManagersByType;
	}

	public Map<String, String> getDirDestinationByType() {
		return dirDestinationByType;
	}

	public void setDirDestinationByType(Map<String, String> dirDestinationByType) {
		this.dirDestinationByType = dirDestinationByType;
	}

	public Locker getLocker() {
		return locker;
	}

	public void setLocker(Locker locker) {
		this.locker = locker;
	}

	public String getDestinationFilename() {
		return destinationFilename;
	}

	public void setDestinationFilename(String destinationFilename) {
		this.destinationFilename = destinationFilename;
	}
}
