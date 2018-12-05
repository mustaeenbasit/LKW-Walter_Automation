package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22931 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		// Create a new account and contact record
		sugar().accounts.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Select_Verify that the contact can be selected by checking the check box in front of the contact records from the select contacts pop-up box.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22931_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto an Accounts record view 
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		
		// "Link Existing Record" from Contacts sub-panel. 
		contactsSubpanel.linkExistingRecord(myContact);
		
		// Selected contact is displayed on "CONTACTS" sub-panel.
		contactsSubpanel.getDetailField(1, "fullName").assertEquals(myContact.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}