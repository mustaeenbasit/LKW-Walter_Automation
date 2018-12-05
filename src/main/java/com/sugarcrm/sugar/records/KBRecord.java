package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class KBRecord extends StandardRecord {
	public KBRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().knowledgeBase;
	}
} // KBRecord
