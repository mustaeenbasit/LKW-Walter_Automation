package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class CaseRecord extends StandardRecord {
	public CaseRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().cases;
	}
} // CaseRecord
