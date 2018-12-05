package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23509 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().login();

		// Create a new contact record
		myContact = (ContactRecord) sugar().contacts.api.create();
	}

	/**
	 * Edit contact_Verify that a contact can be edited
	 * @throws Exception
	 */
	@Test
	public void Contacts_23509_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();

		newData.put("department", "QA");
		newData.put("phoneMobile", "+375 29 1111111");
		newData.put("phoneWork", "+375 17 2222222");
		newData.put("phoneFax", "+375 17 3333333");
		newData.put("primaryAddressStreet", "Kalvarijskaya");
		newData.put("primaryAddressState", "Minskas");
		newData.put("primaryAddressPostalCode", "232323");
		newData.put("primaryAddressCity", "Minsk");
		newData.put("primaryAddressCountry", "Belarus");
		newData.put("description", "test contact description");

		//Edit contact
		myContact.edit(newData);

		//Verify that contact has been edited
		myContact.verify(newData);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
