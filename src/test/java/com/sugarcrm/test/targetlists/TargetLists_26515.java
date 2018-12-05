package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_26515 extends SugarTest {
	LeadRecord myLead;
	ContactRecord myContact;

	public void setup() throws Exception {
		myLead = (LeadRecord) sugar.leads.api.create();
		myContact = (ContactRecord) sugar.contacts.api.create();
		sugar.targetlists.api.create();
		sugar.login();
	}

	/**
	 * Link existing Contacts or Leads into Target List
	 * 
	 * @throws Exception
	 */
	@Test
	public void TargetLists_26515_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);

		// Link a Lead record to the targetlist
		StandardSubpanel leadsSubPanel = sugar.targetlists.recordView.subpanels
				.get(sugar.leads.moduleNamePlural);
		leadsSubPanel.linkExistingRecord(myLead);

		// Verify the Lead record is appearing in Leads sub panel
		// TODO: VOOD-1424
		leadsSubPanel.getDetailField(1, "fullName").assertEquals(
				sugar.leads.getDefaultData().get("fullName"), true);

		// Link a Contact record to the targetlist
		StandardSubpanel contactsSubPanel = (StandardSubpanel) sugar.targetlists.recordView.subpanels
				.get(sugar.contacts.moduleNamePlural);
		contactsSubPanel.linkExistingRecord(myContact);

		// Verify the Contact record is appearing in Contacts sub panel
		contactsSubPanel.getDetailField(1, "fullName").assertEquals(
				sugar.contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}