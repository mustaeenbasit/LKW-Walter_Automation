package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28751 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().accounts.api.create(customData);
		sugar().login();
	}

	/**
	 * Verify the functionality of "View all results" on Quick search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28751_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch"); 
		globalSearch.set(customData.get(0).get("name").substring(0,4));
		VoodooUtils.waitForReady();
		VoodooControl searchRibbonResults = sugar().navbar.search.getControl("searchResults");

		// Verify results exists in dropdown
		for(int i = 0; i <= customData.size()-1; i++) {
			searchRibbonResults.assertContains(customData.get(i).get("name").substring(2,7), true);
		}

		// Click View All Results
		sugar().navbar.viewAllResults();

		// Verify that the Global Search Bar does not collapse on the results page
		// TODO: VOOD-1849
		sugar().navbar.search.getControl("cancelSearch").assertVisible(true);
		new VoodooControl("div", "css", ".search.expanded").assertVisible(true);

		// Verify Search Query still exists in Search Bar
		globalSearch.assertEquals(customData.get(0).get("name").substring(0,4), true);

		// Verify quick search typeahead dropdown below the Search Bar gets closed
		searchRibbonResults.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}