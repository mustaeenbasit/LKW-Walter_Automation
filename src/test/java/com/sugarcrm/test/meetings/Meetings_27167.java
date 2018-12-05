package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27167 extends SugarTest {
	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that a Meeting is saved and appears in Calendar
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27167_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);
		
		// Navigate to Calendar. 
		sugar().navbar.navToModule(fs.get("calendar"));
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-863
		// Click on a cell in Week view.
		new VoodooControl("div", "css", ".week div[time='11:00am']").click();
		VoodooUtils.focusDefault();

		// Fill in all required fields, such as subject.
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		String startDate = sugar().meetings.createDrawer.getEditField("date_start_date").getText();
		String startTime = sugar().meetings.createDrawer.getEditField("date_start_time").getText();
		String endDate = sugar().meetings.createDrawer.getEditField("date_end_date").getText();
		String endTime = sugar().meetings.createDrawer.getEditField("date_end_time").getText();

		// Save it
		sugar().meetings.createDrawer.save();

		// TODO: VOOD-863
		// A meeting record is saved and appears in the calendar cell.
		VoodooUtils.pause(15000); // Need on jenkins
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl meetingRecord = new VoodooControl("div", "css", ".week div[time='11:00am'] div div.head");
		meetingRecord.assertContains(testName, true);
		meetingRecord.click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// TODO: VOOD-1217
		// Verify that start date is the day and time when click on.The end date is same day and time is 15 minutes after start time.
		VoodooControl durationDetail = new VoodooControl("span", "css", ".fld_duration.detail");
		durationDetail.assertContains(startDate + " " + startTime + " - " + endTime, true);

		// Assigned user is QAUser.Status is Scheduled.Team is Global.
		sugar().meetings.recordView.getDetailField("assignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);
		sugar().meetings.recordView.getDetailField("status").assertEquals(sugar().meetings.getDefaultData().get("status"), true);

		// TODO: VOOD-1217
		//sugar().meetings.recordView.getDetailField("teams").assertContains(fs.get("global"), true);
		new VoodooControl("div", "css", ".fld_team_name.detail div").assertContains(fs.get("global"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}