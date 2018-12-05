package com.sugarcrm.test.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20389 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Close and Create New Meeting_Verify that the meeting's status is changed automatically 
	 * after clicking the "Close and Create New" button.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20389_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();

		// Click "Calendar" navigation tab & Schedule a Meeting
		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleMeeting");
		
		// Enter all the Mandatory fields
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.getEditField("date_start_date").set(dateFormat.format(date));
		sugar().calls.createDrawer.getEditField("date_start_time").set("08:00am");
		
		// Set status = "Scheduled"
		sugar().meetings.createDrawer.getEditField("status").set(sugar().meetings.getDefaultData().get("status"));
		
		// Save meeting
		sugar().meetings.createDrawer.save();

		// TODO: TR-11453 - Record(s) are not visible in the Calendar cell without hard refresh the page, when creating from the Calendar's mega drop down.
		VoodooUtils.refresh();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify record saved and check subject in Calendar view
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").assertContains(testName, true);

		// Goto Meeting record view
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").click();
		VoodooUtils.waitForReady();
		
		// Click  "Close and Create New" button in meeting's detail view
		sugar().meetings.recordView.closeAndCreateNew();
		sugar().meetings.createDrawer.save();

		// Verify the first meeting's status is "Held" in detail view.
		sugar().meetings.recordView.getDetailField("status").assertEquals(customData.get("status"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}