package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24721  extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  User-Group User_Verify that group user cannot login SugarCRM
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24721_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().users.navToListView();
		sugar().navbar.selectMenuItem(sugar().users, "createGroupUser");
		VoodooUtils.focusFrame("bwc-frame"); 
		FieldSet customData = testData.get(testName).get(0);

		// Verify that there is no password area
		// TODO: VOOD-1053
		new VoodooControl("div", "id", "EditView_tabs").assertContains(customData.get("passwordField"), false);

		// Cancel
		new VoodooControl("input", "id", "CANCEL_HEADER").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}