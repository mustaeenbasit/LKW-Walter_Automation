package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_27168 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that a Task can be created when click on a cell in Calendar.
	 * @throws Exception
	 */
	@Test
	public void Calendar_27168_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		// Get Today's Date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = sdf.format(date);

		// Date after 1 Day
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		String dateAfter1Day = sdf.format(date);

		// Yesterday's Date
		cal.add(Calendar.DATE, -2);
		date = cal.getTime();
		String yesterdaysDate = sdf.format(date);

		// TODO: VOOD-863 need support for Calendar module
		sugar.navbar.navToModule("Calendar");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		// Click on a cell in Calendar in tomorrow 11am. 
		VoodooControl nextDayCtrl = new VoodooControl("div", "css", ".week div[datetime='"+ dateAfter1Day +" "+ ds.get(0).get("cell_time") + "']");
		nextDayCtrl.click();
		VoodooUtils.focusDefault();

		// Click on the "Create Task" link in the yellow bar.
		sugar.alerts.getAlert().clickLink(0);

		//  Verify that Start Date and Start Time is blank. 
		sugar.tasks.createDrawer.getEditField("date_start_date").assertContains("", true);
		sugar.tasks.createDrawer.getEditField("date_start_time").assertContains("", true);

		// Fill in required fields in Task Create form. Select a Start day is before Due Date. i.e today, 11am. 
		sugar.tasks.createDrawer.getEditField("subject").set(ds.get(0).get("taskName"));
		sugar.tasks.createDrawer.getEditField("date_start_date").set(todaysDate);
		sugar.tasks.createDrawer.getEditField("date_start_time").set(ds.get(0).get("time"));
		// save the task 
		sugar.tasks.createDrawer.save();

		VoodooUtils.focusFrame("bwc-frame");
		// Verify that the new task appears in tomorrow's weekly calendar. 
		nextDayCtrl.assertEquals(ds.get(0).get("taskName"), true);

		// Click on a cell in Calendar in tomorrow 11am. 
		nextDayCtrl.click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();

		//  Verify that Due Date is 11am of tomorrow.  Start Date is 11am of today in task record. 
		sugar.tasks.recordView.getDetailField("date_start_date").assertContains(todaysDate +" " + ds.get(0).get("time") , true);
		sugar.tasks.recordView.getDetailField("date_due_date").assertContains(dateAfter1Day +" "+ ds.get(0).get("time") , true);

		// navigate back to Calendar module 
		sugar.navbar.navToModule("Calendar");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl previousDayCtrl = new VoodooControl("div", "css", ".week div[datetime='"+ yesterdaysDate +" "+ ds.get(1).get("cell_time") + "']");
		// Click on another cell, such as yesterday 2pm. 
		previousDayCtrl.click();
		VoodooUtils.focusDefault();

		// Click on the "Create Task" link in the yellow bar.
		sugar.alerts.getAlert().clickLink(0);

		//  Verify that Start Date and Start Time is blank. 
		sugar.tasks.createDrawer.getEditField("date_start_date").assertContains("", true);
		sugar.tasks.createDrawer.getEditField("date_start_time").assertContains("", true);

		// Fill in required fields in Task Create form.	Select a Start day is before the Due Date. i.e yesterday, 8am. 
		sugar.tasks.createDrawer.getEditField("subject").set(ds.get(1).get("taskName"));
		sugar.tasks.createDrawer.getEditField("date_start_time").set(ds.get(1).get("time"));
		sugar.tasks.createDrawer.getEditField("date_start_date").set(yesterdaysDate);
		sugar.tasks.createDrawer.save();

		VoodooUtils.focusFrame("bwc-frame");
		//  The new task appears in yesterday's weekly calendar.
		previousDayCtrl.assertEquals(ds.get(1).get("taskName"), true);
		// Click on the cell, yesterday 2pm. 
		previousDayCtrl.click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that Due Date is 2pm of yesterday.  Start Date is 8am of yesterday.
		sugar.tasks.recordView.getDetailField("date_start_date").assertContains( yesterdaysDate +" " + ds.get(1).get("time") , true);
		sugar.tasks.recordView.getDetailField("date_due_date").assertContains(yesterdaysDate +" "+ ds.get(1).get("cell_time") , true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}