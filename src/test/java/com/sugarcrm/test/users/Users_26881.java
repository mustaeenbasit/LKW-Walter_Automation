package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_26881 extends SugarTest {
	VoodooControl contractModuleCtrl;
		
	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
		sugar.contracts.api.create();
	}

	/**
	 * Verify only select user action is allowed in user subpanel when create a many-to-many relationship 
	 * between bwc module and users module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_26881_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Add a many-many relationship with Users
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		contractModuleCtrl = new VoodooControl("a", "id", "studiolink_Contracts");
		contractModuleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#rhs_mod_field option[value='Users']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// Go to Contract Record view
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that Select button is exist in Users subpanel.
		new VoodooControl("a", "id", "contracts_users_1_select_button").assertExists(true);
		
		// TODO: VOOD-1193
		// Click Select and select a user.
		new VoodooControl("a", "id", "contracts_users_1_select_button").click();		
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "last_name_advanced").set(sugar.users.getQAUser().get("userName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 > td:nth-child(3) > a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that user is listed in user subpanel.
		new VoodooControl("a", "css", "#list_subpanel_contracts_users_1 tr.oddListRowS1 td:nth-child(1) a").assertContains(sugar.users.getQAUser().get("userName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}