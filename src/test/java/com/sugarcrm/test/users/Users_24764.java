package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24764 extends SugarTest {
	FieldSet passwordSetting = new FieldSet();
	
	public void setup() throws Exception {
		sugar().login();
		
		// Enable System-Generated Passwords Feature
		passwordSetting.put("SystemGeneratedPasswordCheckbox", "true");
		sugar().admin.passwordSettings(passwordSetting);
		passwordSetting.clear();
	}

	/**
	 * New action dropdown list in user detail view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24764_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to a user detail view page
		sugar().admin.navToAdminPanelLink("userManagement");
		// The script failed at CI at this line so it is suspected that probably due to some sorting order 
		// mismatch at runtime, qauser was not found at #1 row. So below replaced the line - sugar().users.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "xpath", "//table//tr[contains(.,'qauser')][@class!='']/td[3]//a").click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click the down arrow beside Edit action
		sugar().users.detailView.openPrimaryButtonDropdown();
		
		// Verify that all of the actions (Edit, Copy, Delete, Reset Password, Reset User Preference) shown in the dropdown list
		sugar().users.detailView.getControl("editButton").assertVisible(true);
		sugar().users.detailView.getControl("deleteButton").assertVisible(true);
		sugar().users.detailView.getControl("copyButton").assertVisible(true);
		
		// TODO: VOOD-563
		new VoodooControl("a", "css", "#detail_header_action_menu li ul li:nth-child(3) a").assertVisible(true);
		new VoodooControl("a", "id", "reset_user_preferences_header").assertVisible(true);
		
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}