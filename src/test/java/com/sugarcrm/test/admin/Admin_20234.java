package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20234 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the out-of-box OAuth key in the list view after the install
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20234_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-648 - Admin Oauth LIB support: for all VoodooControls
		new VoodooControl("a", "id", "oauth").click();
		VoodooUtils.focusFrame("bwc-frame");
		// The SNIPOAuthKey key to verify will be the Second in the list
		new VoodooControl("a", "css", "tr.evenListRowS1 a").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").assertEquals("SNIPOAuthKey",
				true);
		new VoodooControl("span", "id", "c_key").assertEquals("SNIPOAuthKey",
				true);
		new VoodooControl("td", "css",
				"#detailpanel_1 table tbody tr:nth-of-type(1) td:nth-of-type(4)")
				.assertContains("OAuth 1.0", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
