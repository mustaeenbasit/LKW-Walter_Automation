package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;

public class Targets_19359 extends SugarTest {
	public void setup() throws Exception {
		sugar().targets.api.create();
		sugar().login();
	}

	/**
	 * Target - Delete Target_Verify that the "Delete" function in the target detail view works correctly.
	 */
	@Test
	public void Targets_19359_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// View record from navigation shortcut
		sugar().navbar.selectMenuItem(sugar().targets, "viewtargets");
		sugar().alerts.waitForLoadingExpiration();
		sugar().targets.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().targets.recordView.delete();
		sugar().alerts.getAlert().confirmAlert();
		sugar().alerts.waitForLoadingExpiration();

		// Verify target record is removed from list view
		sugar().targets.listView.assertIsEmpty();
		VoodooUtils.voodoo.log.info(testName + " complete. ");
	}

	public void cleanup() throws Exception {}
}
