package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class TargetListRecord extends StandardRecord {
	public TargetListRecord(FieldSet data) throws Exception{
		super(data);
		module = sugar().targetlists;
	}
	
	/**
	 * getRecordIdentifier will return the basic Type Records Name
	 * @return - String of the records Identification (targetlistName)
	 */
	@Override
	public String getRecordIdentifier(){
		//TODO: This method needs to be made to handle different situations
		return get("targetlistName");
	}
} // TargetListRecord