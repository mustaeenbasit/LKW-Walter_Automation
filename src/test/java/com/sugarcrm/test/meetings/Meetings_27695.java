package com.sugarcrm.test.meetings;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27695 extends SugarTest {	
	FieldSet meetingData;
	UserRecord myUser;
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		// Login with QAUser
		sugar().login(sugar().users.getQAUser());

		myContact = (ContactRecord)sugar().contacts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().revLineItems.api.create();
		myUser = (UserRecord) sugar().users.api.create();
		meetingData = testData.get(testName).get(0);
	}

	/**
	 * Verify that edit parent meeting and it displays /edit/all_recurrences
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27695_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		String meetingDate = sdf.format(calendar.getTime());
		calendar.setTime(sdf.parse(meetingDate));

		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		FieldSet repeatData = new FieldSet();
		repeatData.put("repeatType", meetingData.get("repeatType"));

		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myContact);

		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myLead);

		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myUser);

		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.getEditField("date_start_date").set(meetingDate);
		sugar().meetings.createDrawer.repeatOccurrences(repeatData);
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(meetingData.get("repeatOccur"));

		// Save meeting
		sugar().meetings.createDrawer.save();
		sugar().meetings.navToListView();

		// TODO: VOOD-1288
		// Open the parent meeting
		// Using XPath to select meetings by unique date.
		new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+meetingDate+"')]/td[2]/span/div/a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Click on action drop down, select "Edit All Recurrences".
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		sugar().meetings.recordView.getControl("editAllReocurrences").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify in URL that "edit/all_recurrences" is exist.
		sugar().meetings.recordView.getEditField("date_start_date").assertContains(meetingDate, true);
		String currentUrl1 = VoodooUtils.getUrl();
		assertTrue("URL does not contain string edit/all-recurrences when it should. "+currentUrl1, currentUrl1.contains("edit/all-recurrences") || currentUrl1.contains("edit/all_recurrences"));

		// In the parent meeting record, change one field, such as add an RLI record in "Related To" field.
		sugar().meetings.recordView.getEditField("relatedToParentType").set(meetingData.get("rliName"));
		sugar().meetings.recordView.getEditField("relatedToParentName").set(sugar().revLineItems.getDefaultData().get("name"));

		// Save it
		sugar().meetings.recordView.save();

		// After saving, notice the URL doesn't have 'edit/all_recurrences' any more.
		String currentUrl2 = VoodooUtils.getUrl();
		assertFalse("URL contains string edit/all-recurrences when it should not. "+currentUrl2, currentUrl2.contains("edit/all-recurrences") || currentUrl2.contains("edit/all_recurrences"));

		// Check other meetings.
		for(int i=0; i<4; i++){
			// Using XPath to select meetings by unique date.
			sugar().meetings.navToListView();
			// TODO: VOOD-1288
			new VoodooControl("a", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+meetingDate+"')]/td[2]/span/div/a").click();
			sugar().alerts.waitForLoadingExpiration();

			// Verify that updated changes are reflecting in all meeting records
			sugar().meetings.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("fullName"), true);
			sugar().meetings.recordView.getControl("invitees").assertContains(sugar().users.getDefaultData().get("fullName"), true);
			sugar().meetings.recordView.getControl("invitees").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
			sugar().meetings.recordView.getDetailField("relatedToParentName").assertContains(sugar().revLineItems.getDefaultData().get("name"), true);
			calendar.add(Calendar.YEAR, 1); 
			meetingDate = sdf.format(calendar.getTime());
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}