package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class TagRecord extends StandardRecord {
	public TagRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().tags;
	}
} // TagRecord
