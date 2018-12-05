package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class OpportunityRecord extends StandardRecord {
	public OpportunityRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().opportunities;
	}
} // OpportunityRecord
