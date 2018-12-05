package com.sugarcrm.test.cases;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23342 extends SugarTest {
	ContactRecord relContact;

	public void setup() throws Exception {
		relContact = (ContactRecord) sugar().contacts.api.create();
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Create Contact_Verify that an existing contact can be selected for case in "Contacts" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Cases_23342_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Cases" tab in navigation bar
		sugar().navbar.navToModule(sugar().cases.moduleNamePlural);
		sugar().cases.listView.clickRecord(1);
		StandardSubpanel contactsSubpanel = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);

		contactsSubpanel.scrollIntoView();
		// Click "Link Existing Record" button in "Contacts" sub-panel.
		contactsSubpanel.clickLinkExisting();

		// Click on check box for any contact name in the Search and Select Contacts form.
		sugar().contacts.searchSelect.selectRecord(relContact);
		sugar().contacts.searchSelect.link();

		contactsSubpanel.expandSubpanel();
		// Verify contact selected for the case is displayed in "Contacts" sub-panel.
		contactsSubpanel.getDetailField(1, "fullName").assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}