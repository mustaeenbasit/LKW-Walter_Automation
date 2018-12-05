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

public class Opportunities_24289 extends SugarTest {
	ContactRecord myContact;
	OpportunityRecord myOpp;
	StandardSubpanel contactsSub;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
	}

	/**
	 * Test Case 24289: Verify that an existing contacts can be linked with the opportunity from contacts subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24289_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity record view and link existing contact from the contacts subpanel
		myOpp.navToRecord();
		contactsSub = sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSub.linkExistingRecord(myContact);
		
		// Verify that linked contact is available in the contacts subpanel
		contactsSub.assertContains(myContact.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}