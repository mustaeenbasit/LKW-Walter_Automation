package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21229 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21229").get(0);
		sugar().login();
		sugar().accounts.api.create();
		AdminModule.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and
		// Description have been Saved
	}

	/**
	 * Verify user cannot mass update the records while the Mass Update role is
	 * set to None
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21229_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set the Accounts Mass Update cell to None
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_massupdate div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_massupdate div select")
				.set("None");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as QAuser to verify there is no
		// Mass Update option
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		// Check the Actions box and click the drop down to verify the options
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		// TODO VOOD-657
		new VoodooControl("a", "css", ".fld_addtolist_button.list a")
				.assertVisible(true);
		new VoodooControl("a", "css", ".fld_merge_button.list a")
				.assertVisible(true);
		sugar().accounts.listView.getControl("exportButton").assertVisible(true);
		sugar().accounts.listView.getControl("deleteButton").assertVisible(true);
		sugar().accounts.listView.getControl("massUpdateButton").assertVisible(
				false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
