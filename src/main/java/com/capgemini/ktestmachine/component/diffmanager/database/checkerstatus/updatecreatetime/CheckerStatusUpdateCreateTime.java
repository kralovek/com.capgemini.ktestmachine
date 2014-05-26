package com.capgemini.ktestmachine.component.diffmanager.database.checkerstatus.updatecreatetime;

import java.util.Date;
import java.util.Map;

import com.capgemini.ktestmachine.component.diffmanager.DiffManager.Status;
import com.capgemini.ktestmachine.component.diffmanager.database.ItemCruid;
import com.capgemini.ktestmachine.component.diffmanager.database.checkerstatus.CheckerStatus;
import com.capgemini.ktestmachine.exception.ABaseException;

public class CheckerStatusUpdateCreateTime extends
		ACheckerStatusUpdateCreateTimeFwk implements CheckerStatus {
	public Status checkStatus(long index, ItemCruid itemCruid) throws ABaseException {
		if (itemCruid == null || itemCruid.getParameters() == null || itemCruid.getParameters().isEmpty()) {
			return Status.NEW;
		}
		
		Object valueCreate = null;
		Object valueUpdate = null;
		
		for (Map.Entry<String, Object> entry : itemCruid.getParameters().entrySet()) {
			if (this.columnCreate.equals(entry.getKey())) {
				valueCreate = entry.getValue();
			}
			if (this.columnUpdate.equals(entry.getKey())) {
				valueUpdate = entry.getValue();
			}
		}
		
		if (valueCreate == null || valueUpdate == null) {
			return Status.NEW;
		}
		
		if (valueCreate.equals(valueUpdate)) {
			return Status.NEW;
		}
		
		Date dateTs = new Date(index);
		Date dateCreate = null;
		
		if (valueCreate instanceof Date) {
			dateCreate = (Date) valueCreate;
		}
		
		if (dateTs.compareTo(dateCreate) > 0) {
			return Status.UPD;
		}
		
		return Status.NEW;
	}
}
