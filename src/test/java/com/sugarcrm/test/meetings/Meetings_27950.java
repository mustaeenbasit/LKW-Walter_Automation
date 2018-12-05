package com.sugarcrm.test.meetings;

import static org.junit.Assert.assertTrue;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Meetings_27950 extends SugarTest {

	public void setup() throws Exception {
		// Initialize test data
		sugar().leads.api.create();
		sugar().contacts.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "Attending" is set in all children records when assignee is creator
	 * @throws Exception
	 */
	@Test
	public void Meetings_27950_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Initialize meeting data
		FieldSet meetingData = testData.get(testName).get(0);
		
		// Create meeting
		sugar().meetings.navToListView();		
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeat_type"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(meetingData.get("repeatOccurencesType"));
  		sugar().meetings.createDrawer.getEditField("repeatOccur").set(meetingData.get("repeatOccur"));

		// Add invitee contact
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().contacts.getDefaultData().get("lastName"));

		// Add invitee lead
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().leads.getDefaultData().get("lastName"));
		
		// Save Meeting.
		sugar().meetings.createDrawer.save();
		
		// Verify that 10 records exist on meetings list view
		assertTrue("Meetings count is not ten on list view while it should.", sugar().meetings.listView.countRows() == 10);
		
		// Verify meeting status for invitees
		FieldSet meetingStatus = new FieldSet();
		sugar().meetings.listView.clickRecord(1);
		for (int i = 1; i < 11; i++) {			
			
			// Verify that appears "Attending" for qauser and others have "No Reply" status
			meetingStatus.put("name", sugar().leads.getDefaultData().get("fullName"));
			meetingStatus.put("status", meetingData.get("meeting_status2"));
			sugar().meetings.recordView.verifyInvitee(1, meetingStatus);
			meetingStatus.clear();
			meetingStatus.put("name", sugar().users.getQAUser().get("userName"));
			meetingStatus.put("status", meetingData.get("meeting_status1"));
			sugar().meetings.recordView.verifyInvitee(2, meetingStatus);
			meetingStatus.clear();
			meetingStatus.put("name", sugar().contacts.getDefaultData().get("fullName"));
			meetingStatus.put("status", meetingData.get("meeting_status2"));
			sugar().meetings.recordView.verifyInvitee(3, meetingStatus);
			meetingStatus.clear();
			
			// Click to next record
			if (i < 10) {
				sugar().meetings.recordView.gotoNextRecord();
			}
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}