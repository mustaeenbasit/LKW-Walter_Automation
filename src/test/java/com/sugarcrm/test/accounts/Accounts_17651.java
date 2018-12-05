package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17651 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * should back to the list view after saving a new record
	 * record
	 * @throws Exception
	 */
	@Test
	public void Accounts_17651_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		sugar().navbar.clickModuleDropdown(sugar().accounts);
		sugar().accounts.menu.getControl("createAccount").click();
		FieldSet fs = testData.get(testName).get(0);
		sugar().accounts.createDrawer.getEditField("name").set(fs.get("name"));
		sugar().accounts.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.getControl("filterDropdown").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}