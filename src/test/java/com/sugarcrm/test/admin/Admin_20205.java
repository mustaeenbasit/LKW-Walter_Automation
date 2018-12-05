package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20205 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().login();
	}

	/**
	 * Update singular name of one module and check Account module list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20205_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// Goto 'Rename Module' in Admin and update string "Account" as "Acct" then save
		sugar().admin.renameModule(sugar().accounts, ds.get(0).get("singularLabel"), sugar().accounts.moduleNamePlural);
		
		// Check action list of "Accounts" module. Verify "View Acct Reports" and ""Create Acct"
		sugar().accounts.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().accounts);
		sugar().accounts.menu.getControl("createAccount").assertElementContains(ds.get(0).get("assert1"), true);
		sugar().accounts.menu.getControl("viewAccountReports").assertElementContains(ds.get(0).get("assert2"), true);
		
		// Check that Create Account from shortcut menu shows "Create Acct"
		sugar().navbar.openQuickCreateMenu();
		sugar().navbar.quickCreate.assertElementContains(ds.get(0).get("assert1"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
