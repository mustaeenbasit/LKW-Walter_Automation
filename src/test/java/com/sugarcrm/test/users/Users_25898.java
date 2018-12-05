package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_25898 extends SugarTest {
		
	public void setup() throws Exception {
		sugar.login();
		
		sugar.navbar.navToAdminTools();
	}

	/**
	 * 25898 Display Users module in Studio
	 * @throws Exception
	 */
	@Test
	public void Users_25898_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO VOOD-517
		new VoodooControl("a", "id", "studio").click();
		new VoodooControl("a", "id", "studiolink_Users").assertExists(true);
		
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}