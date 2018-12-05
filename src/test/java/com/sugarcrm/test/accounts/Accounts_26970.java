package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_26970 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify only one error message is shown while saving a new record without any input
	 * @throws Exception
	 */
	@Test
	public void Accounts_26970_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet messageData = testData.get(testName).get(0);

		// Go to accounts module
		sugar().accounts.navToListView();

		// Click Create to create a new account
		sugar().accounts.listView.create();

		// Click Save
		sugar().accounts.createDrawer.save();

		// Verify only one error message: "Error Please resolve any errors before proceeding." shown on the page.
		sugar().alerts.getError().assertContains(messageData.get("errorMessage"), true);

		// Click on Cancel button
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}