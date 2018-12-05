package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_29817 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Module name should roll back after rename it using XSS injection
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_29817_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		FieldSet customData = testData.get(testName).get(0);

		// Go to Admin > Rename Modules > XSS Injection > Save
		sugar().admin.renameModule(sugar().accounts, customData.get("singularLabel"), customData.get("pluralLabel"));

		// Rename it again as its original name as "Accounts" -> Save
		sugar().admin.renameModule(sugar().accounts, sugar().accounts.moduleNameSingular, sugar().accounts.moduleNamePlural);

		// Go to Contact module list view
		sugar().contacts.navToListView();

		// Verify the Account Name relate field should be appearing as "Account Name" in contact list view.
		sugar().contacts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("account_name"))).assertEquals(customData.get("columnName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}