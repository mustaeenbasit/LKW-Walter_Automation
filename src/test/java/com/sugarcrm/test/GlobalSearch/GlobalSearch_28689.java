package com.sugarcrm.test.GlobalSearch;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28689 extends SugarTest {
	DataSource accountRecords = new DataSource();

	public void setup() throws Exception {
		accountRecords = testData.get(testName);
		sugar().accounts.api.create(accountRecords);
		sugar().login();
		
		FieldSet fs = new FieldSet();
		fs.put("maxEntriesPerPage", "10");
		sugar().admin.setSystemSettings(fs);
	}

	/**
	 * Verify the default number of search results
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28689_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String searchString = accountRecords.get(0).get("name").substring(0, 4);

		// TODO: VOOD-1848 - Need Lib support for controls on the Global Search Results page
		VoodooControl viewAllResultsBtn = sugar().navbar.search.getControl("viewAllResults");
		VoodooControl globalSearchBar = sugar().navbar.getControl("globalSearch");
		VoodooControl globalSearchShowMoreBtn = new VoodooControl("button", "css", ".search-more-results button[data-action='show-more']");

		// Type search string 
		globalSearchBar.set(searchString);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1868 - Support Global search typeahead results
		int recordCountTypeAhead = new VoodooControl("li", "css", sugar().navbar.search.getControl("searchResults").getHookString() + " .search-result").countWithClass();

		// Assert that type ahead drop down lists 5 records on page, and there is a "View All Results" link
		assertEquals("The record counts in search type ahead drop down is not equal to 5.", 5, recordCountTypeAhead);
		viewAllResultsBtn.assertVisible(true);

		// Click View All Results
		viewAllResultsBtn.click();
		VoodooUtils.waitForReady();

		// Assert that when there are more than 10 records matching the search criterion, show more button appears.
		sugar().globalSearch.getRow(10).assertExists(true);
		sugar().globalSearch.getRow(11).assertExists(false);
		globalSearchShowMoreBtn.assertVisible(true);

		// Click Show more...
		globalSearchShowMoreBtn.click();
		VoodooUtils.waitForReady();
		
		// Verify that next 10 records are displayed
		sugar().globalSearch.getRow(20).assertExists(true);
		sugar().globalSearch.getRow(21).assertExists(false);
		globalSearchShowMoreBtn.assertVisible(true);

		// Click Show more...
		globalSearchShowMoreBtn.click();
		VoodooUtils.waitForReady();
		
		// Verify that rest of the records are displayed
		sugar().globalSearch.getRow(30).assertExists(true);
		sugar().globalSearch.getRow(31).assertExists(false);
		globalSearchShowMoreBtn.assertVisible(true);

		// Click Show more...
		globalSearchShowMoreBtn.click();
		VoodooUtils.waitForReady();
		
		// Verify that rest of the records are displayed
		sugar().globalSearch.getRow(33).assertExists(true);
		sugar().globalSearch.getRow(34).assertExists(false);
		globalSearchShowMoreBtn.assertVisible(false);

		// ReType search string and verify that no. of records displayed is unchanged
		globalSearchBar.set(searchString);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1868 - Support Global search typeahead results
		recordCountTypeAhead = new VoodooControl("li", "css", sugar().navbar.search.getControl("searchResults").getHookString() + " .search-result").countWithClass();

		// Assert that type ahead drop down lists 5 records on page, and there is a "View All Results" link
		assertEquals("The record counts in search type ahead drop down is not equal to 5.", 5, recordCountTypeAhead);
		viewAllResultsBtn.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}