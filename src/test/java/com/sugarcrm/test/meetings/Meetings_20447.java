package com.sugarcrm.test.meetings;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_20447 extends SugarTest {
	public void setup() throws Exception {
		FieldSet meetingData = testData.get(testName).get(0);
		sugar().login();

		// Create a recurrent meeting
		sugar().navbar.selectMenuItem(sugar().meetings, "create" + sugar().meetings.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeatType"));

		// Set the Until Date
		DateTime date = DateTime.now();
		String repeatUntilDate = date.plusWeeks(2).toString("MM/dd/yyyy");
		sugar().meetings.createDrawer.getEditField("repeatUntil").set(repeatUntilDate);
		sugar().meetings.createDrawer.save();
	}

	/**
	 * Delete weekly recurring meeting
	 * @throws Exception
	 */
	@Test
	public void Meetings_20447_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete all recurrences
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		sugar().meetings.recordView.getControl("deleteAllReocurrences").click();

		VoodooUtils.waitForReady();
		sugar().alerts.getWarning().confirmAlert();

		// Verify all meetings record are removed
		sugar().meetings.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}