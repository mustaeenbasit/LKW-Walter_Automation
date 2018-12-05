package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22973 extends SugarTest {
	DataSource customDS;

	public void setup() throws Exception {
		customDS = testData.get(testName+"_record");
		sugar().accounts.api.create(customDS);
		sugar().login();

		// Create filter in Account list view for several accounts
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("name", "Name", "starts with", customDS.get(0).get("name").substring(0, 4), 1);
		sugar().accounts.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();
	}

	/**
	 * Filter Account_Verify that the layout of account records can be updated.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22973_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify, the results are shown as per the saved "Filter".
		sugar().accounts.listView.verifyField(1, "name", customDS.get(1).get("name"));
		sugar().accounts.listView.verifyField(2, "name", customDS.get(0).get("name"));
		
		// Select a saved filter from precondition. 
		// TODO: VOOD-1580
		new VoodooControl("span", "css", "[data-voodoo-name='Accounts'] .choice-filter-label").click();
		VoodooUtils.waitForReady();
		
		// Change the specified fields or values in it (add or remove condition)
		sugar().accounts.listView.filterCreate.setFilterFields("name", "Name", "starts with", customDS.get(2).get("name").substring(0, 3), 1);

		// Verify that the layout of account records can be updated.
		sugar().accounts.listView.verifyField(1, "name", customDS.get(3).get("name"));
		sugar().accounts.listView.verifyField(2, "name", customDS.get(2).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}