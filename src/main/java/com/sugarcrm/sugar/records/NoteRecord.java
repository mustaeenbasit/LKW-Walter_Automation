package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class NoteRecord extends StandardRecord {
	
	public NoteRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().notes;
		putAll(data);
	}
	
	/**
	 * getRecordIdentifier will return the Note Records Subject
	 * @return - String of the records Identification (subject)
	 */
	@Override
	public String getRecordIdentifier(){
		return get("subject");
	}
	
} // end NoteRecord