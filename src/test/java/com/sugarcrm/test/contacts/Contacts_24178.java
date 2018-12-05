package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */
public class Contacts_24178 extends SugarTest {
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Unlink lead_Verify that a related lead can be unlinked from leads subpanel of a contact record view
	 * @throws Exception
	 */
	@Test
	public void Contacts_24178_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link existing lead to Contact record
		myContact.navToRecord();
		StandardSubpanel leadsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.scrollIntoViewIfNeeded(false);
		leadsSubpanel.linkExistingRecord(myLead);

		// Unlink the related lead by clicking Unlink action in the leads subpanel
		leadsSubpanel.unlinkRecord(1);

		// Verify that unlinked lead is not available in the leads subpanel
		leadsSubpanel.expandSubpanel();
		Assert.assertTrue("The subpanel is not empty", leadsSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
