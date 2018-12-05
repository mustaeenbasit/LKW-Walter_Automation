package com.sugarcrm.test.dashlets;

import com.sugarcrm.candybean.datasource.FieldSet;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlet_29790 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that 'Dashboard' title and 'Create' button should not be disappeared while creating a 'Dashboard'
	 *
	 * @throws Exception
	 */
	@Test
	public void Dashlet_29790_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);
		// Navigating to account module
		sugar().accounts.navToListView();

		// Creating dashboard in listview of account
		sugar().accounts.dashboard.clickCreate();

		// Adding row two times
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960: Dashlet selection
		// Adding Forecast pipeline dashlet
		new VoodooControl("input", "css", "[data-voodoo-name='filtered-search'] input").set(fs.get("dashlet_title"));
		new VoodooControl("a", "css", "[data-voodoo-name='title'] a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();

		// Click on save without dashboard name
		sugar().accounts.dashboard.getControl("save").click();

		// Verifying error is appearing 
		sugar().alerts.getError().assertExists(true);

		// Closing the alert
		sugar().alerts.getAlert().closeAlert();

		// Save the dashboard
		sugar().accounts.dashboard.getControl("title").set(testName);
		sugar().accounts.dashboard.save();

		// Verifying Dashboard title and 'Create' button is appearing properly
		sugar().accounts.dashboard.getControl("dashboardTitle").assertVisible(true);
		sugar().accounts.dashboard.getControl("dashboardTitle").assertEquals(testName, true);
		sugar().accounts.dashboard.getControl("create").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}