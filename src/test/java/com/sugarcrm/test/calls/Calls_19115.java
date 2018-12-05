package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;


public class Calls_19115 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that Schedule bar shows ones on Calls when date is manually edited
	 * @throws Exception
	 */
	@Test
	public void Calls_19115_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		FieldSet customData = testData.get(testName).get(0);

		// Navigate to Calls module and open create drawer
		sugar.calls.navToListView();
		sugar.calls.listView.create();
		sugar.calls.createDrawer.getEditField("name").set(testName);

		// Verifying that Scheduler Panel exists
		sugar.calls.createDrawer.getControl("invitees").assertVisible(true);

		// Enter the new date in the Date fields without pop-up calendar
		sugar.calls.createDrawer.getEditField("date_start_date").set(customData.get("date_Start"));
		sugar.calls.createDrawer.getEditField("date_end_date").set(customData.get("date_End"));
		// verify schedulerPanel is present having administrator user 
		sugar.calls.createDrawer.getControl("invitees").assertContains(customData.get("assert_Name"), true);
		sugar.calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}