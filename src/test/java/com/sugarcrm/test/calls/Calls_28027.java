package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_28027 extends SugarTest {
	LeadRecord myLeadRecord;
	ContactRecord myContactRecord;

	public void setup() throws Exception {
		myLeadRecord = (LeadRecord) sugar.leads.api.create();
		myContactRecord = (ContactRecord) sugar.contacts.api.create();

		// Login as QAUser
		sugar.login(sugar.users.getQAUser());	

		sugar.calls.create();
	}

	/**
	 * Verify that child record also sync up after the Contact/Lead is removed from all recurrences
	 * @throws Exception
	 */
	@Test
	public void Calls_28027_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		FieldSet callData = testData.get(testName).get(0);

		// Edit a call to a repeat Call record. Make sure in "Related To" field doesn't have Contact or Lead.  
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("repeatType").set(callData.get("repeat_type"));
		sugar.calls.recordView.getEditField("repeatOccur").set(callData.get("repeat_count"));

		// Add one Contact or Lead in Guests field.
		// TODO: VOOD-1345
		VoodooControl addInviteeCtrl = new VoodooControl("button", "css", ".fld_invitees.edit [data-action='addRow']");
		VoodooSelect inviteeSearchCtrl = new VoodooSelect("input", "css", "#select2-drop div input");

		// Add invitee lead
		addInviteeCtrl.click();
		inviteeSearchCtrl.set(myLeadRecord.getRecordIdentifier());

		// Add invitee contact
		addInviteeCtrl.click();
		inviteeSearchCtrl.set(myContactRecord.getRecordIdentifier());

		// Save the Call
		sugar.calls.recordView.save();

		// Verify that the repeat call is created with one Contact or Lead as Guest.
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		VoodooControl inviteeRecord = sugar.calls.recordView.getControl("invitees");
		for(int i = 1; i <= Integer.parseInt(callData.get("repeat_count")); i++) {
			inviteeRecord.assertContains(myLeadRecord.getRecordIdentifier(), true);
			inviteeRecord.assertContains(myContactRecord.getRecordIdentifier(), true);

			// Click to next record
			if(i < Integer.parseInt(callData.get("repeat_count")))
				sugar.calls.recordView.gotoNextRecord();
		}

		// "Edit All Recurrences" for the Call.
		// TODO: VOOD-1257
		sugar.calls.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "[name='edit_recurrence_button']").click();
		sugar.alerts.waitForLoadingExpiration();

		// Remove the Contact or the Lead from Guests field.
		new VoodooControl("button", "xpath", "//div[@class='row participant'][contains(.,'"+myLeadRecord.getRecordIdentifier()+"')]//button[@class='btn'][@data-action='removeRow']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "xpath", "//div[@class='row participant'][contains(.,'"+myContactRecord.getRecordIdentifier()+"')]//button[@class='btn'][@data-action='removeRow']").click();
		VoodooUtils.waitForReady();

		// Save the Calls record
		sugar.calls.recordView.save();

		// Go to List view and preview the first Calls.
		sugar.calls.navToListView();
		sugar.calls.listView.previewRecord(1);

		VoodooControl previewPaneInviteeRecord = new VoodooControl("div", "css", ".fld_invitees.detail");
		// Verify that the Contact or Lead isn't appearing in the Guest field.
		for(int i = 1; i <= Integer.parseInt(callData.get("repeat_count")); i++) {
			previewPaneInviteeRecord.assertContains(myLeadRecord.getRecordIdentifier(), false);
			previewPaneInviteeRecord.assertContains(myContactRecord.getRecordIdentifier(), false);
			sugar.previewPane.gotoNextRecord();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}