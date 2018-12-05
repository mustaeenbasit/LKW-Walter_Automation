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

public class Meetings_27576 extends SugarTest {
	FieldSet customData;
	MeetingRecord meetingRecord;
	UserRecord myUser;
	ContactRecord contactRecord1, contactRecord2;
	LeadRecord leadRecord;
	
	public void setup() throws Exception {
		// Login with qauser
		sugar().login(sugar().users.getQAUser());

		customData = testData.get(testName).get(0);

		myUser = (UserRecord) sugar().users.api.create();
		
		contactRecord1 = (ContactRecord) sugar().contacts.api.create();
		
		leadRecord = (LeadRecord) sugar().leads.api.create();
		
		meetingRecord = (MeetingRecord) sugar().meetings.create();
	}

	/**
	 * Verify that invitee is copied in the recurring meeting
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27576_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		meetingRecord.navToRecord();
		sugar().meetings.recordView.edit();
		sugar().alerts.waitForLoadingExpiration(); // Required for loading edit meeting record
		sugar().meetings.recordView.getEditField("status").set(customData.get("meetingStatus1"));
		
		// In Guests fields, add user as invitees.
		// In Guests field in Meeting, click on + to add one row and click on Select to bring a search(make sure not click on "Search for more" link.
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(contactRecord1.getRecordIdentifier());

		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(leadRecord.getRecordIdentifier());

		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myUser.get("fullName"));
		
		sugar().meetings.recordView.save();
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		
		// In action drop down, select Copy
		sugar().meetings.recordView.copy();
		sugar().meetings.recordView.getEditField("name").set(testName);
		
		// TODO: VOOD-1254: While clicking on save button after copy a Meeting record, save button is not working
		// Once resolved this VOOD-1254 remove line#58, #59 and uncomment line#57 
		// sugar().meetings.recordView.save();
		new VoodooControl("a", "css", ".fld_main_dropdown .fld_save_button a").click();
		sugar().alerts.waitForLoadingExpiration(70000); // Required for save action
		
		// Verify the Status of the meeting.
		VoodooUtils.voodoo.log.info(sugar().meetings.recordView.getDetailField("status").getText() + " <<< status.>>>"+customData.get("meetingStatus2"));
		sugar().meetings.recordView.getDetailField("status").assertContains(customData.get("meetingStatus2"), true);
		
		// TODO: VOOD-1223
		// Verify that invitee is copied in the recurring meeting
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+myUser.get("lastName")+"')]").assertExists(true);
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+contactRecord1.get("lastName")+"')]").assertExists(true);
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+leadRecord.get("lastName")+"')]").assertExists(true);
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+sugar().users.qaUser.get("lastName")+"')]").assertExists(true);
		sugar().meetings.recordView.getDetailField("name").assertContains(testName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}