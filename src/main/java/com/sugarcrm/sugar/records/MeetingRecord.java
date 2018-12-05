package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class MeetingRecord extends ActivityRecord {
	public MeetingRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().meetings;
	}
} // MeetingRecord
