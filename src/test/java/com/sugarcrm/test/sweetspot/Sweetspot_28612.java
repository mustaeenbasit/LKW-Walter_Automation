package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28612 extends SugarTest {
	VoodooControl salesAdminCtrl;
	UserRecord jim;

	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-1200 - After resolved, we can use with default CSV data
		// Jim user
		FieldSet jimData = testData.get(testName+"_user").get(0);
		jim = (UserRecord)sugar().users.create(jimData);
		VoodooUtils.focusDefault();

		// In Admin > Role Management > Sales Admin, set Accounts > Type to Read-Only.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("rolesManagement").click();

		// Click on 'Sales Administrator'
		// TODO: VOOD-580
		VoodooUtils.focusFrame("bwc-frame");
		salesAdminCtrl = new VoodooControl("a", "css", "table.list.view tr:nth-of-type(6) td:nth-child(3) a");
		salesAdminCtrl.click();
		VoodooUtils.waitForReady();

		// Jim user associate with Sales administrator Role
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "acl_roles_users_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "user_name_advanced").set(jim.get("userName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-of-type(3) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Logout as Admin and login as partial admin: Jim 
		sugar().logout();
		sugar().login(jim);
	}

	/**
	 * Verify partial admin user cannot access admin actions through Sweet Spot
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28612_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to sweetSpot section
		FieldSet customDS = new FieldSet();
		customDS = testData.get(testName).get(0);
		sugar().sweetspot.show();

		// Try to search Sweet Spot for an admin action; e.g. Studio
		sugar().sweetspot.search(customDS.get("adminSetting"));

		// Verify that user cannot access any admin action through Sweet Spot
		sugar().sweetspot.getActionsResult().assertExists(false);

		// Go to Sweet Spot Config Drawer - try to add a hotkey for an admin action
		sugar().sweetspot.configure();
		sugar().sweetspot.getControl("hotkeysAction").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1836
		new VoodooControl("input", "css", ".select2-drop-active .select2-search input").set(customDS.get("adminSetting"));
		VoodooUtils.waitForReady();
		
		// Verify that the user cannot add a HotKey for any admin action
		new VoodooControl("li", "css", ".select2-drop-active ul .select2-no-results").assertContains(customDS.get("noMatchFound"),true);
		
		// Select default hotkey
		sugar().sweetspot.showHotkeysDropDownOptions(customDS.get("defaultHotkey"));
		sugar().sweetspot.clickHotkeysDropDownOption();
		sugar().sweetspot.cancelConfiguration();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}