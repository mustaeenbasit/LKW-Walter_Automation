package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21064 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that a call is not logged by edit view when mandatory fields are not entered.
	 * @throws Exception
	 */
	@Test
	public void Calls_21064_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar.navbar.selectMenuItem(sugar.calls, "create" + sugar.calls.moduleNameSingular);	
		sugar.calls.createDrawer.save();

		// Verify error message while not entering required fields
		sugar.alerts.getError().assertContains(testData.get(testName).get(0).get("error_msg"), true);
		sugar.alerts.getError().closeAlert();
		sugar.calls.createDrawer.cancel();

		// Verify call is not logged
		sugar.calls.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}