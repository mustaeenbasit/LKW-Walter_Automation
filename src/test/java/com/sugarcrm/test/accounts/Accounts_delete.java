package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import static org.junit.Assert.assertEquals;

public class Accounts_delete extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	@Test
	public void Accounts_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the account using the UI.
		myAccount.delete();
		
		// Verify the account was deleted.
		sugar().accounts.navToListView();
		assertEquals(VoodooUtils.contains(myAccount.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
