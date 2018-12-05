package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20237 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the Standard OAuth Username & Password Key is read-only and cannot be edited
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20237_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("oauth").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-648 - Admin Oauth LIB support: for all VoodooControls
		VoodooControl OAuthFirstRow = new VoodooControl("a", "css", "tr.oddListRowS1 a");
		String OAuthFirstRowText = "Standard OAuth Username & Password Key";
		OAuthFirstRow.assertContains(OAuthFirstRowText, true);

		// Verify that no edit icon (pencil icon) is shown next to "Standard OAuth Username & Password Key" in the list view
		new VoodooControl("a", "css", "tr.oddListRowS1 a.quickEdit").assertExists(false);

		// Verify that only Delete button is shown on the action menu in the detail view
		OAuthFirstRow.click();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-648
		new VoodooControl("a", "id", "delete_button").assertExists(true);
		new VoodooControl("a", "id", "edit_button").assertExists(false);
		new VoodooControl("a", "id", "duplicate_button").assertExists(false);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}