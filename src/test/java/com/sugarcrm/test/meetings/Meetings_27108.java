package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27108 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that remove Dashlet and Dashboard in Meeting
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27108_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Navigate to Meetings module 
		sugar().navbar.navToModule(sugar().meetings.moduleNamePlural);

		// Create a Dashboard
		sugar().meetings.dashboard.clickCreate();
		sugar().meetings.dashboard.getControl("title").set(testName);
		sugar().meetings.dashboard.getControl("addRow").click();
		sugar().meetings.dashboard.addDashlet(1, 1);

		// Add a Meeting listview Dashlet. Save it.
		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']").set(fs.get("dashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		VoodooUtils.waitForReady();
		sugar().meetings.dashboard.save();

		// In "My Meetings" Dashlet, click on toggle icon select Remove.
		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("i", "css", ".dashlet-header .btn-group button i").click();
		new VoodooControl("a", "css", ".dashlet-toolbar [data-dashletaction='removeClicked']").click();

		// Verify A message bar asking to confirm or cancel.
		sugar().alerts.getWarning().assertContains(fs.get("assert_string1"), true);
		sugar().alerts.getWarning().confirmAlert();

		// Delete Dashboard.
		sugar().meetings.dashboard.edit();
		sugar().meetings.dashboard.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Assert that "Dashboard" is removed. 
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().assertContains(fs.get("assert_string2"), true);
		}

		// Also verifying that deleted dash board is no longer visible
		sugar().meetings.dashboard.getControl("dashboardTitle").assertContains(testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}