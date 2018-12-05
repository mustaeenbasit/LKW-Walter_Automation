package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20236 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable portal in admin
		sugar().admin.portalSetup.enablePortal();
	}

	/**
	 * Verify the support portal key is read-only and cannot be edited in OAuth Keys
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20236_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("oauth").click();
		VoodooUtils.focusFrame("bwc-frame");

		// When Portal has been enabled the 'Portal OAuth key' will be the first in the list
		// TODO: VOOD-648 - Admin Oauth LIB support: for all VoodooControls
		VoodooControl OAuthFirstRow = new VoodooControl("a", "css", ".list.view tr:nth-child(3) td:nth-child(3) a");
		String OAuthFirstRowText = "OAuth Support Portal Key";
		OAuthFirstRow.assertContains(OAuthFirstRowText, true);

		// Verify that no edit icon (pencil icon) is shown next to "OAuth Support Portal Key" in the list view
		new VoodooControl("a", "css", ".list.view tr:nth-child(3) td:nth-child(2) .quickEdit").assertExists(false);

		// Verify that only Delete button is shown on the action menu in the detail view
		OAuthFirstRow.click();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-648
		new VoodooControl("a", "id", "delete_button").assertExists(true);
		new VoodooControl("a", "id", "edit_button").assertExists(false);
		new VoodooControl("a", "id", "duplicate_button").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}