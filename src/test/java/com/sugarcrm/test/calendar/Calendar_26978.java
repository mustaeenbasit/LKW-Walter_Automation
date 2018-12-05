package com.sugarcrm.test.calendar;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calendar_26978 extends SugarTest {
	FieldSet fs = new FieldSet();
	UserRecord chris;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().login();

		// Setting up the local settings
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-820
		// This is the default settings code still added because mentioned in the test steps
		new VoodooControl("a", "css" ,"#locale").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "select[name='default_time_format']").set(fs.get("defaultTimeFormat"));
		new VoodooControl("input", "css", "input[title='Save']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(30000);

		// Create new Test User
		chris = (UserRecord) sugar().users.create();
	}

	/**
	 * Verify the meeting stays at the Calendar when move to a different day with specific Time Format
	 * @throws Exception
	 */
	@Test
	public void Calendar_26978_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as Chris and update the Advanced Date & Time Format
		sugar().logout();

		chris.login();
		FieldSet dateTimeSetting = new FieldSet();
		dateTimeSetting.put("advanced_dateFormat", fs.get("advancedDateFormat"));
		dateTimeSetting.put("advanced_timeFormat", fs.get("advancedTimeFormat"));
		sugar().users.setPrefs(dateTimeSetting);
		dateTimeSetting.clear();

		// Click "Calendar" navigation tab & Schedule Meeting
		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleMeeting");
		String currentDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("date_start_date").set(currentDate);
		sugar().meetings.createDrawer.getEditField("date_end_date").set(currentDate);
		sugar().meetings.createDrawer.getEditField("date_start_time").set(fs.get("defaultMettingTime"));
		sugar().meetings.createDrawer.save();

		// Navigate to Calendar
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-863
		// Assert that a new meeting is saved on correct date in Calendar 
		new VoodooControl("input", "css", "#month-tab").click();
		VoodooUtils.waitForReady();
		VoodooControl meetingRecord = new VoodooControl("div", "css", "div[datetime='"+currentDate+" "+
				fs.get("defaultMettingTime")+"']"+" "+".head div:nth-child(2)");
		meetingRecord.assertEquals(testName, true);

		// Drag & Drop meeting to one day ahead in Calendar
		DateTime date = DateTime.now();
		String day = date.plusDays(1).toString("dd");
		String month = date.toString("MM");
		String year = date.toString("yyyy");
		VoodooControl movedToDate = new VoodooControl("div", "css", "div[datetime='"+month+"/"+day+"/"+year+" "+
				fs.get("defaultMettingTime")+"']");
		meetingRecord.dragNDrop(movedToDate);

		// Assert that a meeting is moved to new date in Calendar 
		VoodooControl movedMeetingRecord = movedToDate.getChildElement("div",  "css", ".head div:nth-child(2)");
		movedMeetingRecord.assertEquals(testName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}