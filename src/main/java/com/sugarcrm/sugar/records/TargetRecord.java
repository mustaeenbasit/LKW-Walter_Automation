package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class TargetRecord extends StandardRecord {
	public TargetRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().targets;
	}
	
	/**
	 * getRecordIdentifier will return the Person Type Records First Name
	 * @return - String of the records Identification (first name)
	 */
	@Override
	public String getRecordIdentifier(){
		/*
		 * TODO: 	This method needs to be made to handle different situations.
		 * 			It is not what we want, a person isn't Identified by his firstName alone. We need to figure out how to better take care of this.
		 */
		return get("firstName");
	}
} // end TargetRecord
