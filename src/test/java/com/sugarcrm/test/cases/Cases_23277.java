package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23277 extends SugarTest {

	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Create Note_Verify that related note can be created in "Notes & Attachments" dashlet
	 * @throws Exception
	 */
	@Test
	public void Cases_23277_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet recordSet = testData.get(testName).get(0);

		// Navigate to Case Detail view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Create new dashboard i.e Notes Dashboard
		sugar().cases.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(recordSet.get("dashboardName"));

		// Add new dashlet inside "Notes Dashboard"
		sugar().cases.dashboard.addRow();
		sugar().cases.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 - Dashlet selection
		// Select "Notes & Attachments" in dashlets list to add inside "Notes Dashboard"
		new VoodooControl("input", "css", ".inline-drawer-header input").set(recordSet.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".fld_title a").click();
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save Dashboard
		sugar().contacts.dashboard.save();

		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("a", "css", ".dashboard-pane .dashlet-toolbar.btn-group a").click();
		new VoodooControl("a", "css", ".dropdown-menu .dashlet-toolbar a").click();
		VoodooUtils.waitForReady();
		sugar().notes.createDrawer.getEditField("subject").set(sugar().notes.defaultData.get("subject"));
		sugar().notes.createDrawer.save();
		VoodooUtils.waitForReady();

		// Verify that a note record is listed.
		new VoodooControl("div", "css", "[data-voodoo-name='attachments']").assertContains(sugar().notes.defaultData.get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}