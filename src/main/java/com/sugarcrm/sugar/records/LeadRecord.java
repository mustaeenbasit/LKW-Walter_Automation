package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class LeadRecord extends StandardRecord {
	public LeadRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().leads;
	}
	
	/**
	 * getRecordIdentifier will return the Person Type Records First Name
	 * @return - String of the records Identification (first_name last_name)
	 */
	@Override
	public String getRecordIdentifier(){
		String leadIdentifier = "";
		String salutation = get("salutation");
		String firstName = get("firstName");
		String lastName = get("lastName");

/*   The block is commented because sugar doesn't support salutation field for contact search now
		if (!salutation.isEmpty()){
			leadIdentifier = leadIdentifier + salutation;
		}
*/
		salutation = ""; // it is made at the same reason

		if ((firstName != null && !firstName.isEmpty()) && (salutation != null && !salutation.isEmpty())){
			leadIdentifier = leadIdentifier + ' ' + firstName;
		} else leadIdentifier = firstName;
		if ((lastName != null && !lastName.isEmpty()) && ((firstName != null && !firstName.isEmpty()) || (salutation != null && !salutation.isEmpty()))){
			leadIdentifier = leadIdentifier + ' ' + lastName;
		} else leadIdentifier = lastName;

		return leadIdentifier;
	}
} // end LeadRecord