package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Targets_19357 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Target - Create Target_Verify that target can be created when using "Create Target" function in navigation shortcuts sub-panel.
	 */
	@Test
	public void Targets_19357_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Target record from navigation shortcut
		sugar().navbar.selectMenuItem(sugar().targets, "createTarget");
		sugar().alerts.waitForLoadingExpiration();
		sugar().targets.createDrawer.getEditField("lastName").set(sugar().targets.getDefaultData().get("lastName"));
		sugar().targets.createDrawer.showMore(); // a hot fix to make save button visible as open/close dashboard tooltip appearing above save button (only appearing on MAC/Ubuntu machine)
		sugar().targets.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1149
		// Verify target record
		// sugar().targets.listView.verifyField(1, "lastName", sugar().targets.getDefaultData().get("lastName"));
		new VoodooControl("a", "css", ".fld_full_name.list a").assertEquals(sugar().targets.getDefaultData().get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete. ");
	}

	public void cleanup() throws Exception {}
}
