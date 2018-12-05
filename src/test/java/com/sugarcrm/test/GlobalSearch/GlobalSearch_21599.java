package com.sugarcrm.test.GlobalSearch;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class GlobalSearch_21599 extends SugarTest {
	DataSource accountData = new DataSource();
	ArrayList<Record> leads, accounts = new ArrayList<Record>();

	public void setup() throws Exception {
		// Create records in accounts/leads module
		DataSource leadData = testData.get(testName);
		accountData = testData.get(testName+"_accountData");
		leads = sugar().leads.api.create(leadData);
		accounts = sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Global search icon
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_21599_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// From top navigation bar, click on search box
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();

		// Verify Search bar is extended horizontally
		// TODO: VOOD-1849, VOOD-1882
		new VoodooControl("div", "css", ".search.expanded").assertAttribute("class", "expanded");

		// Click on "Search Module" Dropdown
		VoodooControl searchModuleIcon = sugar().navbar.search.getControl("searchModuleDropdown");
		searchModuleIcon.click();
		// Select any module i.e. Accounts
		sugar().navbar.search.getControl("searchAccounts").click();

		// Enter data in search bar 
		globalSearchCtrl.set(accountData.get(2).get("description"));
		VoodooUtils.waitForReady();

		// Verify Available accounts record as per Search query are displayed as popup list below to "Search" box.
		VoodooControl typeAheadSearchResult = sugar().navbar.search.getControl("searchResults");
		typeAheadSearchResult.assertContains(accountData.get(2).get("name"), true);

		// Hit Enter
		// TODO: CB-252, VOOD-1437
		globalSearchCtrl.append("'\uE007'");

		// Verify that Only records from Accounts module related to search is appearing
		sugar().globalSearch.getRow(1).assertContains(accountData.get(2).get("name"), true);

		// Choose "Search all" from module list drop-down by clicking on Ac icon 
		// TODO: VOOD-1849
		searchModuleIcon.click();
		sugar().navbar.search.getControl("searchAll").click();

		// Enter text in the placeholder again
		globalSearchCtrl.set(accountData.get(2).get("description"));
		VoodooUtils.waitForReady();

		// Verify available record as per Search query are displayed as popup list below to "Search" box.
		for(Record record : leads) {
			typeAheadSearchResult.assertContains(record.get("lastName"), true);
		}
		typeAheadSearchResult.assertContains(accountData.get(2).get("name"), true);

		// Press Enter
		// TODO: CB-252, VOOD-1437
		globalSearchCtrl.append("'\uE007'");
		VoodooUtils.waitForReady();

		// Records from all modules related to search text is appearing.
		// Verifying Leads records
		for(Record record : leads) {
			sugar().globalSearch.getRow(record).assertVisible(true);
		}

		// Verifying Accounts records
		sugar().globalSearch.getRow(accounts.get(2)).assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}