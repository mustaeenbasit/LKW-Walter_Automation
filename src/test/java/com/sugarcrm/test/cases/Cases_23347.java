package com.sugarcrm.test.cases;


import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23347 extends SugarTest {
	ContactRecord relContact;
	StandardSubpanel subContacts;

	public void setup() throws Exception {
		CaseRecord myCase = (CaseRecord)sugar().cases.api.create();
		relContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();

		// Link a contact to a case
		myCase.navToRecord();
		subContacts = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		subContacts.scrollIntoView();
		subContacts.linkExistingRecord(relContact);
	}

	/**
	 * Remove Contact_Verify that contact can be unlinked from a case.
	 * @throws Exception
	 */
	@Test
	public void Cases_23347_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1424
		// Verify that contact is linked
		subContacts.getDetailField(1, "fullName").assertEquals(relContact.get("fullName"), true);

		// Unlink it and verify
		subContacts.unlinkRecord(1);
		Assert.assertTrue("The subpanel is not empty", subContacts.isEmpty());

		// Verify that contact isn't deleted
		relContact.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
