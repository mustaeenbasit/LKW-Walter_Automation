package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.test.SugarTest;

public class Accounts_17436 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
	}

	/**
	 * Verify subpanel header contains "Associate Existing Record" action
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17436_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// Go to account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Create an array list of different modules
		ArrayList<StandardModule> modules = new ArrayList<StandardModule>();
		modules.add(sugar().accounts);
		modules.add(sugar().contacts);
		modules.add(sugar().leads);
		modules.add(sugar().notes);
		int i = 0;
		
		for(StandardModule mod : modules) {
			// Click on 'Link Existing Record'
			sugar().accounts.recordView.subpanels.get(ds.get(i).get("subpanel")).clickLinkExisting();
			
			// Verify that the "Search and Add Contacts(selected module name)" window appears
			mod.searchSelect.getControl("moduleTitle").assertContains(ds.get(i).get("moduleTitle"), true);
			
			mod.searchSelect.getControl("cancel").click();
			sugar().alerts.waitForLoadingExpiration();
			i++;
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}