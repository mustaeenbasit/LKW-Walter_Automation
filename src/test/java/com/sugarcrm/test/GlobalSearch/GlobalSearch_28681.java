package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class GlobalSearch_28681 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		sugar().login();

		// Go to Admin > role management > Create role > Name the role and save it
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Set Accounts module "Access" Level to "Disabled"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_access").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_access div select").set(testData.get(testName).get(0).get("access_type"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify global search module dropdown list when a role is applied
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28681_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.getControl("globalSearch").click();
		VoodooUtils.waitForReady();

		// Verify no Accounts module appears on search dropdown
		try {
			sugar().navbar.search.getControl("searchModuleDropdown").click();
			sugar().navbar.search.getControl("searchAccounts").assertVisible(false);
		}
		finally {
			// Better to close global search bar
			sugar().navbar.search.getControl("cancelSearch").click();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}