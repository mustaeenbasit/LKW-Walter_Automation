package com.sugarcrm.test.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_27578 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().login();
	}

	/**
	 * Verify that new Meeting create form opens when schedule meeting from Calendar.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_27578_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();

		// Go to Calendar
		// In top nav click on Schedule Meeting. 
		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleMeeting");
		sugar().meetings.createDrawer.setFields(ds.get(0));
		sugar().meetings.createDrawer.getEditField("date_start_date").set(dateFormat.format(date));
		sugar().meetings.createDrawer.getEditField("date_start_time").set("08:00am");
		// save the record
		sugar().meetings.createDrawer.save();

		// In top nav click on Schedule Call.
		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleCall");
		sugar().calls.createDrawer.setFields(ds.get(1));
		sugar().calls.createDrawer.getEditField("date_start_date").set(dateFormat.format(date));
		sugar().calls.createDrawer.getEditField("date_start_time").set("08:00am");
		// save the record
		sugar().calls.createDrawer.save();

		// TODO: TR-11453 - Record(s) are not visible in the Calendar cell without hard refresh the page, when creating from the Calendar's mega drop down.
		VoodooUtils.refresh();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Assert that a new meeting and a new call is saved in Calendar 
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").assertContains(ds.get(0).get("name"), true);
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").assertContains(ds.get(1).get("name"), true);

		VoodooUtils.focusDefault();
		
		// navigate to meetings list view
		sugar().meetings.navToListView();
		// Very a new meeting is saved meetings listview.
		sugar().meetings.listView.verifyField(1, "name", ds.get(0).get("name"));

		// navigate to calls list view
		sugar().calls.navToListView();
		// Very a new meeting is saved meetings listview.
		sugar().calls.listView.verifyField(1, "name", ds.get(1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}