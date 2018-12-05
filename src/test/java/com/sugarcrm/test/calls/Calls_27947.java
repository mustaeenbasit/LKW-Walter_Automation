package com.sugarcrm.test.calls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27947 extends SugarTest {
	String startDate, endDate;

	public void setup() throws Exception {
		// Set Start Date & End Date
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		startDate = sdf.format(dt);

		// Set end date after 30 min.
		c.add(Calendar.MINUTE, 30); 
		dt = c.getTime();
		endDate = sdf.format(dt);
		sugar.login();
	}

	/**
	 * Verify the default setting like duration is 30 minutes in Call
	 * @throws Exception
	 */
	@Test
	public void Calls_27947_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
			
		sugar.calls.navToListView();
		sugar.calls.listView.create();
		sugar.calls.createDrawer.getEditField("name").set(sugar.calls.getDefaultData().get("name"));

		// Verify that Start/End date is todays date
		sugar.calls.createDrawer.getEditField("date_start_date").assertContains(startDate, true);
		sugar.calls.createDrawer.getEditField("date_end_date").assertContains(endDate, true);		
		String startTime = sugar.calls.createDrawer.getEditField("date_start_time").getText();
		String endTime = sugar.calls.createDrawer.getEditField("date_end_time").getText();
		sugar.calls.createDrawer.save();

		// Verify that record saved		
		sugar.calls.listView.getDetailField(1, "name").assertContains(sugar.calls.getDefaultData().get("name"), true);

		// Verify that the duration between Start Time and End Time is 30 minutes i.e. "02/13/2015 03:00pm - 03:30pm (30 minutes)"
		sugar.calls.listView.clickRecord(1);
		FieldSet fs = testData.get(testName).get(0);
		String meetingTimes = startDate+" "+startTime+" - "+endTime+" "+fs.get("meeting_duration");
		sugar.calls.recordView.getDetailField("date_start_date").assertContains(meetingTimes , true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}