package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27587 extends SugarTest {	
	FieldSet meetingData;

	public void setup() throws Exception {
		meetingData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that schedule a meeting is created and saved correctly
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27587_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Meetings module and click on Create.
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// Subject is marked as required.
		sugar().meetings.createDrawer.getEditField("name").assertAttribute("class", "required", true);
		String startTime = sugar().meetings.createDrawer.getEditField("date_start_time").getText();
		String endTime = sugar().meetings.createDrawer.getEditField("date_end_time").getText();
		String startDate = sugar().meetings.createDrawer.getEditField("date_start_date").getText();
		String endDate = sugar().meetings.createDrawer.getEditField("date_end_date").getText();

		// Start date time,end date time, and team are pre-filled fields.
		sugar().meetings.createDrawer.getEditField("date_start_date").assertContains(startDate, true);
		sugar().meetings.createDrawer.getEditField("date_start_time").assertContains(startTime, true);
		sugar().meetings.createDrawer.getEditField("date_end_date").assertContains(endDate, true);
		sugar().meetings.createDrawer.getEditField("date_end_time").assertContains(endTime, true);

		// Status is Scheduled by default.Repeat should default to Select.The meeting should be assigned to the logged in user by default.
		sugar().meetings.createDrawer.getEditField("status").assertContains(sugar().meetings.getDefaultData().get("status"), true);
		sugar().meetings.createDrawer.getEditField("assignedTo").assertContains(meetingData.get("user"), true);

		// TODO: VOOD-1169
		new VoodooSelect("span", "css", ".fld_repeat_type.edit div").assertContains(meetingData.get("default_repeat_type"), true);

		// Fill in all required and non-required fields.Click on Save.
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("description").set(sugar().meetings.getDefaultData().get("description"));
		sugar().meetings.createDrawer.save();

		// Verify that a new meeting is saved with all values set correctly.
		sugar().alerts.getSuccess().assertContains(meetingData.get("success_message")+" "+testName+" "+"for"+" "+startDate, true);
		sugar().meetings.listView.clickRecord(1);

		// TODO: VOOD-1217
		VoodooControl dateTimeCtrl = new VoodooControl("div", "css", ".fld_duration.detail");
		dateTimeCtrl.assertContains(startDate, true);
		dateTimeCtrl.assertContains(endDate, true);
		sugar().meetings.recordView.getDetailField("status").assertContains(sugar().meetings.getDefaultData().get("status"), true);
		sugar().meetings.recordView.getDetailField("assignedTo").assertContains(meetingData.get("user"), true);
		sugar().meetings.recordView.getDetailField("description").assertContains(sugar().meetings.getDefaultData().get("description"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}