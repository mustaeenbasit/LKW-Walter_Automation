package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Targets_28001 extends SugarTest {

	public void setup() throws Exception {
		sugar().targets.api.create();
		sugar().login();
	}

	/**
	 * Verify that "View Change Log" isn't available in Target record action list
	 * @throws Exception
	 */
	@Test
	public void Targets_28001_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-691
		// Verify that "View Change Log" isn't in the list.
		FieldSet fs = testData.get(testName).get(0);
		new VoodooControl("li", "xpath", "//*[@class='dropdown-menu']/li[contains(.,'"+fs.get("action_drop_down")+"')]").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
