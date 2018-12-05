package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_26515 extends SugarTest {
	LeadRecord myLead;
	ContactRecord myContact;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().targetlists.api.create();
		sugar().login();
	}

	/**
	 * Link existing Contacts or Leads into Target List
	 * @throws Exception
	 */
	@Test
	public void Leads_26515_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);

		// Link a Lead record to the targetlist
		StandardSubpanel leadsSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubPanel.linkExistingRecord(myLead);

		// Verify the Lead record is appearing in Leads sub panel
		FieldSet fsLeadName = new FieldSet();
		fsLeadName.put("fullName", myLead.getRecordIdentifier());

		// TODO: VOOD-1424 - Once resolved it should use verify method
		//leadsSubPanel.verify(1, fsLeadName, true);
		leadsSubPanel.getDetailField(1, "fullName").assertContains(myLead.getRecordIdentifier(), true);

		StandardSubpanel contactsSubPanel = (StandardSubpanel) sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		// Link a Contact record to the targetlist
		contactsSubPanel.linkExistingRecord(myContact);
		FieldSet fsContactRecord = new FieldSet();
		fsContactRecord.put("fullName", myContact.getRecordIdentifier());

		// Verify the Contact record is appearing in Contacts sub panel
		// TODO: VOOD-1424 - Once resolved it should use verify method
		//contactsSubPanel.verify(1, fsContactRecord, true);
		contactsSubPanel.getDetailField(1, "fullName").assertContains(myContact.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}