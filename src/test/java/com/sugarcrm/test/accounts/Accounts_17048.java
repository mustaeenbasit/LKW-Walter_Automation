package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Accounts_17048 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// account record -> assignedTo -> qauser
		FieldSet account2 = new FieldSet();
		account2.put("name", testName);
		account2.put("relAssignedTo", roleRecord.get("userName"));
		sugar().accounts.create(account2);

		// Create role => Accounts -> View access (None)
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "td#ACLEditView_Access_Accounts_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_view div select").set(roleRecord.get("roleName"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Shouldn't show preview action of a record on list view if the user's view access is none
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17048_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();

		// Verify shouldn't show preview action (assignedTo => qauser)
		sugar().accounts.listView.getControl("preview01").assertVisible(false);

		// Verify shouldn't show preview action (assignedTo => other users)
		sugar().accounts.listView.getControl("preview02").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}