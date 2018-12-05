package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27580 extends SugarTest {	
	FieldSet meetingData;
	MeetingRecord myMeeting;
	ContactRecord myCon;

	public void setup() throws Exception {
		myCon = (ContactRecord) sugar().contacts.api.create();
		myMeeting = (MeetingRecord) sugar().meetings.api.create();
		meetingData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that a daily recurring meeting is saved when select in Repeat Occurences
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27580_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit the meeting to a Daily recurring, set Repeat Occurrences as 5.
		myMeeting.navToRecord();
		sugar().meetings.recordView.edit();
		// TODO: VOOD-1169
		new VoodooSelect("span", "css", "div.record div:nth-child(1) div:nth-child(2) span span span:nth-child(1)").set(meetingData.get("repeat_type"));
		new VoodooControl("input", "css", "input[name='repeat_count']").set(meetingData.get("repeat_count"));

		// TODO: VOOD-847
		// Add one additional Contact invitee
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myCon.getRecordIdentifier());

		// Save meeting
		sugar().meetings.recordView.save();
		sugar().meetings.navToListView();

		// Verify the recurring meeting is saved with 5 meeting records.Each one has same meeting info except Start Date and End Date.Start Date is 1 day apart and the first meeting is start at today.
		for(int i=2;i<=6;i++)
		{
			// Using XPath to select meetings by unique date.
			VoodooControl meetingRecord = new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'11/1"+(i)+"/2020')]");
			meetingRecord.assertContains(sugar().meetings.getDefaultData().get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}