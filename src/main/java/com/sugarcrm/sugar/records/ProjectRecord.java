package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class ProjectRecord extends BWCRecord {
	public ProjectRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().projects;
	}
} // ProjectRecord