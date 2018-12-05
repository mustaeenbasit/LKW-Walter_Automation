package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.test.SugarTest;

public class Cases_23346 extends SugarTest {
	CaseRecord myCase;
	ContactRecord relContact;
	StandardSubpanel subContacts;

	public void setup() throws Exception {
		relContact = (ContactRecord)sugar().contacts.api.create();
		myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();

		// Link a contact to a case
		myCase.navToRecord();
		subContacts = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		subContacts.scrollIntoView();
		subContacts.linkExistingRecord(relContact);
	}

	/**
	 * Edit Contact_Verify that the detail view is displayed after clicking the contact name in contacts sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Cases_23346_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1100
		FieldSet contactFullname = new FieldSet();
		contactFullname.put("fullName", relContact.get("fullName"));

		// Verify that contact is linked and open its detail
		subContacts.verify(1, contactFullname, true);
		subContacts.scrollIntoView();
		subContacts.clickRecord(1);

		// Verify RecordView of the contact is rendered
		sugar().contacts.recordView.getDetailField("fullName").waitForVisible();
		sugar().contacts.recordView.getDetailField("fullName").assertEquals(contactFullname.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
