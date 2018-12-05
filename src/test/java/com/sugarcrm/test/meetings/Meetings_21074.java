package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21074 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that a meeting is not scheduled when mandatory fields are not entered.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21074_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a meeting without entering the required fields.
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.save();
		sugar().alerts.getError().assertContains(testData.get(testName).get(0).get("error_msg"), true);
		sugar().alerts.getError().closeAlert();
		
		// Cancel meeting record
		sugar().meetings.createDrawer.cancel();

		// Verify no meeting record 
		sugar().meetings.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}