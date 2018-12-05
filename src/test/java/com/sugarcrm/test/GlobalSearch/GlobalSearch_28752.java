package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28752 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the Ability to Clear and Collapse Global Search Bar
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28752_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String accountName = sugar().accounts.getDefaultData().get("name");
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch"); 
		globalSearch.set(accountName.substring(0,4));
		VoodooUtils.waitForReady();

		// Click on Close icon
		sugar().navbar.search.getControl("cancelSearch").click();

		// Verify that the Global Search Bar gets collapse on the results page
		// TODO: VOOD-1849
		VoodooControl searchBarExpand = new VoodooControl("div", "css", ".search.expanded");
		searchBarExpand.assertVisible(false);

		// Verify Search Query doesn't exists in Search Bar
		globalSearch.assertEquals(accountName.substring(0,4), false);

		// Verify quick search typeahead dropdown below the Search Bar gets closed
		VoodooControl searchRibbonResults = sugar().navbar.search.getControl("searchResults");
		searchRibbonResults.assertVisible(false);

		// Entering the search string in Search Bar
		// TODO: CB-252,VOOD-1437
		globalSearch.append(accountName + '\uE007');

		// Verify Global Search result Page is displayed
		sugar().globalSearch.getControl("headerpaneTitle").assertElementContains(accountName, true);

		// Verify that the Global Search Bar doesn't collapses on the results page
		searchBarExpand.assertVisible(true);

		// Verify Quick search typeahead dropdown below the Search Bar gets closed
		searchRibbonResults.assertVisible(false);
		VoodooUtils.waitForReady();

		// Navigate to Accounts Module
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		// Enter Value in Search Bar and click on the result fetched
		globalSearch.set(accountName);
		VoodooUtils.waitForReady();
		searchRibbonResults.click();
		VoodooUtils.waitForReady();

		// Verify user is navigated to that Record
		sugar().accounts.recordView.getDetailField("name").assertEquals(accountName, true);
		VoodooUtils.waitForReady();

		// Global Search Bar gets Collapsed
		searchBarExpand.assertVisible(false);

		// Verify Search Bar contains the string
		globalSearch.assertEquals(accountName, true);

		// Verify quick search typeahead dropdown below the Search Bar gets closed
		searchRibbonResults.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}