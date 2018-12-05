package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24746 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();		
	}
	/**
	 * Create new inactive portal user-Verify that no reassign record pop-up window displays
	 * @throws Exception
	 */
	@Test
	public void Users_24746_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Admin -> User management
		sugar().users.navToListView();

		// Click on Create Group User
		sugar().navbar.selectMenuItem(sugar.users, "createPortalApiUser");
		VoodooUtils.focusFrame("bwc-frame");
		
		// Setting status as "inactive"; enter other required field and "Save"
		// TODO: VOOD-1053 (Need lib support of group user/ portal user's create/edit/delete) 
		new VoodooControl("div", "id", "EditViewGroup_tabs").waitForVisible();
		new VoodooControl("input", "id", "user_name").set(testName);
		new VoodooControl("input", "id", "last_name").set(testName);
		new VoodooControl("select", "id", "status").set(customData.get("activity"));
		
		sugar().users.editView.getControl("passwordTab").click();
		new VoodooControl("input", "id", "new_password").set(sugar().users.getDefaultData().get("password"));
		new VoodooControl("input", "id", "confirm_pwd").set(sugar().users.getDefaultData().get("confirmPassword"));
		
		// Saving the created Portal API User
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		
		// Closing the Password Updated window
		sugar().users.editView.getControl("confirmCreate").click(); 
		
		// Verify that there is no reassign record pop-up window displayed
		new VoodooControl("div", "css", "#popup_window .yui-dialog").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}