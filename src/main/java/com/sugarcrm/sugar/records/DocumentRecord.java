package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class DocumentRecord extends BWCRecord {
	public DocumentRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().documents;
	}
	
	/**
	 * getRecordIdentifier will return the document name
	 * @return - String of the records Identification (documentName)
	 */
	@Override
	public String getRecordIdentifier(){
		return get("documentName");
	}
} // DocumentRecord
