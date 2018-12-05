package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_22877 extends SugarTest {
	AccountRecord acc1;
	
	public void setup() throws Exception {		
		sugar().login();
		// Create account record with default data
		acc1 = (AccountRecord) sugar().accounts.api.create();
	}
	
	/**
	 * Test Case 22877: Create Account - Copy_Verify that editing a duplicate account record is canceled.
	 * @throws Exception 
	 * 
	 */
	@Test
	public void Accounts_22877_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);		
		
		// Go to created account record view and click copy from action dropdown
		acc1.navToRecord();
		sugar().accounts.recordView.copy();
		
		// Populate with custom data, then click cancel
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.setFields(ds.get(0));
		sugar().accounts.createDrawer.cancel();
		
		// Verify that modified account is not displayed on list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.setSearchString(testName);
		sugar().accounts.listView.getControl("emptyListViewMsg").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
