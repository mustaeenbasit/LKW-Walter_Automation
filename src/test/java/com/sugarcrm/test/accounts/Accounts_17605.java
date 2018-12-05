package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17605 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * Verify not see a success saved message while clicking on the favorite icon on record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17605_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		sugar().accounts.recordView.toggleFavorite();
		sugar().alerts.getSuccess().assertExists(false);
		sugar().accounts.recordView.getControl("favoriteButton").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}