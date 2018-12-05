package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_26517 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify admin able to access Portal theme successfully
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_26517_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-799 - Sugar Portal Theme LIB support
		new VoodooControl("a", "id", "sugarportal").click();
		// TODO VOOD-799 - Sugar Portal Theme LIB support
		new VoodooControl("a", "css", "td#SPUploadCSS a").click();
		// Verify the "Restore Default Theme" button exist
		new VoodooControl("button", "name", "reset_button").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
