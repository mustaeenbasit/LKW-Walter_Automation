package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27284 extends SugarTest {
	UserRecord myUser;
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		myUser = (UserRecord) sugar().users.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
	}

	/**
	 * Verify that add or remove invitees in meeting scheduler is working properly.
	 * @throws Exception
	 */
	@Test
	public void Meetings_27284_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a meeting record
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));

		// Add contact in guest list
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().contacts.getDefaultData().get("lastName"));

		// Add Lead in guest list
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().leads.getDefaultData().get("lastName"));

		// Add user in guest list
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myUser);

		// save meeting record
		sugar().meetings.createDrawer.save();

		// Open the newly created meeting record
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.showMore();

		// Verify, Including QAUser, 4 meeting participants in the meeting record view.
		VoodooControl inviteeCtrl = sugar().meetings.recordView.getControl("invitees");
		inviteeCtrl.assertContains(myContact.getRecordIdentifier(), true);
		inviteeCtrl.assertContains(myLead.getRecordIdentifier(),true);
		inviteeCtrl.assertContains(myUser.getRecordIdentifier(),true);
		inviteeCtrl.assertContains(sugar().users.getQAUser().get("userName"),true);

		// Edit the meeting record
		sugar().meetings.recordView.edit();

		// Remove the user, the contact and the lead from Scheduling div.
		FieldSet meetingName = new FieldSet();
		meetingName.put("name", sugar().meetings.getDefaultData().get("name"));
		MeetingRecord myMeeting = new MeetingRecord(meetingName);

		// Remove other User
		myMeeting.removeInvitee(myUser);

		// Confirm that other User has been removed
		inviteeCtrl.assertContains(myUser.getRecordIdentifier(),false);

		// Remove Lead
		myMeeting.removeInvitee(myLead);

		// Confirm that Lead has been removed
		inviteeCtrl.assertContains(myLead.getRecordIdentifier(),false);

		// Remove Contact
		myMeeting.removeInvitee(myContact);

		// Confirm that Contact has been removed
		inviteeCtrl.assertContains(myContact.getRecordIdentifier(), false);

		// Verify, Including QAUser, only 1 participant in the meeting record view.
		inviteeCtrl.assertContains(myContact.getRecordIdentifier(), false);
		inviteeCtrl.assertContains(myLead.getRecordIdentifier(),false);
		inviteeCtrl.assertContains(myUser.getRecordIdentifier(),false);
		inviteeCtrl.assertContains(sugar().users.getQAUser().get("userName"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}