package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27679 extends SugarTest {	
	DataSource emailDS;
	MeetingRecord myMeeting;
	UserRecord myUser;
	CallRecord myCall;
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		sugar().login();

		emailDS = testData.get(testName);
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		myUser = (UserRecord) sugar().users.api.create();
		myMeeting = (MeetingRecord) sugar().meetings.api.create();
		myCall = (CallRecord) sugar().calls.api.create();
	}

	/**
	 * Verify that search email works correctly when add invitee in either call or meeting.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27679_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-896
		VoodooControl emailCtrl = new VoodooControl("input", "css", ".fld_email.edit input");
		VoodooControl userEmailCtrl = new VoodooControl("input", "css", "input#Users0emailAddress0");

		// Edit email for contact -> abc@sbc.com
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		emailCtrl.set(emailDS.get(0).get("emailAddress"));
		sugar().contacts.recordView.save();

		// Edit email for lead -> lead22@cnn.net
		myLead.navToRecord();
		sugar().leads.recordView.edit();
		emailCtrl.set(emailDS.get(1).get("emailAddress"));
		sugar().leads.recordView.save();

		// Edit email for user -> a.tom@gmail.com
		myUser.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		userEmailCtrl.set(emailDS.get(2).get("emailAddress"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		sugar().logout();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// For Meetings
		myMeeting.navToRecord();
		sugar().meetings.recordView.edit();

		// In Guests field in Meeting, click on + to add one row and click on Select to bring a search(make sure not click on "Search for more" link.
		for(int i=0;i<emailDS.size();i++){
			sugar().meetings.recordView.clickAddInvitee();
			// Add contact, leads and user invitee.(search by email)
			sugar().meetings.recordView.selectInvitee(emailDS.get(i).get("emailAddress"));
		}

		// Save meeting
		sugar().meetings.recordView.save();

		// Verify the new meeting is created with 3 invitees are added in the meeting. 
		VoodooControl inviteeForMeetings = sugar().meetings.recordView.getControl("invitees");
		inviteeForMeetings.assertContains(myContact.getRecordIdentifier(), true);
		inviteeForMeetings.assertContains(myLead.getRecordIdentifier(), true);
		inviteeForMeetings.assertContains(myUser.get("fullName"), true);

		// For Calls
		myCall.navToRecord();
		sugar().calls.recordView.edit();

		// In Guests field in Calls, click on + to add one row and click on Select to bring a search(make sure not click on "Search for more" link.
		for(int i=0;i<emailDS.size();i++){
			sugar().calls.recordView.clickAddInvitee();
			// Add contact, leads and user invitee.(search by email)
			sugar().calls.recordView.selectInvitee(emailDS.get(i).get("emailAddress"));
		}

		// Save call
		sugar().calls.recordView.save();

		// Verify the new call is created with 3 invitees are added in the call. 
		VoodooControl inviteeForCalls = sugar().calls.recordView.getControl("invitees");
		inviteeForCalls.assertContains(myContact.getRecordIdentifier(), true);
		inviteeForCalls.assertContains(myLead.getRecordIdentifier(), true);
		inviteeForCalls.assertContains(myUser.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}