package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_23249 extends SugarTest {
	
	public void setup() throws Exception {
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
		
		// add email ID
		FieldSet fs = new FieldSet();
		fs.put("emailAddress", sugar().accounts.getDefaultData().get("emailAddress"));
		myAccount.edit(fs);
	}
	
	/**
	 * Verify record with email can be saved after emptying the value of email field
	 * @throws Exception
	 */
	@Test
	public void Accounts_23249_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		
		// TODO: VOOD-707 
		new VoodooControl("a", "css", "a.btn.removeEmail").click();
		sugar().accounts.recordView.save();

		// Verify that the record is saved successful without email.
		sugar().accounts.recordView.getDetailField("emailAddress").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}