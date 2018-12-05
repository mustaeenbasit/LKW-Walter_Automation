package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class ListView_16987 extends SugarTest {
	FieldSet roleRecord;
	public void setup() throws Exception {
		sugar.accounts.api.create();
		roleRecord = testData.get(testName+"_roledata").get(0);
		sugar.login();
		
		// Create and Save the Role 
		AdminModule.createRole(roleRecord);
		
		// Set the Accounts Access edit cell to None
		// TODO: VOOD-856
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_edit div select").set("None");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		
		// Assign role to qauser
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		
		sugar.logout();
	}

	/**
	 * Verify Edit button (inline) not available when Edit = none 
	 * (restricts users from editing or creating records)
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_16987_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.login(sugar.users.getQAUser());

		// Assert 'Create Account' isn't visible the Accounts Drop Down
		sugar.navbar.clickModuleDropdown(sugar.accounts);
		sugar.accounts.menu.getControl("createAccount").assertVisible(false);
		
		// Assert 'Edit' isn't visible in list/detail view
		sugar.accounts.navToListView();
		sugar.accounts.listView.openRowActionDropdown(1);
		sugar.accounts.listView.getControl("edit01").assertVisible(false);
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.getControl("editButton").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}