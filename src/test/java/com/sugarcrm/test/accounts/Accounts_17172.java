package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.ArrayList;

public class Accounts_17172 extends SugarTest {
	ArrayList<Record> myAccounts;
	DataSource customDs;
	
	public void setup() throws Exception {
		customDs = testData.get(testName);
		
		// create accounts
		myAccounts = sugar().accounts.api.create(customDs);
		sugar().login();
	}

	/**
	 * Verify display the recently viewed record in the mega menu
	 * @throws Exception
	 */
	@Test
	public void Accounts_17172_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click to view created all accounts
		for(Record createdRecord : myAccounts) {	
			createdRecord.navToRecord();
		}
		
		sugar().accounts.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().accounts);
		
		// TODO VOOD-508 need lib support for Recently Viewed container in the navbar
		// The newly created account record and recently viewed record should be displayed as recently viewed under mega menu of Accounts. 
		for(int i = customDs.size()-1; i == 0; i--) {
			if(i == 3)
				// The max number of the most recently viewed is 3, so that fourth record is not visible and assertExists = false
				new VoodooControl("a", "xpath", "//ul[@role='menu']//a[.=contains(.,'" + customDs.get(i) + "')]").assertExists(false);
			else
				new VoodooControl("a", "xpath", "//ul[@role='menu']//a[.=contains(.,'" + customDs.get(i) + "')]").assertExists(true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}