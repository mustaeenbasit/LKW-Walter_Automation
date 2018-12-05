package com.sugarcrm.test.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20324 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that call edit view is displayed when clicking "Schedule Call" in "Calendar" module.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20324_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet errorMessageData = testData.get(testName).get(0);

		// Click "Log Call"/ "Schedule Call" link on Navigation shortcuts.
		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleCall");

		// Save without filling required fields.
		sugar().calls.createDrawer.save();

		// Verify Error message is displayed when Subject field left blank
		sugar().alerts.waitForVisible();
		sugar().alerts.assertContains(errorMessageData.get("errorMessage"), true);
		sugar().alerts.closeAllError();

		// Enter required subject data and save.
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.getEditField("date_start_time").set("08:00am");
		sugar().calls.createDrawer.save();

		// TODO: TR-11453 - Record(s) are not visible in the Calendar cell without hard refresh the page, when creating from the Calendar's mega drop down.
		VoodooUtils.refresh();
		VoodooUtils.waitForReady();

		VoodooUtils.focusFrame("bwc-frame");

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();

		// Verify record saved and check subject in Calendar view
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").assertContains(testName, true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}