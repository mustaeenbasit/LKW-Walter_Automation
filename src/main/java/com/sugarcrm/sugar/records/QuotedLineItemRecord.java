package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class QuotedLineItemRecord extends StandardRecord {
	public QuotedLineItemRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().quotedLineItems;
	}
} //QuotedLineItemRecord
