package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_23510 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		// Create a new contact record with default data
		myContact = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Test Case 23510: Edit contact_Verify that editing a contact from detail view can be canceled.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23510_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		//Edit contact using custom data and click Cancel to abort saving it
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.setFields(ds.get(0));
		sugar().contacts.recordView.cancel();

		//Verify that contact has not been edited
		myContact.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

		public void cleanup() throws Exception {}
}
