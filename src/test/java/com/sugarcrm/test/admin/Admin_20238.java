package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20238 extends SugarTest {
	// Common VoodooControl references
	// TODO VOOD-648 - Admin Oauth LIB support: for all VoodooControls
	VoodooControl clientType;

	public void setup() throws Exception {
		// Initialize the common Control references
		clientType = new VoodooControl("select", "id", "client_type");
		sugar().login();
	}

	/**
	 * Verify the client type is shown while OAuth version is 2.0
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20238_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "oauth").click();
		// Create an OAuth Key
		new VoodooControl("button", "css",
				"li[data-module='OAuthKeys'] button[data-toggle='dropdown']")
				.click();
		new VoodooControl("a", "css",
				"li[data-module='OAuthKeys'] ul[role='menu'] a:nth-of-type(1)")
				.click();
		VoodooUtils.focusFrame("bwc-frame");
		// The Client type should only be visible after the Version is set to
		// 2.0
		clientType.assertVisible(false);
		new VoodooControl("select", "id", "oauth_type").set("OAuth 2.0");
		clientType.assertVisible(true);
		clientType.assertContains("Sugar User", true);
		clientType.assertContains("Support Portal", true);
		// No need to create a record so Cancel
		new VoodooControl("input", "id", "CANCEL_HEADER").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
