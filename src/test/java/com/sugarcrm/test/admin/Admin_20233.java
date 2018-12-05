package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20233 extends SugarTest {
	// Common VoodooControl references
	// TODO VOOD-648 - Admin Oauth LIB support: for all VoodooControls
	VoodooControl OAuthFirstRow;

	public void setup() throws Exception {
		// Initialize the common Control references
		OAuthFirstRow = new VoodooControl("a", "css", "tr.oddListRowS1 a");
		sugar().login();
	}

	/**
	 * Verify the standard OAuth key is generated after login
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20233_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "oauth").click();
		VoodooUtils.focusFrame("bwc-frame");
		// The standard OAuth key to verify will be the first in the list
		OAuthFirstRow.assertContains("Standard OAuth Username & Password Key",
				true);
		OAuthFirstRow.click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "id", "c_key").assertEquals("sugar", true);
		new VoodooControl("td", "css",
				"#detailpanel_1 table tbody tr:nth-of-type(1) td:nth-of-type(4)")
				.assertContains("OAuth 2.0", true);
		new VoodooControl("td", "css",
				"#detailpanel_1 table tbody tr:nth-of-type(2) td:nth-of-type(4)")
				.assertContains("Sugar User", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
