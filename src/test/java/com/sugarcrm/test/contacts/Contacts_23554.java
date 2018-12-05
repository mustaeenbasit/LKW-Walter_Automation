package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23554 extends SugarTest {
	StandardSubpanel caseSub;

	public void setup() throws Exception {
		ContactRecord myContact = (ContactRecord) sugar().contacts.api.create();
		CaseRecord myCase = (CaseRecord) sugar().cases.api.create();
		sugar().login();
		// Add case record to contacts module
		myContact.navToRecord();
		caseSub = sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		caseSub.linkExistingRecord(myCase);
	}

	/**
	 * Verify that related case can be unlinked from contact detail view
	 * @throws Exception
	 */
	@Test
	public void Contacts_23554_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		caseSub.scrollIntoViewIfNeeded(false);
		// Verifying case record is completely unlink from contacts module
		caseSub.unlinkRecord(1);
		caseSub.expandSubpanel();
		Assert.assertTrue("The subpanel is not empty", caseSub.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
