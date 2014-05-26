package com.capgemini.ktestmachine.component.diffmanager.database.checkerstatus;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager;
import com.capgemini.ktestmachine.component.diffmanager.database.ItemCruid;
import com.capgemini.ktestmachine.exception.ABaseException;

public interface CheckerStatus {

	DiffManager.Status checkStatus(long index, ItemCruid itemCruid) throws ABaseException;
}
