package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27699 extends SugarTest {	

	public void setup() throws Exception {
		// Initialize test data
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().users.api.create();
		sugar().revLineItems.api.create();
		sugar().meetings.api.create();
		
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that clicking on + to add invitee, direct opens select2
	 * @throws Exception
	 */
	@Test
	public void Meetings_27699_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Initialize meeting data
		FieldSet meetingData = testData.get(testName).get(0);
		String leadName = sugar().leads.getDefaultData().get("lastName");
		String contactName = sugar().contacts.getDefaultData().get("lastName");
		String userName = sugar().users.getDefaultData().get("fullName");
		String rliName = sugar().revLineItems.getDefaultData().get("name");
		String meetingSubject = meetingData.get("name");

		// Edit an existing meeting record.  
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Add contact as Invitee
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(leadName);
		
		// Add lead as Invitee
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(contactName);
		
		// Add user as Invitee
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(userName);

		// Save meeting
		sugar().meetings.recordView.save();
		
		// Verify Guests list in meeting
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("fullName"), true);
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().users.getDefaultData().get("fullName"), true);
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);

		// Create new meeting
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(meetingSubject);

		// Add Guests, Related To a RLI record. 
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(meetingData.get("rli_singular"));
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(rliName);
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(leadName);
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(userName);

		// Save meeting
		sugar().meetings.createDrawer.save();

		// Verify that all info in the meeting record are correct especially in Guests field in Listview preview for first record
		sugar().meetings.listView.previewRecord(1);
		sugar().previewPane.showMore();
		sugar().previewPane.getPreviewPaneField("name").assertEquals(meetingSubject, true);
		sugar().previewPane.getPreviewPaneField("relatedToParentName").assertEquals(rliName, true);

		// TODO: VOOD-1428 - Need lib support for Guests list & "More Guests" link in preview pane for Calls/Meetings
		VoodooControl inviteeParticipants = new VoodooControl("span", "css", ".detail.fld_invitees .participants");
		inviteeParticipants.assertContains(leadName, true);
		inviteeParticipants.assertContains(userName, true);
		
		// Verify that all info in the meeting record are correct especially in Guests field in Listview preview for first record
		sugar().meetings.listView.previewRecord(2);
		sugar().previewPane.showMore();
		inviteeParticipants.assertContains(contactName, true);
		inviteeParticipants.assertContains(leadName, true);
		inviteeParticipants.assertContains(userName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}