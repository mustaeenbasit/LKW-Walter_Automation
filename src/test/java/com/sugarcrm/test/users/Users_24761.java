package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24761 extends SugarTest { 
	UserRecord chris;
	VoodooControl showOnEmployees;

	public void setup() throws Exception {
		sugar().login();
		
		// Create new user
		chris = (UserRecord) sugar().users.create();
		
		// Set user's corresponding employee record to hidden in employees module
		sugar().admin.navToAdminPanelLink("userManagement");
		sugar().users.listView.clickRecord(1);
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1065
		showOnEmployees = new VoodooControl("input", "id", "show_on_employees");
		showOnEmployees.set("false");
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
	}

	/**
	 * Set display employee record when editing a user
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24761_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Users Management
		sugar().admin.navToAdminPanelLink("userManagement");
		sugar().users.listView.clickRecord(1);
		
		// Open the edit page of the user record
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Check "Display Employee Record" checkbox
		// TODO: VOOD-1065
		showOnEmployees.set("true");
		VoodooUtils.focusDefault();
		
		// Save the new user
		sugar().users.editView.save();
		
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