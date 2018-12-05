package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_20255 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify Navigation and Cancel from menu Quick Create Log Call
	 * @throws Exception
	 */
	@Test
	public void Calls_20255_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Initially on home dashboard
		sugar.home.dashboard.getControl("dashboardTitle").assertVisible(true);

		sugar.navbar.quickCreateAction(sugar.calls.moduleNamePlural);
		
		// Set any field for call and cancel
		sugar.calls.createDrawer.getEditField("name").set(sugar.calls.getDefaultData().get("name"));
		sugar.calls.createDrawer.cancel();

		// Verifying home dashboard is displayed on which working before and no call record is created
		sugar.home.dashboard.getControl("dashboardTitle").assertVisible(true);
		sugar.calls.navToListView();
		sugar.calls.listView.assertIsEmpty();
	}

	public void cleanup() throws Exception {}
}