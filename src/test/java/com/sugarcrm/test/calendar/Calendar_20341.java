package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20341 extends SugarTest {
	String todaysDate, dateAfter1Days;

	public void setup() throws Exception {
		FieldSet startDate = new FieldSet();

		// Get Today's Date from Instance
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = sdf.format(date);

		// Date after 1 Day
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		dateAfter1Days = sdf.format(date);
		startDate.put("date_start_date",todaysDate);
		sugar.meetings.api.create(startDate);
		sugar.login();
	}

	/**
	 * Edit meeting_Verify that a meeting is edited.
	 * @throws Exception
	 */	
	@Test
	public void Calendar_20341_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet meetingData = testData.get(testName).get(0);

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-863
		VoodooControl meetingCtrl = new VoodooControl("div", "css", ".week div[time='11:00am'] div div.head");
		meetingCtrl.assertContains(sugar.meetings.getDefaultData().get("name"), true);
		meetingCtrl.click();

		// Updating the already created Meeting Record.
		sugar.meetings.recordView.edit();
		sugar.meetings.recordView.getEditField("name").set(testName);
		sugar.meetings.recordView.getEditField("status").set(meetingData.get("status"));
		sugar.meetings.recordView.getEditField("date_start_time").set(meetingData.get("date_start_time"));
		sugar.meetings.recordView.getEditField("date_start_date").set(dateAfter1Days);
		sugar.meetings.recordView.getEditField("description").set(meetingData.get("description"));
		sugar.meetings.recordView.getEditField("date_end_time").set(meetingData.get("date_end_time"));
		sugar.meetings.recordView.getEditField("date_end_date").set(dateAfter1Days);
		sugar.meetings.recordView.getEditField("location").set(meetingData.get("location"));
		sugar.meetings.recordView.save();

		// Verify the updated Meeting Record.
		sugar.meetings.recordView.getDetailField("name").assertContains(testName, true);
		sugar.meetings.recordView.getDetailField("status").assertContains(meetingData.get("status"),true);
		sugar.meetings.recordView.getDetailField("date_start_date").assertContains(dateAfter1Days,true);
		sugar.meetings.recordView.getDetailField("description").assertContains(meetingData.get("description"), true);
		sugar.meetings.recordView.getDetailField("date_start_time").assertContains(meetingData.get("date_start_time"), true);
		sugar.meetings.recordView.getDetailField("date_end_time").assertContains(meetingData.get("date_end_time"), true);
		sugar.meetings.recordView.getDetailField("location").assertContains(meetingData.get("location"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}