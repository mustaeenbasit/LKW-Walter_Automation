package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24745 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();		
	}
	/**
	 * Create new inactive group user-Verify that no reassign record pop-up window displays
	 * @throws Exception
	 */
	@Test
	public void Users_24745_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Admin -> User management
		sugar().users.navToListView();

		// Click on Create Group User
		sugar().navbar.selectMenuItem(sugar.users, "createGroupUser");
		VoodooUtils.focusFrame("bwc-frame");
		
		// Setting status as "inactive";enter other required field and "Save"
		// TODO: VOOD-1053 (Need lib support of group user/ portal user's create/edit/delete) 
		new VoodooControl("div", "id", "EditViewGroup_tabs").waitForVisible();
		new VoodooControl("input", "id", "user_name").set(testName);
		new VoodooControl("input", "id", "last_name").set(testName);
		new VoodooControl("select", "id", "status").set(customData.get("activity"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		
		// Verify that there is no reassign record pop-up window displayed
		new VoodooControl("div", "css", "#popup_window .yui-dialog").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}