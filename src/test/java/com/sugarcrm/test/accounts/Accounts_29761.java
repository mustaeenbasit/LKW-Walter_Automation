package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_29761 extends SugarTest {
	public void setup() throws Exception {
		sugar().documents.api.create();
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Create button is not displaying at Search & Select drawer for BWC module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_29761_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Go to Subpanel of BWC module i.e. Documents and click on Link Existing Record option 
		StandardSubpanel documentsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural);
		documentsSubpanel.clickLinkExisting();
			
		// Verify that Create button not displayed for BWC module
		sugar().accounts.searchSelect.getControl("create").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}