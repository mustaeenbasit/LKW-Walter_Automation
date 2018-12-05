package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;

public class TaskRecord extends StandardRecord {
	public TaskRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().tasks;
	}
	
	/**
	 * getRecordIdentifier will return the Task Records Subject
	 * @return - String of the records Identification (subject)
	 */
	@Override
	public String getRecordIdentifier(){
		//TODO: This method needs to be made to handle different situations
		return get("subject");
	}
} // end TaskRecord