package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class ProcessBusinessRulesRecord extends StandardRecord {
	public ProcessBusinessRulesRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().processBusinessRules;
	}
} // ProcessBusinessRecord
