package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class AccountRecord extends StandardRecord {
	public AccountRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().accounts;
	}
} // AccountRecord
