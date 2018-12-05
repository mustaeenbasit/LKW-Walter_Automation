package com.sugarcrm.test.meetings;

import static org.junit.Assert.assertTrue;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;	
import com.sugarcrm.test.SugarTest;

public class Meetings_29314 extends SugarTest {
	VoodooControl userInvitee, contactInvitee, rowInvitees;
	FieldSet meetingDefaultData = new FieldSet();
	FieldSet contactDefaultData = new FieldSet();
	FieldSet leadDefaultData = new FieldSet();
	String qauserLastName = "";

	public void setup() throws Exception {
		// Create a quote, contact and lead record
		sugar().quotes.api.create();
		sugar().contacts.api.create();
		sugar().leads.api.create();

		meetingDefaultData = sugar().meetings.getDefaultData();
		contactDefaultData = sugar().contacts.getDefaultData();
		leadDefaultData = sugar().leads.getDefaultData();
		qauserLastName = sugar().users.qaUser.get("lastName");

		// Log-In as a qaUser
		sugar().login(sugar().users.qaUser);

		// TODO: VOOD-1223 - Need library support to get records in Guests list in Meetings record view
		rowInvitees = new VoodooControl("div", "css", ".row.participant");
		userInvitee = new VoodooControl("div", "css", ".row.participant[data-module='Users'] .name");
		contactInvitee = new VoodooControl("div", "css", ".row.participant[data-module='Contacts'] .name");
	}

	/**
	 * Verify that "Edit All Recurrences" replace all record in the series of meeting/call
	 * @throws Exception
	 */
	@Test
	public void Meetings_29314_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet meetingsInfo = testData.get(testName).get(0);
		String repeatType = meetingsInfo.get("repeatType");
		String repeatOccur = meetingsInfo.get("repeatOccur");

		// Navigate to the meetings module
		sugar().navbar.navToModule(sugar().meetings.moduleNamePlural);

		/**
		 * qaUser creates a repeat meeting or call, e.g. 3 days, Related To a Quote record,
		 * has a string in Description, add one Contact record as invitee, and etc. Save it.
		 */
		sugar().meetings.listView.create();
		LocalDate date = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");
		VoodooControl nameCtrl = sugar().meetings.createDrawer.getEditField("name");
		nameCtrl.set(meetingDefaultData.get("name"));
		sugar().meetings.createDrawer.getEditField("date_start_time").set(meetingDefaultData.get("date_start_time"));
		sugar().meetings.createDrawer.getEditField("repeatType").set(repeatType);
		nameCtrl.click();
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(meetingsInfo.get("repeatEndType"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(repeatOccur);
		sugar().meetings.createDrawer.getEditField("description").set(meetingDefaultData.get("description"));
		VoodooSelect relatedToParentType = (VoodooSelect)sugar().meetings.createDrawer.getEditField("relatedToParentType");
		relatedToParentType.click();
		String quoteModuleNameSingular = sugar().quotes.moduleNameSingular;
		relatedToParentType.selectWidget.getControl("searchBox").set(quoteModuleNameSingular);
		
		// TODO: VOOD-629 - Add support for accessing and manipulating individual components of a VoodooSelect
		new VoodooControl("li", "css", ".select2-drop-active .select2-results li:not(.select2-highlighted)").click();
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(quoteModuleNameSingular);
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(contactDefaultData.get("lastName"));
		sugar().meetings.createDrawer.save();

		int totalRecurringMeetings = Integer.parseInt(repeatOccur);
		sugar().meetings.listView.clickRecord(1);

		// Assert that 2 guests are displayed in the parent meeting guest list
		userInvitee.assertEquals(qauserLastName, true);
		contactInvitee.assertEquals(contactDefaultData.get("fullName"), true);
		int inviteesCountParentMeeting = Integer.parseInt(meetingsInfo.get("guestsCountParentMeeting"));
		assertTrue(rowInvitees.countWithClass() == inviteesCountParentMeeting);

		VoodooControl nameDetailField = sugar().meetings.recordView.getDetailField("name");

		// Verify that In this series of 3 meetings, the fields have same value as what we described above in the Parent Meeting
		for(int i = 1; i < totalRecurringMeetings; i++) {
			sugar().meetings.recordView.gotoNextRecord();
			nameDetailField.assertEquals(meetingDefaultData.get("name"), true);
			sugar().meetings.recordView.getDetailField("repeatType").assertEquals(repeatType, true);
			sugar().meetings.recordView.getDetailField("repeatOccur").assertEquals(repeatOccur, true);
			assertMeetingFieldsAndGuests(inviteesCountParentMeeting);
		}

		/**
		 * Edit the middle record, e.g. add extra string in the subject, change a different description, 
		 * change the "Related To" to Lead, therefore a Lead, a Contact and a User are in the Guests field. 
		 * Even change meeting start time to one hour later, change status to Held and Save it.
		 */
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(2);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("date_start_time").set(meetingsInfo.get("newStartTime"));
		sugar().meetings.recordView.getEditField("description").set(testName);
		sugar().meetings.recordView.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		sugar().meetings.recordView.getEditField("relatedToParentName").set(leadDefaultData.get("lastName"));
		sugar().meetings.recordView.getEditField("status").set(meetingsInfo.get("meetingStatus"));
		sugar().meetings.recordView.save();

		// Assert that now 3 guests are displayed in the guests list
		assertTrue(rowInvitees.countWithClass() == 3);
		userInvitee.assertEquals(qauserLastName, true);
		contactInvitee.assertEquals(contactDefaultData.get("fullName"), true);
		VoodooControl leadInvitee = new VoodooControl("div", "css", ".row.participant[data-module='Leads'] .name");
		leadInvitee.assertEquals(leadDefaultData.get("fullName"), true);

		// Verify that now only this record has different meeting content with other meetings in the series.
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		VoodooControl meetingStatusDetailField = sugar().meetings.recordView.getDetailField("status");

		for(int i = 1; i < totalRecurringMeetings; i++) {
			sugar().meetings.recordView.gotoNextRecord();
			meetingStatusDetailField.assertEquals(meetingDefaultData.get("status"), true);
			assertMeetingFieldsAndGuests(inviteesCountParentMeeting);
			leadInvitee.assertExists(false);
		}

		/**
		 * Open another meeting in the series, which has not yet modified as above and Select "Edit All Recurrences",
		 * only change subject such as add extra string as "Edit" to distinguish what you have changed in the middle 
		 * record.
		 */
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(3);
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		sugar().meetings.recordView.getControl("editAllReocurrences").click();
		VoodooUtils.waitForReady();
		sugar().meetings.recordView.getEditField("name").set(testName);
		sugar().meetings.recordView.save();

		/**
		 * Saving changes in "Edit All Recurrences" should replace all the content in each record of the series. 
		 * In this case, we look at the Start Date, End Date and Guests fields and other fields are in sync.
		 */
		sugar().meetings.navToListView();
		sugar().meetings.listView.sortBy("headerDatestart", true);
		sugar().meetings.listView.clickRecord(1);
		VoodooUtils.waitForReady(); // Extra wait needed

		for(int i = 0; i < totalRecurringMeetings; i++) {
			nameDetailField.assertEquals(testName, true);
			meetingStatusDetailField.assertEquals(meetingDefaultData.get("status"), true);
			sugar().meetings.recordView.getDetailField("date_start_date").assertContains(date.plusDays(i).toString(dtf), true);
			assertMeetingFieldsAndGuests(inviteesCountParentMeeting);
			if (i <= totalRecurringMeetings - 1)
				sugar().meetings.recordView.gotoNextRecord();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	// Using Private Method for recurring assertion of startTime, description, relatedToParentType, relatedToParentName and Guests
	private void assertMeetingFieldsAndGuests(int guestCount) throws Exception{
		sugar().meetings.recordView.getDetailField("date_start_time").assertContains(meetingDefaultData.get("date_start_time"), true);
		sugar().meetings.recordView.getDetailField("description").assertEquals(meetingDefaultData.get("description"), true);
		sugar().meetings.recordView.getDetailField("relatedToParentName").assertEquals(sugar().quotes.getDefaultData().get("name"), true);
		assertTrue(rowInvitees.countWithClass() == guestCount);
		userInvitee.assertEquals(qauserLastName, true);
		contactInvitee.assertEquals(contactDefaultData.get("fullName"), true);
	}

	public void cleanup() throws Exception {}
}