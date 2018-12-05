package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21136 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();		
	}

	/**
	 * Verify creating call with duration 0 min is allowed
	 * @throws Exception
	 */
	@Test
	public void Calls_21136_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// start and end time same
		String time = testData.get(testName).get(0).get("time");
		sugar.navbar.selectMenuItem(sugar.calls, "create" + sugar.calls.moduleNameSingular);
		sugar.alerts.waitForLoadingExpiration();
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.getEditField("date_start_time").set(time);
		sugar.calls.createDrawer.getEditField("date_end_time").set(time);
		sugar.calls.createDrawer.save();

		// Verify call record is saved
		sugar.alerts.getSuccess().assertVisible(true);
		sugar.calls.listView.verifyField(1, "name", testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 