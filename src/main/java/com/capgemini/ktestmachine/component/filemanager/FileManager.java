package com.capgemini.ktestmachine.component.filemanager;

import com.capgemini.ktestmachine.exception.ABaseException;

public interface FileManager {

	void contentToFile(String content, String filename, String encoding, String dir) throws ABaseException;
	void contentToGzFile(String content, String filename, String encoding, String dir) throws ABaseException;
}
