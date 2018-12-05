package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class CallRecord extends ActivityRecord {
	public CallRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().calls;
	}
} // CallRecord