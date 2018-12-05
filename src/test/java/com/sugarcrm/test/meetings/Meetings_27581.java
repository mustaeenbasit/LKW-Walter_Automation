package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27581 extends SugarTest {	
	FieldSet meetingData;
	MeetingRecord myMeeting;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		myMeeting = (MeetingRecord) sugar().meetings.api.create();
		meetingData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that a weekly recurring meeting is saved when select in Repeat on Days of Week
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27581_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit meeting to a weekly recurring, set Repeat Interval as 1, set "Repeat on Days of Week" as Monday, Wednesday, set "Repeat Occurrences" is 5.
		myMeeting.navToRecord();
		sugar().meetings.recordView.edit();

		// TODO: VOOD-1169
		new VoodooSelect("span", "css", "div.record div:nth-child(1) div:nth-child(2) span span span:nth-child(1)").set(meetingData.get("repeat_type"));
		new VoodooControl("li", "css", ".select2-choices li:nth-child(1) a").click();
		VoodooControl daySelect = new VoodooSelect("input", "css", ".select2-choices input[type='text']");
		daySelect.set(meetingData.get("day_monday"));
		daySelect.set(meetingData.get("day_wednesday"));
		new VoodooControl("input", "css", "input[name='repeat_count']").set(meetingData.get("repeat_count"));

		// TODO: VOOD-847
		// Add one additional Contact invitee
		sugar().meetings.createDrawer.clickAddInvitee();
		new VoodooControl("input", "css", "#select2-drop div input").set(sugar().contacts.getDefaultData().get("firstName"));
		new VoodooControl("div", "css", "#select2-drop ul:nth-child(2) li:nth-child(1) div").click();
		// Save meeting
		sugar().meetings.recordView.save();
		sugar().meetings.navToListView();

		// Verify the recurring meeting is saved with 5 meeting records.Each one has same meeting info except Start Date and End Date.Start Date is 1 day apart. 
		for(int i = 1; i <=5; i++){
			// Using XPath to select meetings by unique date.
			// TODO: VOOD-1169
			VoodooControl meetingRecordDate = new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+meetingData.get("date_meeting"+i)+"')]");
			meetingRecordDate.assertContains(sugar().meetings.getDefaultData().get("name"), true);
			VoodooControl meetingRecord = new VoodooControl("tr", "xpath", "//*[@id='content']/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[1]/table/tbody/tr[contains(.,'"+meetingData.get("date_meeting"+i)+"')]/td[2]/span/div");
			meetingRecord.click();
			sugar().alerts.waitForLoadingExpiration();
			// Verify "Repeat on Days of Week" is Mon and Wed for all 5 meeting records.
			VoodooControl repeatDays = new VoodooControl("span", "css", "div:nth-child(2) > div > span.normal.index span div:nth-child(2) span div");
			repeatDays.assertContains(meetingData.get("repeat_days"), true);
			sugar().meetings.navToListView();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}