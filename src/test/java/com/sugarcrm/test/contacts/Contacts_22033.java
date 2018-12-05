package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_22033 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		myContact = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * User try to go to another page without saving changes
	 * @throws Exception
	 */
	@Test
	public void Contacts_22033_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit contact record
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("department").set(myContact.getRecordIdentifier());

		// Click on profile - to discard changes
		sugar().navbar.navToProfile();

		// Verify that a confirm windows show message
		sugar().alerts.getWarning().assertContains(sugar().alerts.getWarning().getText(), true);
		sugar().alerts.getAlert().confirmAlert();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
