package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_19117 extends SugarTest {
	DataSource ds;
	LeadRecord lead1, lead2;
	ContactRecord myContact;

	public void setup() throws Exception {
		ds = testData.get(testName);

		// 1 Call, 1 Contact, 2 Leads record
		sugar.calls.api.create();
		myContact = (ContactRecord)sugar.contacts.api.create();
		lead1 = (LeadRecord)sugar.leads.api.create(ds.get(0));
		lead2 = (LeadRecord)sugar.leads.api.create(ds.get(1));

		sugar.login();	
	}

	/**
	 * Add Related person to invitees
	 * @throws Exception
	 */
	@Test
	public void Calls_19117_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// Add Contact and Lead as invitee
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(myContact);
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(lead1);
		sugar.calls.recordView.save();

		// Verify contact and lead records are in guest list
		sugar.calls.recordView.getControl("invitees").assertContains(sugar.contacts.getDefaultData().get("fullName"), true);
		sugar.calls.recordView.getControl("invitees").assertContains(lead1.getRecordIdentifier(),true);

		// Add another Lead as invitee
		sugar.calls.recordView.edit();
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(lead2);
		sugar.calls.recordView.save();

		// Verify old contact, lead and this related lead records are in guest list
		sugar.calls.recordView.getControl("invitees").assertContains(sugar.contacts.getDefaultData().get("fullName"), true);
		sugar.calls.recordView.getControl("invitees").assertContains(lead1.getRecordIdentifier(),true);
		sugar.calls.recordView.getControl("invitees").assertContains(lead2.getRecordIdentifier(),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}