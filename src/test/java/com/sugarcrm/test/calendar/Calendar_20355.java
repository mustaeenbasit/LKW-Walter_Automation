package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calendar_20355 extends SugarTest {

	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify a scheduled call record is displayed in "Activities" sub-panel of "Account" detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20355_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab & Schedule Call
		sugar.navbar.selectMenuItem(sugar.calendar, "scheduleCall");
		// Enter all the Mandatory fields
		sugar.calls.createDrawer.getEditField("name").set(testName);
		// Select "Account" from drop down list,
		sugar.calls.createDrawer.getEditField("relatedToParentName").set(sugar.accounts.getDefaultData().get("name"));
		sugar.calls.createDrawer.save();

		// Go to "Account" detail view page.
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// Verify the scheduled call record is displayed in "Calls" sub-panel.
		StandardSubpanel callsSubpanel = sugar.accounts.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		callsSubpanel.expandSubpanel();
		callsSubpanel.assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}