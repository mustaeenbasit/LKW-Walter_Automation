package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_17822 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		roleRecord = testData.get("env_role_setup").get(0);
		sugar().login();

		// Create role => Accounts => Access=Disabled
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 - Create a Roles (ACL) Module LIB
		// TODO: VOOD-856 - Lib is needed for Roles Management
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_access").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_access div select").set("Disabled");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify user not able to view and edit related-to records if he/she does not have access to related-to record
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_17822_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Log Call
		sugar().navbar.selectMenuItem(sugar().calls, "create"+sugar().calls.moduleNameSingular);

		sugar().meetings.createDrawer.getEditField("relatedToParentType").click();
		VoodooUtils.waitForReady();

		// Verify that under "Related to" field, "Account" is not exist
		// TODO: VOOD-1488 - Support Flex "Related to" control (dropdown+field) for all layouts (views and filters)
		new VoodooControl("ul", "css", ".select2-results").assertContains(sugar().accounts.moduleNameSingular, false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}