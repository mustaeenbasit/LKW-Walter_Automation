package com.sugarcrm.test.calls;

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
public class Calls_27273 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that Start Date and End Date are editable on Calls create and and edit record views
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_27273_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		// Set next date from current date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
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
		sugar.navbar.selectMenuItem(sugar.calls, "create" + sugar.calls.moduleNameSingular);
		sugar.calls.createDrawer.getEditField("date_start_date").assertContains(todaysDate, true);
		sugar.calls.createDrawer.getEditField("date_end_date").assertContains(todaysDate, true);

		// Verify Call date/time displayed with next day to current date + duration = 30 minutes i.e <current date> 11:45pm - <next date> 12:15am (30 minutes)
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.getEditField("date_start_time").set(ds.get(0).get("date_start_time"));
		sugar.calls.createDrawer.save();
		if (sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert();

		sugar.calls.listView.clickRecord(1);
		String dateTimeStr = String.format("%s %s - %s %s %s", todaysDate, ds.get(0).get("date_start_time"), tomorrowDate, ds.get(0).get("date_end_time"), ds.get(0).get("duration"));
		sugar.calls.recordView.getDetailField("date_start_date").assertContains(dateTimeStr, true);

		// Verify Call date/time displayed with 3days after to current date + duration = 15 minutes i.e <3days after> 10:30pm - 10:45pm (15 minutes)
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("date_start_date").set(dateAfter3Days);
		sugar.calls.recordView.getEditField("date_start_time").set(ds.get(1).get("date_start_time"));
		sugar.calls.recordView.getEditField("date_end_date").set(dateAfter3Days);
		sugar.calls.recordView.getEditField("date_end_time").set(ds.get(1).get("date_end_time"));
		sugar.calls.recordView.save();
		if (sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert();

		dateTimeStr = String.format("%s %s - %s %s", dateAfter3Days, ds.get(1).get("date_start_time"), ds.get(1).get("date_end_time"), ds.get(1).get("duration"));
		sugar.calls.recordView.getDetailField("date_start_date").assertContains(dateTimeStr, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}