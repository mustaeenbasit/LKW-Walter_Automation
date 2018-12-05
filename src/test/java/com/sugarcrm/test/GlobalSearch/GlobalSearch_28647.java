package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28647 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify "no results were found" message is displayed when user search any non-existing string. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28647_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");

		// TODO: VOOD-1849
		// Enter any non existing string in the global search textbox
		globalSearch.set(testName);
		VoodooUtils.waitForReady();

		// Verify that "No results were found" message is displayed in the Quick search typeahead dropdown.
		sugar().navbar.search.getControl("searchResults").assertContains(customFS.get("searchResult"), true);

		// TODO: CB-252
		// Hit ENTER when it says "No results were found" in the Quick Search Typeahead Dropdown. 
		globalSearch.set(testName+'\uE007');

		// Verify that the user is navigated to the Global search results page with "No results were found" message displayed in the page. 
		sugar().globalSearch.getControl("headerpaneTitle").assertContains(customFS.get("headerpaneTitle"), true);
		new VoodooControl("li", "css", ".nav.search-results .no-results").assertContains(customFS.get("searchResult"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}