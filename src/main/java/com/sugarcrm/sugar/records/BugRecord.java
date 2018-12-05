package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class BugRecord extends StandardRecord {
	public BugRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().bugs;
	}
} // BugRecord
