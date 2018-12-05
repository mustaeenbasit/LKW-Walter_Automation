package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class QuoteRecord extends BWCRecord {
	public QuoteRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().quotes;
	}
} // QuoteRecord
