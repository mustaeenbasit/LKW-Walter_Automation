package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27577 extends SugarTest {
	FieldSet customData;
	MeetingRecord meetingRecord;
	UserRecord myUser;
	ContactRecord contactRecord1, contactRecord2;
	
	public void setup() throws Exception {
		// Login with qauser
		sugar().login(sugar().users.getQAUser());

		customData = testData.get(testName).get(0);
		
		myUser = (UserRecord) sugar().users.api.create();
		
		contactRecord1 = (ContactRecord) sugar().contacts.api.create();
		
		FieldSet fs = new FieldSet();
		fs.put("lastName", testName);
		contactRecord2 = (ContactRecord) sugar().contacts.api.create(fs);

		meetingRecord = (MeetingRecord) sugar().meetings.create();
	}

	/**
	 * Verify that invitee is copied in the recurring meeting
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27577_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		meetingRecord.navToRecord();
		sugar().meetings.recordView.edit();
		
		// TODO: VOOD-1216 - Once this VOOD is resolved remove line no.: 38 to 41 and uncomment line no.: 37
		// sugar().meetings.recordView.getEditField("status").set(customData.get("meetingStatus1"));
		new VoodooControl("a", "css", ".fld_status.edit div a").click();
		VoodooControl statusList = new VoodooControl("li", "css", "#select2-drop > ul > li:nth-of-type(2)");
		statusList.waitForVisible();
		statusList.click();
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(contactRecord1.getRecordIdentifier());

		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(contactRecord2.getRecordIdentifier());

		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myUser.get("fullName"));
		
		sugar().meetings.recordView.save();
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		
		// In action drop down, select Copy
		sugar().meetings.recordView.copy();
		sugar().meetings.recordView.getEditField("name").set(customData.get("meetingName"));
		
		// TODO: VOOD-1254: While clicking on save button after copy a Meeting record, save button is not working
		// Once resolved this VOOD-1254 remove line#58, #59 and uncomment line#57 
		// sugar().meetings.recordView.save();
		new VoodooControl("a", "css", ".fld_main_dropdown .fld_save_button a").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify the Status of the meeting.
		VoodooUtils.voodoo.log.info(sugar().meetings.recordView.getDetailField("status").getText() + " <<< status.>>>"+customData.get("meetingStatus2"));
		sugar().meetings.recordView.getDetailField("status").assertContains(customData.get("meetingStatus2"), true);
		
		// Verify that invitee is copied in the recurring meeting
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+myUser.get("lastName")+"')]").assertExists(true);
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+contactRecord1.get("lastName")+"')]").assertExists(true);
		new VoodooControl("div", "xpath", "//span[contains(@class,'fld_invitees')]/div/div[contains(.,'"+contactRecord2.get("lastName")+"')]").assertExists(true);
		sugar().meetings.recordView.getDetailField("name").assertContains(customData.get("meetingName"), true);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}