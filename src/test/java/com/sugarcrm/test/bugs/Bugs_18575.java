package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;

public class Bugs_18575 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * 
	 * Verify that "Report Bug" page can be displayed when clicking "Report Bug"
	 * in navigation shortcuts.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18575_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.selectMenuItem(sugar.bugs, "createBug");
		sugar.bugs.createDrawer.getEditField("name").assertExists(true);
		sugar.bugs.createDrawer.getEditField("name").set("Test Case 18575");
		sugar.bugs.createDrawer.save();
		
		sugar.bugs.listView.verifyField(1, "name", "Test Case 18575");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}