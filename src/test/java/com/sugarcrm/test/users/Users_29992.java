package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_29992 extends SugarTest {
	UserRecord chrisUser;

	public void setup() throws Exception {
		// Create a new user
		chrisUser = (UserRecord) sugar().users.api.create();
		
		// Login as admin
		sugar().login();
	}

	/**
	 * Verify that After selecting a Role in Users profile section, Wrong
	 * message is not showing
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29992_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Nav to User record
		chrisUser.navToRecord();
		// Goto User record views Access tab
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "tab3").click();
		VoodooUtils.waitForReady();

		// Add a role
		new VoodooControl("a", "id", "acl_roles_users_select_button").click();
		VoodooUtils.focusWindow(1);

		// Select Role - Sales Administrator
		new VoodooControl("input", "xpath","//table[@class='list view']//tr[contains(.,'Sales')]//input").set("true");
		new VoodooControl("input", "id", "MassUpdate_select_button").click();

		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForReady();

		// Verify that no message is displayed
		new VoodooControl("div", "css", "#alerts .alert").assertExists(false);

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Role is added successfully
		new VoodooControl("a", "id", "tab3").click();
		VoodooUtils.waitForReady();

		new VoodooControl("a", "css", "div#list_subpanel_aclroles table.list.view tr.oddListRowS1 td:not(.inlineButtons) a")
				.assertContains("Sales Administrator", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}