package com.sugarcrm.test.meetings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27691 extends SugarTest {	
	FieldSet meetingData = new FieldSet();
	UserRecord myUser;
	ContactRecord myContact;
	LeadRecord myLead;
	
	public void setup() throws Exception {
		// Initialize test data
		myContact = (ContactRecord)sugar().contacts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		myUser = (UserRecord) sugar().users.api.create();
		meetingData = testData.get(testName).get(0);
		
		// Login with QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that edit one child meeting and it routes to /edit/all-recurrences
	 * @throws Exception
	 */
	@Test
	public void Meetings_27691_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		String meetingDate = sdf.format(calendar.getTime());
		calendar.setTime(sdf.parse(meetingDate));
		calendar.add(Calendar.DATE, 1); 
		
		// Create a new meeting
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		FieldSet repeatData = new FieldSet();
		repeatData.put("repeatType", meetingData.get("repeatType"));

		// Add contact as Invitee
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myContact);
		
		// Add lead as Invitee
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myLead);
		
		// Add user as Invitee
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myUser);
		
		// Fill other fields
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.getEditField("date_start_date").set(meetingDate);
		sugar().meetings.createDrawer.repeatOccurrences(repeatData);
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(meetingData.get("repeatOccurencesType"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(meetingData.get("repeatOccur"));

		// Save meeting
		sugar().meetings.createDrawer.save();
		
		// Verify meetings count on list view
		assertTrue("Meetings count is not five on list view", sugar().meetings.listView.countRows() == 5);
		
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
		
		// Click on Save
		sugar().meetings.recordView.save();

		// After saving, notice the URL doesn't have 'edit/all-recurrences' any more.
		String currentUrl2 = VoodooUtils.getUrl();
		assertFalse("URL contains string edit/all-recurrence when it should not", currentUrl2.contains(meetingData.get("urlString")));
		
		// Navigate to meeting list view and sort by start date ascending
		sugar().meetings.navToListView();
		sugar().meetings.listView.sortBy("headerDatestart", true);
		
		// Check other meetings.
		for (int i = 0; i < 5; i++) {
			sugar().meetings.listView.clickRecord(i + 1);

			// Verify that updated changes are reflecting in all meeting records
			sugar().meetings.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("fullName"), true);
			sugar().meetings.recordView.getControl("invitees").assertContains(sugar().users.getDefaultData().get("fullName"), true);
			sugar().meetings.recordView.getControl("invitees").assertContains(sugar().contacts.getDefaultData().get("fullName"), false);
			sugar().meetings.navToListView();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}