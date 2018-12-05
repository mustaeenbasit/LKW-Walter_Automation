package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22874 extends SugarTest {	
	public void createWithoutSave(FieldSet fs) throws Exception {
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.setFields(fs);
		sugar().accounts.createDrawer.cancel();
	} 
		
	public void setup() throws Exception {
		sugar().login();
	}
	
	/**
	 * Test Case 22874: Create Account_Verify that creating a new account is canceled using cancel function 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22874_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		DataSource ds = testData.get(testName);
		FieldSet accountData = ds.get(0);	
		
		createWithoutSave(accountData);
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
