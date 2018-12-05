package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_22881 extends SugarTest {
	AccountRecord acc1;
		
	public void setup() throws Exception {
		sugar().login();

		// Create account record with default data
		acc1 = (AccountRecord) sugar().accounts.api.create();
	}
	
	/**
	 * Test case 22881: Delete Account_Verify that deleting an account information is canceled
	 * @throws Exception
	 * 
	 */
	@Test
	public void Accounts_22881_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		// Go to created account record view and click 'delete' from action dropdown
		acc1.navToRecord();
		sugar().accounts.recordView.delete();
		
		// Click 'Cancel'
		sugar().alerts.getAlert().cancelAlert();
		
		// Verify that account is not deleted and displayed on list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", acc1.getRecordIdentifier());
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}