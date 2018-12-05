package com.sugarcrm.test.GlobalSearch;

import static org.junit.Assert.*;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_26127 extends SugarTest {
	DataSource accountRecords = new DataSource();

	public void setup() throws Exception {
		accountRecords = testData.get(testName);
		sugar().accounts.api.create(accountRecords);
		sugar().login();
	}

	/**
	 * Load more records when the search result is more than 10
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_26127_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String searchString = accountRecords.get(0).get("name");
		VoodooControl viewAllResultsBtn = sugar().navbar.search.getControl("viewAllResults");
		VoodooControl globalSearchBar = sugar().navbar.getControl("globalSearch");

		// Type search string 
		globalSearchBar.set(searchString);
		VoodooUtils.waitForReady();
		// TODO: VOOD-1868 - Support Global search typeahead results
		int recordCountTypeAhead = new VoodooControl("li", "css", sugar().navbar.search.getControl("searchResults").getHookString() + " .search-result").countWithClass();

		// Assert that type ahead drop down lists 5 records on page, and there is a "View All Results" link
		assertEquals("The record counts in search type ahead drop down is not equal to 5.", Integer.parseInt(accountRecords.get(1).get("name")), recordCountTypeAhead);
		viewAllResultsBtn.click();
		VoodooUtils.waitForReady();

		// Assert that "More Search Results..." link is not visible when there are only 20 records
		sugar().globalSearch.getRow(20).assertVisible(true);
		sugar().globalSearch.getRow(21).assertExists(false);

		// TODO: VOOD-1848 - Need Lib support for controls on the Global Search Results page
		VoodooControl globalSearchShowMoreBtn = new VoodooControl("button", "css", ".search-more-results button[data-action='show-more']");
		globalSearchShowMoreBtn.assertVisible(false);

		sugar().navbar.search.getControl("cancelSearch").click();

		// Create an account record with name starting with search string i.e "Test" to have 21 records
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(searchString + testName);
		sugar().accounts.createDrawer.save();

		// Assert that when there are more than 20 records matching the search criterion, show more button appears.
		globalSearchBar.set(searchString);
		VoodooUtils.waitForReady();
		viewAllResultsBtn.click();
		VoodooUtils.waitForReady();
		globalSearchShowMoreBtn.click();
		VoodooUtils.waitForReady();
		sugar().globalSearch.getRow(21).assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}