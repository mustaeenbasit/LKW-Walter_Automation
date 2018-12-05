package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_19100 extends SugarTest {
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar.contacts.api.create();
		myLead = (LeadRecord)sugar.leads.api.create();
		sugar.login();		
	}

	/**
	 * Record is displayed in "Leads/Contact" sub-panel after selecting "Leads/Contact" record in relate field.
	 * @throws Exception
	 */
	@Test
	public void Calls_19100_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Log call with relatedTo field as Contact and Lead with their parent names
		sugar.navbar.selectMenuItem(sugar.calls, "create"+sugar.calls.moduleNameSingular);
		sugar.calls.createDrawer.getEditField("name").set(testName);
		VoodooControl parentTypeCtrl = sugar.calls.createDrawer.getEditField("relatedToParentType");
		VoodooControl parentNameCtrl = sugar.calls.createDrawer.getEditField("relatedToParentName");
		parentTypeCtrl.set(sugar.contacts.moduleNameSingular);
		parentNameCtrl.set(myContact.getRecordIdentifier());
		parentTypeCtrl.set(sugar.leads.moduleNameSingular);
		parentNameCtrl.set(myLead.getRecordIdentifier());

		// Click "Save" button
		sugar.calls.createDrawer.save();

		// Go to Call record view
		sugar.calls.listView.clickRecord(1);

		// Verify contact and Lead record are in guest list
		VoodooControl invitessCtrl = sugar.calls.recordView.getControl("invitees");
		invitessCtrl.assertContains(myContact.getRecordIdentifier(),true);
		invitessCtrl.assertContains(myLead.getRecordIdentifier(),true);

		// Verify call record on listview with related to field set as Lead only(multiple parentType will show only the latest one)
		sugar.calls.navToListView();
		sugar.calls.listView.verifyField(1, "name", testName);
		sugar.calls.listView.verifyField(1, "relatedToParentName", myLead.getRecordIdentifier());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}