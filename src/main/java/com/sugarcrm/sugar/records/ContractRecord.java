package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class ContractRecord extends BWCRecord {
	public ContractRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().contracts;
	}
} // ContractRecord