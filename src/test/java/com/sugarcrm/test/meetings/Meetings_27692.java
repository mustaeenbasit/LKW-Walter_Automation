package com.sugarcrm.test.meetings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27692 extends SugarTest {	
	
	public void setup() throws Exception {
		// Initialize test data
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().opportunities.api.create();
		sugar().users.api.create();
		
		// Login with QAUser
		sugar().login(sugar().users.getQAUser());
		
	}

	/**
	 * Verify that cancel of editing one child meeting and it removes /edit/all-recurrences
	 * @throws Exception
	 */
	@Test
	public void Meetings_27692_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Initialize meeting data
		FieldSet meetingData = testData.get(testName).get(0);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		String meetingDate = sdf.format(calendar.getTime());
		
		// Create a new meeting
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		FieldSet repeatData = new FieldSet();
		repeatData.put("repeatType", meetingData.get("repeatType"));
		
		// Add contact as Invitee
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(sugar().contacts.getDefaultData().get("lastName"));
		
		// Add lead as Invitee
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(sugar().leads.getDefaultData().get("lastName"));
		
		// Add user as Invitee
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(sugar().users.getDefaultData().get("lastName"));
		
		// Fill other fields
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.getEditField("date_start_date").set(meetingDate);
		sugar().meetings.createDrawer.repeatOccurrences(repeatData);
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(meetingData.get("repeatEndType"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(meetingData.get("repeatOccur"));

		// Select an Opportunity record in "Related To" field. 
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().opportunities.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().opportunities.getDefaultData().get("name"));

		// Save meeting
		sugar().meetings.createDrawer.save();

		// Verify meetings count on list view
		assertTrue("Meetings count is not three on list view while it should.", sugar().meetings.listView.countRows() == 3);
				
		// Click on child record
		sugar().meetings.listView.sortBy("headerDatestart", true);
		sugar().meetings.listView.clickRecord(2);


		// Click on action drop down, select "Edit All Recurrences".
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		sugar().meetings.recordView.getControl("editAllReocurrences").click();
		VoodooUtils.waitForReady();

		// Verify that click on "Edit All Recurrences" routes to the parent meeting edit view and in URL, notice that "edit/all-recurrences".
		sugar().meetings.recordView.getEditField("date_start_date").assertContains(meetingDate, true);

		// Verify in URL that "edit/all-recurrences" is exist.
		String currentUrl1 = VoodooUtils.getUrl();
		assertTrue("URL does not contain string edit/all-recurrence when it should", currentUrl1.contains(meetingData.get("urlString")));

		// In the parent meeting record, change one field, such as remove one invitee. i.e. remove contact. 
		sugar().meetings.recordView.getControl("removeInvitee04").click();
		VoodooUtils.waitForReady();
		
		// Click on Cancel
		sugar().meetings.recordView.cancel();

		// After the canceling success, notice the URL doesn't have "edit/all-recurrences" any more.
		String currentUrl2 = VoodooUtils.getUrl();
		assertFalse("URL contains string edit/all-recurrence when it should not", currentUrl2.contains(meetingData.get("urlString")));
		
		// Navigate to meeting list view and sort by start date ascending
		sugar().meetings.navToListView();
		sugar().meetings.listView.sortBy("headerDatestart", true);
		VoodooControl meetingInvitee = sugar().meetings.recordView.getControl("invitees");
				
		// Check other meetings.
		for (int i = 0; i < 3; i++) {
			sugar().meetings.listView.clickRecord(i + 1);

			// Verify that updated changes are reflecting in all meeting records
			meetingInvitee.assertContains(sugar().leads.getDefaultData().get("fullName"), true);
			meetingInvitee.assertContains(sugar().users.getDefaultData().get("fullName"), true);
			meetingInvitee.assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
			sugar().meetings.navToListView();
			}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}