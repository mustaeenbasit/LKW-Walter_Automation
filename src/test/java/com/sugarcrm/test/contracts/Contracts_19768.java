package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Contracts_19768 extends SugarTest {
	DataSource accountsData = new DataSource(), contractsData = new DataSource();
	VoodooControl studio, studioContracts, layoutBtn, searchBtn, basicSearchBtn, saveAndDeploy;

	public void setup() throws Exception {
		accountsData = testData.get(testName+"_accounts");
		contractsData = testData.get(testName);
		studio = sugar.admin.adminTools.getControl("studio");

		// TODO: VOOD-1509, VOOD-1510
		studioContracts = new VoodooControl("a", "id", "studiolink_Contracts");
		layoutBtn = new VoodooControl("td", "id", "layoutsBtn");
		searchBtn = new VoodooControl("td", "id", "searchBtn");
		basicSearchBtn = new VoodooControl("td", "id", "BasicSearchBtn");
		saveAndDeploy = new VoodooControl("input", "id", "savebtn");

		// 2 account records created via API
		sugar.accounts.api.create(accountsData);
		sugar.login();

		// Enable Contracts module
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);

		// Studio -> Layout -> search -> Add account name and status fields to Default
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studio.click();
		VoodooUtils.waitForReady();
		studioContracts.click();
		VoodooUtils.waitForReady();
		layoutBtn.click();
		VoodooUtils.waitForReady();
		searchBtn.click();
		VoodooUtils.waitForReady();
		basicSearchBtn.click();
		VoodooUtils.waitForReady();
		VoodooControl defaultLayout = new VoodooControl("td", "css", "#Default #ul0");
		// TODO: VOOD-1509, VOOD-1510
		new VoodooControl("li", "css", "li[data-name=account_name]").dragNDropViaJS(defaultLayout);
		new VoodooControl("li", "css", "li[data-name=status]").dragNDropViaJS(defaultLayout);
		saveAndDeploy.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// TODO: VOOD-444
		// account and asigned to relate fields via UI
		sugar.contracts.create(contractsData);
	}

	/**
	 * Search Contract_Verify that basic search for existing contracts works correctly.
	 * 
	 * @throws Exception
	 */
	@Ignore("VOOD-1742 VoodooBWCRelate set method should work with exact XPATH equality of text to work properly")
	@Test
	public void Contracts_19768_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Go to Contracts module
		sugar.contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// Search condition: name = default contract data, status = In Progress, account name = Nerd, My items checked
		sugar.contracts.listView.getControl("nameBasic").set(sugar.contracts.getDefaultData().get("name"));
		sugar.contracts.listView.getControl("myItemsCheckbox").set("true");
		new VoodooControl("input", "id", "account_name_basic").set(accountsData.get(1).get("name"));
		new VoodooControl("li", "css", "#search_form_account_name_basic_results ul li").click();
		new VoodooControl("li", "css", "#status_basic option[value=inprogress]").click();
		VoodooUtils.focusDefault();
		sugar.contracts.listView.submitSearchForm();

		// Verify records with search condition
		Assert.assertTrue("Search condition count records are not matched", sugar.contracts.listView.countRows() == 1);
		VoodooUtils.focusDefault();
		sugar.contracts.listView.verifyField(1, "name", sugar.contracts.getDefaultData().get("name"));
		sugar.contracts.listView.verifyField(1, "account_name", contractsData.get(3).get("account_name"));
		sugar.contracts.listView.verifyField(1, "status", contractsData.get(3).get("status"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}