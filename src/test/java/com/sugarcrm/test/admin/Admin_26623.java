package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_26623 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that shipping provider can be deleted successfully from the Shipping Providers list view 
	 * @throws Exception
	 */
	@Test
	public void Admin_26623_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Admin > Product and Quotes > Shipping Providers
		// TODO: VOOD-1195
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "shipping_providers").click();
		sugar().alerts.waitForLoadingExpiration();

		// Create a Shipping Provider
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "input[name=New]").click();
		new VoodooControl("input", "css", "input[name=name]").set(testName);
		new VoodooControl("input", "css", "input[title=Save]").click();

		// Delete the created Shipping Provider
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-of-type(4) ul a").click();
		VoodooUtils.acceptDialog();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that shipping provider can be deleted successfully from the Shipping Providers list view 
		new VoodooControl("a", "css", ".oddListRowS1").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}