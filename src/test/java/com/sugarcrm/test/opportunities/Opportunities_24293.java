package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24293 extends SugarTest {
	ContactRecord myContact;
	OpportunityRecord myOpp;
	StandardSubpanel contactsSubpanel;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();

		// Navigate to the opportunity record view and add a related contact from contacts subpanel
		myOpp.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		contactsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(myContact);
	}

	/**
	 * Test Case 24293: Unlink Contact_Verify that contact can be unlinked from an opportunity from contacts subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24293_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Unlink the contact from contacts subpanel
		contactsSubpanel.unlinkRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		contactsSubpanel.expandSubpanel();

		// Verify that related contact is correctly unlinked from the contacts subpanel
		contactsSubpanel.assertContains(myContact.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}