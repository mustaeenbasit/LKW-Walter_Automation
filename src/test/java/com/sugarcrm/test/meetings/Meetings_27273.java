package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_27273 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Start Date and End Date are editable on Meetings create and and edit record views
	 * @throws Exception
	 */
	@Test
	public void Meetings_27273_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		// Set next date from current date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = sdf.format(date);
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		String tomorrowDate = sdf.format(date);		

		// add 3 days to current date
		cal.add(Calendar.DATE, 3);
		date = cal.getTime();
		String dateAfter3Days = sdf.format(date);

		// Verify start and date fields with current date (by default)
		sugar().navbar.selectMenuItem(sugar().meetings, "create"+sugar().meetings.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("date_start_date").assertContains(todaysDate, true);
		sugar().meetings.createDrawer.getEditField("date_end_date").assertContains(todaysDate, true);

		// Verify Meeting date/time displayed with tomorrows date + duration = 3 hours i.e <tomorrows date> 08:00am - 11:00am (3 hours)
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("date_start_date").set(tomorrowDate);
		sugar().meetings.createDrawer.getEditField("date_start_time").set(ds.get(0).get("date_start_time"));
		sugar().meetings.createDrawer.getEditField("date_end_date").set(tomorrowDate);
		sugar().meetings.createDrawer.getEditField("date_end_time").set(ds.get(0).get("date_end_time"));
		// sugar().meetings.createDrawer.saveAndView(); // TODO: VOOD-1013
		sugar().meetings.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		sugar().meetings.listView.clickRecord(1);
		String dateTimeStr = String.format("%s %s - %s %s", tomorrowDate,ds.get(0).get("date_start_time"), ds.get(0).get("date_end_time"), ds.get(0).get("duration"));
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(dateTimeStr, true);

		// Verify meeting date/time displayed with 3days after to current date + duration = 1 hour i.e <3days after> 05:00pm - 06:00pm (1 hour)
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("date_start_date").set(dateAfter3Days);
		sugar().meetings.recordView.getEditField("date_start_time").set(ds.get(1).get("date_start_time"));
		sugar().meetings.recordView.getEditField("date_end_date").set(dateAfter3Days);
		sugar().meetings.recordView.getEditField("date_end_time").set(ds.get(1).get("date_end_time"));
		sugar().meetings.recordView.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		dateTimeStr = String.format("%s %s - %s %s", dateAfter3Days,ds.get(1).get("date_start_time"), ds.get(1).get("date_end_time"), ds.get(1).get("duration"));
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(dateTimeStr, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}