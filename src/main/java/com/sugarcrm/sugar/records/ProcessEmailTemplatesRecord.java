package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class ProcessEmailTemplatesRecord extends StandardRecord {
	public ProcessEmailTemplatesRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().processEmailTemplates;
	}
} // ProcessEmailTemplatesRecord