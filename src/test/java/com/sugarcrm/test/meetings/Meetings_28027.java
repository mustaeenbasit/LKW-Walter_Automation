package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_28027 extends SugarTest {
	LeadRecord myLeadRecord;
	ContactRecord myContactRecord;

	public void setup() throws Exception {
		myLeadRecord = (LeadRecord) sugar().leads.api.create();
		myContactRecord = (ContactRecord) sugar().contacts.api.create();
		
		// Login as Admin
		sugar().login();

		// Create a Meeting record via UI
		sugar().meetings.create();
	}

	/**
	 * Verify that child record also sync up after the Contact/Lead is removed from all recurrences
	 * @throws Exception
	 */
	@Test
	public void Meetings_28027_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		FieldSet meetingData = testData.get(testName).get(0);

		// Edit a Meeting to a repeat Meeting record. Make sure in "Related To" field doesn't have Contact or Lead.  
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("repeatType").set(meetingData.get("repeat_type"));
		sugar().meetings.recordView.getEditField("repeatOccur").set(meetingData.get("repeat_count"));

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

		// Save the Meeting
		sugar().meetings.recordView.save();

		// Verify that the repeat Meeting is created with one Contact or Lead as Guest.
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		VoodooControl inviteeRecord = sugar().meetings.recordView.getControl("invitees");
		for(int i = 1; i <= Integer.parseInt(meetingData.get("repeat_count")); i++) {
			inviteeRecord.assertContains(myLeadRecord.getRecordIdentifier(), true);
			inviteeRecord.assertContains(myContactRecord.getRecordIdentifier(), true);

			// Click to next record
			if(i < Integer.parseInt(meetingData.get("repeat_count")))
				sugar().meetings.recordView.gotoNextRecord();
		}

		// "Edit All Recurrences" for the Meeting.
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		sugar().meetings.recordView.getControl("editAllReocurrences").click();
		VoodooUtils.waitForReady();

		// Remove the Contact or the Lead from Guests field.
		sugar().meetings.recordView.getControl("removeInvitee02").click();
		VoodooUtils.waitForReady();
		sugar().meetings.recordView.getControl("removeInvitee02").click();
		VoodooUtils.waitForReady();

		// Save the Meeting record
		sugar().meetings.recordView.save();

		// Go to List view and preview the first Meeting.
		sugar().meetings.navToListView();
		sugar().meetings.listView.previewRecord(1);
		VoodooUtils.waitForReady(20000);

		// Verify that the Contact or Lead isn't appearing in the Guest field.
		for(int i = 1; i <= Integer.parseInt(meetingData.get("repeat_count")); i++) {
			new VoodooControl("div", "css", ".participants").assertContains(myLeadRecord.getRecordIdentifier(), false);
			new VoodooControl("div", "css", ".participants").assertContains(myContactRecord.getRecordIdentifier(), false);
			sugar().previewPane.gotoNextRecord();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}