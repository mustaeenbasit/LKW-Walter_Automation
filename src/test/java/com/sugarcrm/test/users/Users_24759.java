package com.sugarcrm.test.users;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24759 extends SugarTest { 
	UserRecord chris;

	public void setup() throws Exception {
		sugar().login();
		
		// Create new user (By default "Display Employee Record" checkbox is checked)
		chris = (UserRecord) sugar().users.create();
	}

	/**
	 * Verify that the new employee doesn't show in Employees list
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24759_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Nav to newly created user record and set "Display Employee Record" checkbox to false
		sugar().admin.navToAdminPanelLink("userManagement");
		sugar().users.listView.clickRecord(1);
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1065
		VoodooControl showOnEmployees = new VoodooControl("input", "id", "show_on_employees");
		showOnEmployees.set("false");
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that "Display Employee Record" checkbox set to false
		Assert.assertEquals("Display Employee Record is set to true", false, showOnEmployees.isChecked());
		VoodooUtils.focusDefault();
		
		// Go to Employees
		// TODO: VOOD-1041
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify the new employee doesn't show in Employees list
		// TODO: VOOD-1041
		new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'"+chris.get("userName")+"')]/td/b/a").assertExists(false);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}