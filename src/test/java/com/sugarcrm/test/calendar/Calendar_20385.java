package com.sugarcrm.test.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20385 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * View Call_Verify that "Select" button for "Related To:" field is displayed in the call edit view without any notice.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20385_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();

		// Navigate to "Calendar" module & schedule a call
		sugar().navbar.selectMenuItem(sugar().calendar, "scheduleCall");
		
		// Enter all the Mandatory fields, leaving "Related to" field blank
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.getEditField("date_start_date").set(dateFormat.format(date));
		sugar().calls.createDrawer.getEditField("date_start_time").set("08:00am");

		// Save
		sugar().calls.createDrawer.save();

		// TODO: TR-11453 - Record(s) are not visible in the Calendar cell without hard refresh the page, when creating from the Calendar's mega drop down.
		VoodooUtils.refresh();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify record saved and check subject in Calendar view
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").assertContains(testName, true);

		// Goto Meeting record view
		new VoodooControl("div", "css", "div.slot[datetime='"+dateFormat.format(date)+" 08:00am']").click();
		VoodooUtils.waitForReady();
		
		sugar().calls.recordView.edit();

		// Verify Select" button is displayed next to "Related To" field.
		// TODO: VOOD-1488
		new VoodooControl("span", "css",".flex-relate-record .select2-chosen").assertContains(customData.get("text"), true);

		// Cancel
		sugar().calls.recordView.cancel();

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}