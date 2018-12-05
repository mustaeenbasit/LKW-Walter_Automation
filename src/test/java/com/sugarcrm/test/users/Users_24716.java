package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24716  extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  User-Admin_Verify that admin privilege user can create custom query.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24716_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Reports module, Click Manage Advanced Reports link in shortcut navigation.
		sugar().navbar.selectMenuItem(sugar().reports, "manageAdvancedReports");

		// Navigate to "Advanced Reports" dropdown from navbar
		// TODO: VOOD-1057
		new VoodooControl("i", "css", ".dropdown.active[data-module='ReportMaker'] .fa.fa-caret-down").click();

		// Verify "Create Custom Query" link is displayed in shortcut navigation under "Advanced Reports" tab.
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}