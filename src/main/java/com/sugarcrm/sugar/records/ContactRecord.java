package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;

public class ContactRecord extends StandardRecord {

	public ContactRecord(FieldSet data) throws Exception{
		super(data);
		module = sugar().contacts;
	}
	
	/**
	 * getRecordIdentifier will return the Person Type Records First Name
	 * @return - String of the records Identification (first_name last_name)
	 */
	@Override
	public String getRecordIdentifier(){
		String contactIdentifier = "";
		String salutation = get("salutation");
		String firstName = get("firstName");
		String lastName = get("lastName");

/*   The block is commented because sugar doesn't support salutation field for contact search now
		if (!salutation.isEmpty()){
			contactIdentifier = contactIdentifier + salutation;
		}
*/
		salutation = ""; // it is made at the same reason

		if ((firstName != null && !firstName.isEmpty()) && (salutation != null && !salutation.isEmpty())){
			contactIdentifier = contactIdentifier + ' ' + firstName;
		} else contactIdentifier = firstName;
		if ((lastName != null && !lastName.isEmpty()) && ((firstName != null && !firstName.isEmpty()) || (salutation != null && !salutation.isEmpty()))){
			contactIdentifier = contactIdentifier + ' ' + lastName;
		} else contactIdentifier = lastName;

		return contactIdentifier;
	}

	public void verify(FieldSet verifyThis) throws Exception {
		navToRecord();
		module.recordView.showMore();
		
		for(String controlName : verifyThis.keySet()) {
			if(verifyThis.get(controlName) != null) {
				if(module.recordView.getDetailField(controlName) == null) {
					continue;
				}
				VoodooUtils.voodoo.log.info("Verifying field " + controlName);
				String toVerify = verifyThis.get(controlName);
				if(controlName.startsWith("check")){
					module.recordView.getDetailField(controlName).assertChecked(Boolean.parseBoolean(toVerify));
				}else{
					module.recordView.getDetailField(controlName).assertElementContains(toVerify, true);
				}
			}
		}
	}
} // ContactRecord