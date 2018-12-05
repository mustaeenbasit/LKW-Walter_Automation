package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23341 extends SugarTest {
	CaseRecord myCase;
	FieldSet defaultContact;
	StandardSubpanel contactsSubpanel;

	public void setup() throws Exception {
		defaultContact = sugar().contacts.getDefaultData();

		sugar().login();
		myCase = (CaseRecord) sugar().cases.api.create();
		contactsSubpanel = sugar().cases.recordView.subpanels.get("Contacts");
	}

	/**
	 * Test Case 23341: Create Contact_Verify that contact for case is not created in "Contacts" sub-panel when using "Cancel" function.
	 */
	@Test
	public void Cases_23341_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCase.navToRecord();
		contactsSubpanel.addRecord();

		// Put default data and cancel creating a contact
		sugar().contacts.createDrawer.showMore();
		sugar().contacts.createDrawer.setFields(defaultContact);
		sugar().contacts.createDrawer.cancel();

		// but create an object with default contact data
		ContactRecord relContact = new ContactRecord(defaultContact);

		// Verify the contact related to the case hasn't been created
		// TODO: VOOD-735
		contactsSubpanel.waitForVisible();
		contactsSubpanel.assertContains(relContact.getRecordIdentifier(), false);

		// Verify a contact hasn't been created
		sugar().contacts.navToListView();
		sugar().contacts.listView.setSearchString(relContact.getRecordIdentifier());
		sugar().contacts.listView.getControl("emptyListViewMsg").waitForVisible();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
