package com.sugarcrm.test.calls;

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

public class Calls_29314 extends SugarTest {
	VoodooControl userInvitee, contactInvitee, rowInvitees;
	FieldSet callDefaultData = new FieldSet();
	FieldSet contactDefaultData = new FieldSet();
	FieldSet leadDefaultData = new FieldSet();
	String qauserLastName = "";
	String quoteModuleNameSingular = "";

	public void setup() throws Exception {
		// Create a quote, contact and lead record
		sugar().quotes.api.create();
		sugar().contacts.api.create();
		sugar().leads.api.create();

		callDefaultData = sugar().calls.getDefaultData();
		contactDefaultData = sugar().contacts.getDefaultData();
		leadDefaultData = sugar().leads.getDefaultData();
		qauserLastName = sugar().users.qaUser.get("lastName");
		quoteModuleNameSingular = sugar().quotes.moduleNameSingular;

		// Log-In as a qaUser
		sugar().login(sugar().users.qaUser);

		// TODO: VOOD-1223 - Need library support to get records in Guests list in calls record view
		rowInvitees = new VoodooControl("div", "css", ".row.participant");
		userInvitee = new VoodooControl("div", "css", ".row.participant[data-module='Users'] .name");
		contactInvitee = new VoodooControl("div", "css", ".row.participant[data-module='Contacts'] .name");
	}

	/**
	 * Verify that "Edit All Recurrences" replace all record in the series of calls
	 * @throws Exception
	 */
	@Test
	public void Calls_29314_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet callsInfo = testData.get(testName).get(0);
		String repeatType = callsInfo.get("repeatType");
		String repeatOccur = callsInfo.get("repeatOccur");

		// Navigate to the calls module
		sugar().navbar.navToModule(sugar().calls.moduleNamePlural);

		/**
		 * qaUser creates a repeat call, e.g. 3 days, Related To a Quote record,
		 * has a string in Description, add one Contact record as invitee, and etc. Save it.
		 */
		sugar().calls.listView.create();
		LocalDate date = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");
		sugar().calls.createDrawer.getEditField("name").set(callDefaultData.get("name"));
		sugar().calls.createDrawer.getEditField("date_start_time").set(callDefaultData.get("date_start_time"));
		new VoodooControl("body", "css", "body").click();
		sugar().calls.createDrawer.getEditField("repeatType").set(repeatType);
		sugar().calls.createDrawer.getEditField("repeatEndType").set(callsInfo.get("repeatEndType"));
		sugar().calls.createDrawer.getEditField("repeatOccur").set(repeatOccur);
		sugar().calls.createDrawer.getEditField("description").set(callDefaultData.get("description"));
		VoodooSelect relatedToParentType = (VoodooSelect)sugar().calls.createDrawer.getEditField("relatedToParentType");
		relatedToParentType.click();
		relatedToParentType.selectWidget.getControl("searchBox").set(quoteModuleNameSingular);
		// TODO: VOOD-629 - Add support for accessing and manipulating individual components of a VoodooSelect
		new VoodooControl("li", "css", ".select2-drop-active .select2-results li:not(.select2-highlighted)").click();
		sugar().calls.createDrawer.getEditField("relatedToParentName").set(quoteModuleNameSingular);
		sugar().calls.createDrawer.clickAddInvitee();
		sugar().calls.createDrawer.selectInvitee(contactDefaultData.get("lastName"));
		sugar().calls.createDrawer.save();

		int totalRecurringCalls = Integer.parseInt(repeatOccur);
		sugar().calls.listView.clickRecord(1);

		// Assert that 2 guests are displayed in the parent call guest list
		userInvitee.assertEquals(qauserLastName, true);
		contactInvitee.assertEquals(contactDefaultData.get("fullName"), true);
		int inviteesCountParentCall = Integer.parseInt(callsInfo.get("guestsCountParentCall"));
		assertTrue(rowInvitees.countWithClass() == inviteesCountParentCall);

		VoodooControl nameDetailField = sugar().calls.recordView.getDetailField("name");

		// Verify that In this series of 3 calls, the fields have same value as what we described above in the Parent call
		for(int i = 1; i < totalRecurringCalls; i ++) {
			sugar().calls.recordView.gotoNextRecord();
			nameDetailField.assertEquals(callDefaultData.get("name"), true);
			sugar().calls.recordView.getDetailField("repeatType").assertEquals(repeatType, true);
			sugar().calls.recordView.getDetailField("repeatOccur").assertEquals(repeatOccur, true);
			assertCallFieldsAndGuests(inviteesCountParentCall);
		}

		/**
		 * Edit the middle record, e.g. add extra string in the subject, change a different description, 
		 * change the "Related To" to Lead, therefore a Lead, a Contact and a User are in the Guests field. 
		 * Even change call start time to one hour later, change status to Held and Save it.
		 */
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(2);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.getEditField("date_start_time").set(callsInfo.get("newStartTime"));
		sugar().calls.recordView.getEditField("description").set(testName);
		sugar().calls.recordView.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		sugar().calls.recordView.getEditField("relatedToParentName").set(leadDefaultData.get("lastName"));
		sugar().calls.recordView.getEditField("status").set(callsInfo.get("callStatus"));
		sugar().calls.recordView.save();

		// Assert that now 3 guests are displayed in the guests list
		assertTrue(rowInvitees.countWithClass() == totalRecurringCalls);
		userInvitee.assertEquals(qauserLastName, true);
		contactInvitee.assertEquals(contactDefaultData.get("fullName"), true);
		VoodooControl leadInvitee = new VoodooControl("div", "css", ".row.participant[data-module='Leads'] .name");
		leadInvitee.assertEquals(leadDefaultData.get("fullName"), true);

		// Verify that now only this record has different call content with other calls in the series.
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		VoodooControl callStatusDetailField = sugar().calls.recordView.getDetailField("status");

		for(int i = 1; i < totalRecurringCalls; i ++) {
			sugar().calls.recordView.gotoNextRecord();
			callStatusDetailField.assertEquals(callDefaultData.get("status"), true);
			assertCallFieldsAndGuests(inviteesCountParentCall);
			leadInvitee.assertExists(false);
		}

		/**
		 * Open another call in the series, which has not yet modified as above and Select "Edit All Recurrences",
		 * only change subject such as add extra string as "Edit" to distinguish what you have changed in the middle 
		 * record.
		 */
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(3);
		sugar().calls.recordView.openPrimaryButtonDropdown();
		sugar().calls.recordView.getControl("editAllReocurrences").click();
		sugar().calls.recordView.getEditField("name").set(testName);
		sugar().calls.recordView.save();

		/**
		 * Saving changes in "Edit All Recurrences" should replace all the content in each record of the series. 
		 * In this case, we look at the Start Date, End Date and Guests fields and other fields are in sync.
		 */
		sugar().calls.navToListView();
		sugar().calls.listView.sortBy("headerDatestart", true);
		sugar().calls.listView.clickRecord(1);
		VoodooUtils.waitForReady(); // Extra wait needed

		for(int i = 0; i < totalRecurringCalls; i++) {
			nameDetailField.assertEquals(testName, true);
			callStatusDetailField.assertEquals(callDefaultData.get("status"), true);
			sugar().calls.recordView.getDetailField("date_start_date").assertContains(date.plusDays(i).toString(dtf), true);
			assertCallFieldsAndGuests(inviteesCountParentCall);
			if (i <= totalRecurringCalls-1)
				sugar().calls.recordView.gotoNextRecord();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	// Using Private Method for recurring assertion of startTime, description, relatedToParentType, relatedToParentName and Guests
	private void assertCallFieldsAndGuests(int guestCount) throws Exception{
		sugar().calls.recordView.getDetailField("date_start_time").assertContains(callDefaultData.get("date_start_time"), true);
		sugar().calls.recordView.getDetailField("description").assertEquals(callDefaultData.get("description"), true);
		sugar().calls.recordView.getDetailField("relatedToParentName").assertEquals(sugar().quotes.getDefaultData().get("name"), true);
		assertTrue(rowInvitees.countWithClass() == guestCount);
		userInvitee.assertEquals(qauserLastName, true);
		contactInvitee.assertEquals(contactDefaultData.get("fullName"), true);
	}

	public void cleanup() throws Exception {}
}