package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27585 extends SugarTest {	
	
	public void setup() throws Exception {
		// Create 1 meeting record
		sugar().meetings.api.create();
		
		// Create 10 Contacts Record
		FieldSet myContacts = new FieldSet();
		for (int i = 0; i < 10; i++) {
			myContacts.put("lastName", testName + i);
			sugar().contacts.api.create(myContacts);
		}
		sugar().login();
	}

	/**
	 * Verify that a yearly recurring meeting is saved when select in Repeat Occurrences 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27585_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Edit meeting to a Yearly recurring, set Repeat Occurrences to 2
		FieldSet meetingData = testData.get(testName).get(0);
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		
		// Add 10 Contacts as invitees
		for (int i = 0 ; i < 10; i++) {
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(testName + i);
		}
		sugar().meetings.recordView.getEditField("repeatType").set(meetingData.get("repeat_type"));
		sugar().meetings.recordView.getEditField("repeatOccur").set(meetingData.get("repeat_count"));

		// Save meeting
		sugar().meetings.recordView.save();
		sugar().meetings.navToListView();
		
		// Verify start date of 2 meeting records
		sugar().meetings.listView.sortBy("headerDatestart", true);
		sugar().meetings.listView.getDetailField(1, "date_start_date").assertContains(meetingData.get("date_meeting1"), true);
		sugar().meetings.listView.getDetailField(2, "date_start_date").assertContains(meetingData.get("date_meeting2"), true);
		
		// TODO: VOOD-1354 - Need Lib Support for "More Guest..." link in Meetings/Calls sidecar module recordView.
		VoodooControl showMoreLink = new VoodooControl("button", "css", "[data-action='show-more']");
		
		// Verify 10 Contact invitees are appearing in each recurring meeting
		for (int i = 1; i < 3; i++) {
			sugar().meetings.listView.clickRecord(i);
			VoodooControl inviteeCtrl = sugar().meetings.recordView.getControl("invitees");
			showMoreLink.click();
			VoodooUtils.waitForReady();
			showMoreLink.click();
			
			// Verify name of contact in invitees list
			for (int j = 0; j < 10; j++) {
				inviteeCtrl.assertContains(testName+j, true);
			}
			sugar().meetings.navToListView();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}