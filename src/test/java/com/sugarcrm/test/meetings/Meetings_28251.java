package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_28251 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "Related To" Contact is removed in the Guest field for all children and parent record
	 * @throws Exception
	 */
	@Test
	public void Meetings_28251_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Daily recurrence meeting by selecting "Related To" has a contact record.
		FieldSet meetingData = testData.get(testName).get(0);
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeat_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(meetingData.get("repeat_occur_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(meetingData.get("repeat_count"));
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().contacts.getDefaultData().get("firstName"));

		// Save the Meeting
		sugar().meetings.createDrawer.save();

		// Asserting the record count on list view
		Assert.assertTrue("No of Meeting records not equal to 5" , sugar().meetings.listView.countRows() == 5);
		// TODO: VOOD-1428 - Need lib support for Guests list & "More Guests" link in preview pane for Calls/Meetings
		VoodooControl guestFieldCtrl = new VoodooControl("div", "css", "div[name='invitees'] .participants");

		// Contacts full name
		String contactName = sugar().contacts.getDefaultData().get("fullName");

		// Verify that contact is removed from the guest list preview of meeting records
		for(int i = 1; i < 6; i++) {
			// In List view, preview of the above repeat meetings.
			sugar().meetings.listView.previewRecord(i);

			// Verify that in the "Related to" field, see the Contact. 
			sugar().previewPane.getPreviewPaneField("relatedToParentName").assertEquals(contactName, true);

			//  Verify that in the Guest field, QAUser can see the Contact and QAUser.
			guestFieldCtrl.assertContains(sugar().users.getQAUser().get("userName"), true);
			guestFieldCtrl.assertContains(contactName, true);
		}

		// "Edit All Recurrences" for the above meeting.
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		sugar().meetings.recordView.getControl("editAllReocurrences").click();

		// Click on - for the Contact in the Guest field.
		sugar().meetings.recordView.getControl("removeInvitee02").click();
		sugar().meetings.recordView.save();

		// Verify that the Contact is removed from the Guest field.
		sugar().meetings.recordView.getControl("invitees").assertContains(contactName, false);
		sugar().meetings.navToListView();

		// Verify that contact is removed from the guest list preview of meeting records
		for(int i = 1; i < 6; i++) {
			// In List view, preview of the above repeat meetings.
			sugar().meetings.listView.previewRecord(i);

			// Verify that in the "Related to" field, still see the Contact. 
			sugar().previewPane.getPreviewPaneField("relatedToParentName").assertEquals(contactName, true);

			// Verify that in the Guest field, QAUser can see only QAUser.  
			guestFieldCtrl.assertContains(sugar().users.getQAUser().get("userName"), true);
			guestFieldCtrl.assertContains(contactName, false);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}