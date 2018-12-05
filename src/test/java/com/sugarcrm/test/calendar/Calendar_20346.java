package com.sugarcrm.test.calendar;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Calendar_20346 extends SugarTest {
	String todayDate = "", tomorrowDate = "", yesterdayDate = "", startTime = "", endTime = "";
	CallRecord myCall;
	MeetingRecord myMeeting;

	public void setup() throws Exception {
		DateTime dateToday = DateTime.now();
		VoodooUtils.voodoo.log.info("Weej day no. " + dateToday.dayOfWeek().get());
		
		// Check if today is Sat or Sun and adjust accordingly as for Sat and Sun, todayDate, tomorrowDate and
		// yesterdayDate will fall in different Calendar week pages
		if (dateToday.dayOfWeek().get() == 6) // Sat
			dateToday = dateToday.minusDays(5); // Make it this Mon
		else if (dateToday.dayOfWeek().get() == 7) // Sun
			dateToday = dateToday.plusDays(2); // Make it following Tue

		todayDate = dateToday.toString("MM/dd/yyyy");
		tomorrowDate = dateToday.plusDays(1).toString("MM/dd/yyyy");
		yesterdayDate = dateToday.minusDays(1).toString("MM/dd/yyyy");
		startTime = "08:00am";
		endTime = "08:30am";

		myCall = (CallRecord) sugar().calls.api.create();
		myMeeting = (MeetingRecord) sugar().meetings.api.create();

		sugar().login();
	}

	/**
	 * Verify that call/meeting are displayed day's call/meeting in Calendar panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20346_Calls_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify for today's date
		FieldSet startDate = new FieldSet();
		startDate.put("date_start_date",todayDate);
		startDate.put("date_start_time",startTime);
		startDate.put("date_end_date",todayDate);
		startDate.put("date_end_time",endTime);

		myCall.edit(startDate);

		// Click "Calendar" navigation tab.
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Call subject link in the Calendar view in today's date
		VoodooControl callCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + todayDate + " 08:00am']");
		callCtrl.assertContains(sugar().calls.getDefaultData().get("name"), true);

		VoodooUtils.focusDefault();
		
		// Verify for tomorrow's date
		startDate.clear();
		startDate.put("date_start_date",tomorrowDate);
		startDate.put("date_start_time",startTime);
		startDate.put("date_end_date",tomorrowDate);
		startDate.put("date_end_time",endTime);

		myCall.edit(startDate);

		// Click "Calendar" navigation tab.
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Call subject link in the Calendar view in tomorrow's date
		callCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + tomorrowDate + " 08:00am']");
		callCtrl.assertContains(sugar().calls.getDefaultData().get("name"), true);

		// Verify no Call subject link in the Calendar view in today's date
		callCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + todayDate + " 08:00am']");
		callCtrl.assertContains(sugar().calls.getDefaultData().get("name"), false);

		VoodooUtils.focusDefault();

		// Verify for yesterday's date
		startDate.clear();
		startDate.put("date_start_date",yesterdayDate);
		startDate.put("date_start_time",startTime);
		startDate.put("date_end_date",yesterdayDate);
		startDate.put("date_end_time",endTime);

		myCall.edit(startDate);

		// Click "Calendar" navigation tab.
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Call subject link in the Calendar view in yesterday's date
		callCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + yesterdayDate + " 08:00am']");
		callCtrl.assertContains(sugar().calls.getDefaultData().get("name"), true);

		// Verify no Call subject link in the Calendar view in tomorrow's date
		callCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + tomorrowDate + " 08:00am']");
		callCtrl.assertContains(sugar().calls.getDefaultData().get("name"), false);

		// Verify no Call subject link in the Calendar view in today's date
		callCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + todayDate + " 08:00am']");
		callCtrl.assertContains(sugar().calls.getDefaultData().get("name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Test
	public void Calendar_20346_Meetings_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify for today's date
		FieldSet startDate = new FieldSet();
		startDate.put("date_start_date",todayDate);
		startDate.put("date_start_time",startTime);
		startDate.put("date_end_date",todayDate);
		startDate.put("date_end_time",endTime);

		myMeeting.edit(startDate);

		// Click "Calendar" navigation tab.
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Meeting subject link in the Calendar view in today's date
		VoodooControl MeetingCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + todayDate + " 08:00am']");
		MeetingCtrl.assertContains(sugar().meetings.getDefaultData().get("name"), true);

		VoodooUtils.focusDefault();
		
		// Verify for tomorrow's date
		startDate.clear();
		startDate.put("date_start_date",tomorrowDate);
		startDate.put("date_start_time",startTime);
		startDate.put("date_end_date",tomorrowDate);
		startDate.put("date_end_time",endTime);

		myMeeting.edit(startDate);

		// Click "Calendar" navigation tab.
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Meeting subject link in the Calendar view in tomorrow's date
		MeetingCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + tomorrowDate + " 08:00am']");
		MeetingCtrl.assertContains(sugar().meetings.getDefaultData().get("name"), true);

		// Verify no Meeting subject link in the Calendar view in today's date
		MeetingCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + todayDate + " 08:00am']");
		MeetingCtrl.assertContains(sugar().meetings.getDefaultData().get("name"), false);

		VoodooUtils.focusDefault();

		// Verify for yesterday's date
		startDate.clear();
		startDate.put("date_start_date",yesterdayDate);
		startDate.put("date_start_time",startTime);
		startDate.put("date_end_date",yesterdayDate);
		startDate.put("date_end_time",endTime);

		myMeeting.edit(startDate);

		// Click "Calendar" navigation tab.
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Meeting subject link in the Calendar view in yesterday's date
		MeetingCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + yesterdayDate + " 08:00am']");
		MeetingCtrl.assertContains(sugar().meetings.getDefaultData().get("name"), true);

		// Verify no Meeting subject link in the Calendar view in tomorrow's date
		MeetingCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + tomorrowDate + " 08:00am']");
		MeetingCtrl.assertContains(sugar().meetings.getDefaultData().get("name"), false);

		// Verify no Meeting subject link in the Calendar view in today's date
		MeetingCtrl = new VoodooControl("div", "css", "div.slot[datetime='" + todayDate + " 08:00am']");
		MeetingCtrl.assertContains(sugar().meetings.getDefaultData().get("name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}