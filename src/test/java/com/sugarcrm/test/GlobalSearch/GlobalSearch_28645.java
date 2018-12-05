package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28645 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that global search bar and open drawer does not clash
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28645_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Account module listView
		sugar().accounts.navToListView();
		
		// Click on the global search bar to expand
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1849
		// Verify that the global search bar should expand (while expend the search bar style "display: none" not exist)
		VoodooControl searchBarCtrl = new VoodooControl("div", "css", "[data-voodoo-name='quicksearch-modulelist'][style*='display: none']");
		searchBarCtrl.assertExists(false);
		
		// Click the Create Button to open the drawer
		sugar().navbar.clickModuleDropdown(sugar().accounts);
		VoodooControl createBtnCtrl = sugar().accounts.menu.getControl("createAccount");
		
		// Verify that Show the MEGA menu
		createBtnCtrl.assertExists(true);
		VoodooUtils.waitForReady();
		
		// Click on create button
		createBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that the global search bar should collapse (while collapse the search bar style should be "style*='display: none'")
		searchBarCtrl.assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}