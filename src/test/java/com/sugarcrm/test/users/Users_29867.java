package com.sugarcrm.test.users;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_29867 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that user should not get any acknowledgment message while select or deselect the Roles,
	 * through user profile.
	 * @throws Exception
	 */
	@Test
	public void Users_29867_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create New User
		sugar().users.create();
		VoodooUtils.focusFrame("bwc-frame");

		// Navigate to Access tab
		// TODO: VOOD-563
		VoodooControl accessTab = new VoodooControl("a", "id", "tab3");
		accessTab.click();
		VoodooUtils.waitForReady();

		// Link a Role with the user
		// TODO: VOOD-1643
		new VoodooControl("a", "id", "acl_roles_users_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", ".list.view tr:nth-child(3) td input").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify acknowledge message regarding "Password Updated" is not displayed after Linking Role
		Assert.assertFalse("Acknowledgement Message is visible after linking Role", VoodooUtils.isDialogVisible());

		// Navigate back to Access Tab
		accessTab.click();
		VoodooUtils.waitForReady();

		// Unlink the Role Selected above
		// TODO: VOOD-1643
		new VoodooControl("span", "css", "#list_subpanel_aclroles .sugar_action_button .ab").click();
		new VoodooControl("a", "id", "aclroles_remove_1").click();

		// Accept Dialog for unlinking the Role with this User Record
		VoodooUtils.acceptDialog();

		// Verify acknowledge message regarding "Password Updated" is not displayed after unlinking Role
		Assert.assertFalse("Acknowledgement Message is visible after unlinking Role", VoodooUtils.isDialogVisible());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}