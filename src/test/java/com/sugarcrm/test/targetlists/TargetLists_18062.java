package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class TargetLists_18062 extends SugarTest {
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		DataSource tlData = testData.get(testName);
		accountData = testData.get(testName + "_accData");
		sugar().targetlists.api.create(tlData);
		sugar().accounts.api.create(accountData);
		sugar().login();		
	}

	/**
	 * Add more than 20 records in targetlist
	 * @throws Exception
	 */
	@Test
	public void TargetLists_18062_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		
		// Select 40 records in listview
		sugar().accounts.listView.sortBy("headerName", true);
		sugar().accounts.listView.showMore();
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		
		// Select "Add to Target List"
		// TODO: VOOD-528,VOOD-992 - Lib support that create a targetlist from user type listview
		new VoodooControl("span", "css", ".fld_addtolist_button.list a").click();
		new VoodooControl("span", "css", ".edit.fld_prospect_lists_name").click();
		
		// click on Search for More
		new VoodooControl("div", "css", ".select2-result-label").click();
		sugar().targetlists.searchSelect.selectRecord(4);
		
		// Click on Update
		new VoodooControl("a", "css", ".fld_update_button a").click();
		VoodooUtils.waitForReady();
		
		// Navigate to TargetList module, open Accounts subpanel
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(4);
		StandardSubpanel accountsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		accountsSubpanel.expandSubpanel();
		
		accountsSubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();
		
		// Verify accounts subpanel shows accounts records.
		for (int i = accountData.size()-1 ; i > (accountData.size()-5) ; i--) {
			accountsSubpanel.verify(accountData.size()-i, accountData.get(i), true);
		}
		
		// Also, "More Accounts" link is available to view the other records. 
		accountsSubpanel.getControl("moreLink").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}