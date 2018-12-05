package com.sugarcrm.test.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_17113 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();	
	}

	/**
	 * Verify default values on time fields.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17113_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		FieldSet customData = testData.get(testName).get(0);

		// Get Month, Day, Year from Calendar
		Date date = new Date();
		SimpleDateFormat dateFormat  = new SimpleDateFormat("MM/dd/yyyy");
		String currentDate = dateFormat.format(date);

		// Create Task
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);
		sugar().tasks.listView.create();
		sugar().tasks.createDrawer.getEditField("subject").set(customData.get("subject"));
		sugar().tasks.createDrawer.getEditField("date_start_date").set(currentDate);

		// Getting the current time stamp immediately after adding the date
		String currentTime = VoodooUtils.getCurrentTimeStamp("hh:mm a");
		currentTime = currentTime.trim().toLowerCase().replaceAll(" ", "");

		// Verify that time field displays current time of the system.
		sugar().tasks.createDrawer.getEditField("date_start_time").assertEquals(currentTime, true);

		// save task
		sugar().tasks.createDrawer.save();
		sugar().tasks.listView.clickRecord(1);

		// Verify date & time that we set
		sugar().tasks.recordView.getDetailField("date_start_date").assertContains(currentDate, true);
		sugar().tasks.recordView.getDetailField("date_start_time").assertContains(currentTime, true);

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}