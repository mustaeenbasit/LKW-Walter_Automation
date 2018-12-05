package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.records.AccountRecord;


public class Accounts_17070 extends SugarTest {
	AccountRecord testAcc;
	FieldSet testTempData;
	
	public void setup() throws Exception {
		testTempData = testData.get("Accounts_17070").get(0);
		sugar().login();
		testAcc = (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * Verify user can cancel the inline edit changes of the fields in the record view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17070_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.setEditFields(1, testTempData);
		sugar().accounts.listView.cancelRecord(1);
		
		// Assert the original default name value is present
		sugar().accounts.listView.verifyField(1, "name", testAcc.get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}