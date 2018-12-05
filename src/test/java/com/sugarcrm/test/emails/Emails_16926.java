package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.AccountRecord;

public class Emails_16926 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar.login();
		myAccount = (AccountRecord)sugar.accounts.create();
	}

	@Test
	public void Emails_16926_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("emailAddress", "sugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugar@sugarc.com");

		// Delete the account using the UI.
		myAccount.edit(newData);
		
		// Here is a substring of email address, because the email address is truncated if it is longer than 100 characters.
		newData.put("emailAddress", "sugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugarsugar@sugarc.co");
		// Verify the account was edited.
		myAccount.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
