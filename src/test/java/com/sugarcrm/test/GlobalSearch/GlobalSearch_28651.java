package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28651 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that user is able select module in global search window
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28651_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Click on Global search text box to expand
		sugar().navbar.getControl("globalSearch").click();
		
		// Verify that "All" (module dropdown) should be displayed
		VoodooControl allModuleDropdown = sugar().navbar.search.getControl("searchModuleDropdown");
		sugar().navbar.getGlobalSearchModulePill(1).assertEquals(customFS.get("all"), true);
		sugar().navbar.search.getControl("searchModuleDropdown").assertVisible(true);

		// Click on "All"(Module dropdown)
		allModuleDropdown.click();

		// Verify that "Search All" link is selected
		// VOOD-2045 - Eliminate assertAttribute("value" from the codebase.
		sugar().navbar.search.getControl("searchAll").assertAttribute("class", "selected");
		
		// Click on Accounts module, Verifying it is selected and 'Ac' Label is showing in 1st pill. 
		VoodooControl accountsModuleCtrl = sugar().navbar.search.getControl("searchAccounts");
		accountsModuleCtrl.click();
		
		// VOOD-2045 - Eliminate assertAttribute("value" from the codebase.
		accountsModuleCtrl.assertAttribute("class", "selected");
		sugar().navbar.getGlobalSearchModulePill(1).assertEquals(customFS.get("accountsLabel"), true);
		
		// Click on Bugs module, Verifying it is selected and 'Bu' Label is showing in 2nd pill.
		VoodooControl bugsModuleCtrl = sugar().navbar.search.getControl("searchBugs");
		bugsModuleCtrl.click();
		
		// VOOD-2045 - Eliminate assertAttribute("value" from the codebase.
		bugsModuleCtrl.assertAttribute("class", "selected");
		sugar().navbar.getGlobalSearchModulePill(2).assertEquals(customFS.get("bugsLabel"), true);
		
		// Click on Quotes module, Verifying it is selected and 'Qu' Label is showing in 3rd pill.
		VoodooControl quotesModuleCtrl = sugar().navbar.search.getControl("searchQuotes");
		quotesModuleCtrl.scrollIntoViewIfNeeded(true);
		
		// VOOD-2045 - Eliminate assertAttribute("value" from the codebase.
		quotesModuleCtrl.assertAttribute("class", "selected");
		sugar().navbar.getGlobalSearchModulePill(3).assertEquals(customFS.get("quotesLabel"), true);
		
		// Click on Quotes module, Verifying it is selected and multiple module is showing.
		VoodooControl campaignsModuleCtrl = sugar().navbar.search.getControl("searchCampaigns");
		campaignsModuleCtrl.scrollIntoViewIfNeeded(true);
		
		// VOOD-2045 - Eliminate assertAttribute("value" from the codebase.
		campaignsModuleCtrl.assertAttribute("class", "selected");
		sugar().navbar.getGlobalSearchModulePill(1).assertEquals(customFS.get("multipleModules"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}