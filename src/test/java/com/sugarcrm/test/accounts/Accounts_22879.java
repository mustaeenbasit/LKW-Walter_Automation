package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_22879 extends SugarTest {
	AccountRecord acc1;
			
	public void setup() throws Exception {		
		sugar().login();
		// Create account record with default data
		acc1 = (AccountRecord) sugar().accounts.api.create();
	}
	
	/**
	 * TC 22879: Edit Account_Verify that editing account information is canceled.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void Accounts_22879_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);		
		 		
		// Go to created account record view and click 'edit' from action dropdown
		acc1.navToRecord();
		sugar().accounts.recordView.edit();
		 
		// Populate with custom data, then click cancel
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.setFields(ds.get(0));
		sugar().accounts.recordView.cancel();
		 
		// Verify that modified account is not displayed on list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.setSearchString(testName);
		sugar().accounts.listView.getControl("emptyListViewMsg").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
