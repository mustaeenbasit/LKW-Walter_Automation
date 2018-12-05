package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27285 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that user's availabilities in meeting scheduler
	 * @throws Exception
	 */
	@Test
	public void Meetings_27285_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating Chris & Sally user records
		FieldSet userData = testData.get(testName+"_sally").get(0);
		UserRecord chris = (UserRecord) sugar().users.create();
		UserRecord sally = (UserRecord)sugar().users.create(userData);
		sugar().logout();

		// Login with sally create a Meetings record
		sugar().login(sally);
		sugar().meetings.create();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();

		// Invites Chris in Sally's meeting
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(chris.get("lastName"));
		sugar().meetings.recordView.save();
		sugar().logout();

		// Logged in as Chris and schedules a meeting at the same time the Sally meeting scheduled. 
		sugar().login(chris);
		FieldSet meetingRecord = new FieldSet();
		meetingRecord.put("name", testName);
		sugar().meetings.create(meetingRecord);

		// Navigate to newly created Meetings record
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.showMore();

		// Invites Sally in Chris meeting
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sally.get("lastName"));
		sugar().meetings.recordView.save();

		// Verify in the scheduler, in Chris row, he sees the busy color as Dark Grey.
		// TODO: VOOD-1699 - Need Lib support to verify 'booked time slot' in Calls/Meetings
		FieldSet meetingData = testData.get(testName).get(0);
		new VoodooControl("span", "css", ".fld_invitees .participant div:nth-child(5) .busy").assertCssAttribute("background-color", meetingData.get("grey_color"));

		// Verify in the scheduler, in Sally row the time slot is in Dark Grey(conflicting)
		new VoodooControl("span", "css", ".fld_invitees div:nth-child(5).participant div:nth-child(5) .busy").assertCssAttribute("background-color", meetingData.get("grey_color"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}