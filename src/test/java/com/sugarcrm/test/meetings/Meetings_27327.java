package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27327 extends SugarTest {
	FieldSet customData;
	MeetingRecord meetingRecord;
	UserRecord myUser;
	
	public void setup() throws Exception {
		// Login with qauser
		sugar().login(sugar().users.getQAUser());

		customData = testData.get(testName).get(0);
		
		myUser = (UserRecord) sugar().users.api.create();

		meetingRecord = (MeetingRecord) sugar().meetings.create();
	}

	/**
	 * Verify that the meeting is copied correctly with one invitees in non-recurrng meeting
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27327_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		meetingRecord.navToRecord();
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("status").set(customData.get("meetingStatus1"));
		
		// In Guests fields, add user as invitees.
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myUser.get("fullName"));
		sugar().meetings.recordView.save();
		
		// In action drop down, select Copy
		meetingRecord.navToRecord();
		sugar().meetings.recordView.copy();
		sugar().meetings.recordView.getEditField("name").set(testName);
		
		// TODO: VOOD-1254: While clicking on save button after copy a Meeting record, save button is not working
		// Once resolved this VOOD-1254 remove line#53, #54 and uncomment line#52
		// sugar().meetings.recordView.save();
		new VoodooControl("a", "css", ".fld_main_dropdown .fld_save_button a").click();
		sugar().alerts.waitForLoadingExpiration(40000); // Required to complete save action
		
		// Verify the Status of the meeting.
		VoodooUtils.voodoo.log.info(sugar().meetings.recordView.getDetailField("status").getText() + " <<< status.>>>"+customData.get("meetingStatus2"));
		sugar().meetings.recordView.getDetailField("status").assertContains(customData.get("meetingStatus2"), true);
		
		// TODO: VOOD-1223
		// Verify the Status of invitee in Guests and other fields
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+myUser.get("lastName")+"')]").assertExists(true);
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+sugar().users.qaUser.get("lastName")+"')]").assertExists(true);
		sugar().meetings.recordView.getDetailField("name").assertContains(testName, true);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}