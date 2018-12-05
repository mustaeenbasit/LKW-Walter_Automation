package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.AccountRecord;

public class Accounts_update extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	@Test
	public void Accounts_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
//		newData.put("name", "International Business Machines, Inc.");
		newData.put("website", "http://www.ibm.com/");

		// Edit the account using the UI.
		myAccount.edit(newData);
		
		// Verify the account was edited.
		myAccount.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}