package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class RevLineItemRecord extends StandardRecord {
	public RevLineItemRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().revLineItems;
	}
} //RevLineItemRecord
