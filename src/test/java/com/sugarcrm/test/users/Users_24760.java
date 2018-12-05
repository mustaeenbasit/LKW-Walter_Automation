package com.sugarcrm.test.users;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24760 extends SugarTest { 
	UserRecord chris;

	public void setup() throws Exception {
		sugar().login();
		
		// Create new user (By default "Display Employee Record" checkbox is checked)
		chris = (UserRecord) sugar().users.create();
	}

	/**
	 * Set display employee record when creating a new user
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24760_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Nav to newly created user record and verify "Display Employee Record" checkbox is checked
		sugar().admin.navToAdminPanelLink("userManagement");
		sugar().users.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1065
		Assert.assertEquals("Display Employee Record is set to false", true, new VoodooControl("input", "id", "show_on_employees").isChecked());
		VoodooUtils.focusDefault();
		
		// Go to Employees
		// TODO: VOOD-1041
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify the new employee shows in Employees list
		// TODO: VOOD-1041
		new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr/td[contains(.,'"+chris.get("userName")+"')]/b/a").assertExists(true);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}