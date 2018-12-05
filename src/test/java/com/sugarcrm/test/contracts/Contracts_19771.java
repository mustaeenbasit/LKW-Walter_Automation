package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Contracts_19771 extends SugarTest {
	DataSource accountsData = new DataSource(), contractsData = new DataSource();
	VoodooControl advanceSubmitSearch;

	public void setup() throws Exception {
		// TODO: VOOD-975
		advanceSubmitSearch = new VoodooControl("input", "id", "search_form_submit_advanced");
		accountsData = testData.get(testName+"_accounts");
		contractsData = testData.get(testName);

		// accounts record via API
		sugar.accounts.api.create(accountsData);
		sugar.login();

		// Enable Contracts
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);

		// TODO: VOOD-444
		// contracts with account name via UI
		sugar.contracts.create(contractsData);
	}

	/**
	 * Search Contract_Verify that the matching records are displayed by performing Advanced Search using Account Name.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19771_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar.contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Advance Search with account name
		sugar.contracts.listView.getControl("advancedSearchLink").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-975
		VoodooControl accountName = new VoodooControl("input", "id", "account_name_advanced");
		accountName.set(accountsData.get(0).get("name"));
		advanceSubmitSearch.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Verify 2 records with 'Aperture Labortories'
		Assert.assertTrue("Record count is not equal two", sugar.contracts.listView.countRows() == 2);
		VoodooUtils.focusDefault();
		sugar.contracts.listView.verifyField(1, "name",contractsData.get(0).get("name"));
		sugar.contracts.listView.verifyField(2, "name",contractsData.get(1).get("name"));
		VoodooUtils.focusFrame("bwc-frame");
		accountName.set(accountsData.get(1).get("name"));
		advanceSubmitSearch.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Verify only 1 record with 'Nerdapplabs'
		Assert.assertTrue("Record count is not equal one", sugar.contracts.listView.countRows() == 1);
		VoodooUtils.focusDefault();
		sugar.contracts.listView.verifyField(1, "name",contractsData.get(2).get("name"));

		VoodooUtils.focusFrame("bwc-frame");
		accountName.set(testName);
		advanceSubmitSearch.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Verify no record with 'Contracts_19771'
		Assert.assertTrue("Records are not empty", sugar.contracts.listView.countRows() == 0);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}