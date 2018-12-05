package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27678 extends SugarTest {	
	UserRecord myUser;
	ContactRecord myCon;
	LeadRecord myLead;

	public void setup() throws Exception {
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		sugar().calls.api.create();
		sugar().meetings.api.create();
		myCon = (ContactRecord)sugar().contacts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		myUser = (UserRecord) sugar().users.api.create();
	}

	/**
	 * Verify that search name works correctly when add invitee in either call or meeting.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27678_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// For Meetings
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Add all possible users in Guests field of meetings module
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myCon);

		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myLead);

		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myUser);

		// Save meeting
		sugar().meetings.recordView.save();

		// Verify the new meeting is created with 3 invitees are added in the meeting
		VoodooControl inviteeForMeetings = sugar().meetings.recordView.getControl("invitees");
		inviteeForMeetings.assertContains(myCon.getRecordIdentifier(), true);
		inviteeForMeetings.assertContains(myLead.getRecordIdentifier(), true);
		inviteeForMeetings.assertContains(myUser.get("fullName"), true);

		// For Calls
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();

		// Add all possible users in Guests field of calls module
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(myCon);

		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(myLead);

		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(myUser);

		// Save call
		sugar().calls.recordView.save();

		// Verify the new call is created with 3 invitees are added in the call. 
		VoodooControl inviteeForCalls = sugar().calls.recordView.getControl("invitees");
		inviteeForCalls.assertContains(myCon.getRecordIdentifier(), true);
		inviteeForCalls.assertContains(myLead.getRecordIdentifier(), true);
		inviteeForCalls.assertContains(myUser.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}