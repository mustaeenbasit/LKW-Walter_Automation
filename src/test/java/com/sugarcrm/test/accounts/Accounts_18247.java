package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_18247 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that a record is able to be selected from "Search and Selected" list
	 * @throws Exception
	 */
	@Test
	public void Accounts_18247_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Searching account record in Global Search 
		String accountName = sugar().accounts.getDefaultData().get("name");
		String accountFirstName = accountName.substring(0, 8);
		sugar().navbar.getControl("globalSearch").set(accountFirstName + '\uE007');
		VoodooUtils.waitForReady();

		// Asserting correct account record in visible in Global Search result window.
		sugar().globalSearch.getRow(1).assertContains(accountName, true);

		// Clicking on account record in Global Search window 
		sugar().globalSearch.clickRecord(1);
		sugar().accounts.recordView.assertVisible(true);
		sugar().accounts.recordView.getDetailField("name").assertEquals(accountName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}