package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class ProcessDefinitionRecord extends StandardRecord {
	public ProcessDefinitionRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().processDefinitions;
	}
} // ProcessDefinitionRecord
