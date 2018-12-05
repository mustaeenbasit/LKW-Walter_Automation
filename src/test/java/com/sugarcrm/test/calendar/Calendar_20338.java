package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20338 extends SugarTest {
	FieldSet meetingData;

	public void setup() throws Exception {
		meetingData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Schedule meeting_Verify that a meeting scheduling by Navigation shortcuts is canceled
	 * @throws Exception
	 */
	@Test
	public void Calendar_20338_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Meeting Module via Calendar menu
		// TODO: VOOD-863 need support for Calendar module and megamenu items
		new VoodooControl("i", "css", "[data-module='Calendar'] button i").click();
		new VoodooControl("a", "css", "li[data-module='Calendar'] a[data-navbar-menu-item='LNK_NEW_MEETING']").click();

		// Setting Various Fields of a Meeting Record.
		sugar.meetings.createDrawer.getEditField("name").set(testName);
		sugar.meetings.createDrawer.getEditField("status").set(meetingData.get("status"));
		sugar.meetings.createDrawer.getEditField("date_start_date").set(meetingData.get("date_start_date"));
		sugar.meetings.createDrawer.getEditField("date_start_time").set(meetingData.get("date_start_time"));
		sugar.meetings.createDrawer.getEditField("description").set(meetingData.get("description"));
		sugar.meetings.createDrawer.getEditField("date_end_date").set(meetingData.get("date_end_date"));
		sugar.meetings.createDrawer.getEditField("date_end_time").set(meetingData.get("date_end_time"));
		sugar.meetings.createDrawer.cancel();

		// Verify no record in Meetings list view
		sugar.meetings.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}