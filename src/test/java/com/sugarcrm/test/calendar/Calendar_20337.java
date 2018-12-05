package com.sugarcrm.test.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20337 extends SugarTest {
	FieldSet meetingData;

	public void setup() throws Exception {
		meetingData = sugar().meetings.getDefaultData();
		sugar().login();
	}

	/**
	 * Schedule meeting_Verify that meeting is scheduled by Navigation shortcuts.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20337_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();

		// Go to Meeting Module
		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleMeeting");
		sugar().meetings.createDrawer.setFields(meetingData);
		sugar().calls.createDrawer.getEditField("date_start_date").set(dateFormat.format(date));
		sugar().calls.createDrawer.getEditField("date_start_time").set("08:00am");
		sugar().meetings.createDrawer.save();

		// TODO: TR-11453 - Record(s) are not visible in the Calendar cell without hard refresh the page, when creating from the Calendar's mega drop down.
		VoodooUtils.refresh();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify record saved and check subject in Calendar view
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").assertContains(meetingData.get("name"), true);

		// Goto Meeting record view
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").click();
		VoodooUtils.waitForReady();
		
		// Verify data in meeting detail view.
		sugar().meetings.recordView.getDetailField("name").assertEquals(meetingData.get("name"), true);
		sugar().meetings.recordView.getDetailField("status").assertEquals(meetingData.get("status"), true);
		sugar().meetings.recordView.getDetailField("description").assertEquals(meetingData.get("description"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
