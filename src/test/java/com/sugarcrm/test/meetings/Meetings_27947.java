package com.sugarcrm.test.meetings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27947 extends SugarTest {
	String startDate, endDate;

	public void setup() throws Exception {
		// Set Start Date & End Date
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		startDate = sdf.format(dt);

		// set end date
		c.add(Calendar.MINUTE, 30); 
		dt = c.getTime();
		endDate = sdf.format(dt);
		sugar().login();
	}

	/**
	 * Verify that contact_id isn't available in Meetings/Calls sub panel
	 * @throws Exception
	 */
	@Test
	public void Meetings_27947_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));

		// Verify that Start/End date is todays date
		sugar().meetings.createDrawer.getEditField("date_start_date").assertContains(startDate, true);
		sugar().meetings.createDrawer.getEditField("date_end_date").assertContains(endDate, true);
		String startTime = sugar().meetings.createDrawer.getEditField("date_start_time").getText();
		String endTime = sugar().meetings.createDrawer.getEditField("date_end_time").getText();
		sugar().meetings.createDrawer.save();

		// Verify that record saved		
		sugar().meetings.listView.getDetailField(1, "name").assertContains(sugar().meetings.getDefaultData().get("name"), true);

		// Verify that the duration between Start Time and End Time is 30 minutes
		sugar().meetings.listView.clickRecord(1);
		FieldSet fs = testData.get(testName).get(0);
		String meetingTimes = startDate+" "+startTime+" - "+endTime+" "+fs.get("meeting_duration");
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(meetingTimes , true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}