package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28646 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		// Create six contact records
		customDS = testData.get(testName+"_contactData");
		sugar().contacts.api.create(customDS);
		sugar().login();
	}

	/**
	 * Verify that Quick search works in search results page
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28646_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Enter any existing record name in Global Search textbox and press enter
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();

		// TODO: VOOD-1849, CB-252
		// search and hit enter
		globalSearchCtrl.set(customDS.get(0).get("lastName")+'\uE007');

		// TODO: VOOD-1849
		// Verify, Should display the contact record's name.
		new VoodooControl("a", "css", ".layout_default .search-result h3 a").assertContains(customDS.get(0).get("lastName"), true);

		// Go to Enter any another existing record name
		globalSearchCtrl.set(customDS.get(1).get("lastName").substring(0, 4));
		VoodooUtils.waitForReady();
		
		// Verify that If more results are there, "View all results" link should be displayed
		sugar().navbar.search.getControl("viewAllResults").assertExists(true);

		// Verify, Should display the results in the Global Search bar
		// Since Global search results for identical items are ordered randomly, exact assert is not possible
		// Searching for "Ran test" in each row (list item)
		String lastName = customDS.get(1).get("lastName");
		String customFullName = sugar().contacts.defaultData.get("firstName") + " " + lastName.substring(0, 3);
		for (int i = 1; i < customDS.size()-1; i++)
			new VoodooControl("li" , "css", ".navbar .search .dropdown-menu.search-results li:nth-child(" + i + ")")
				.assertContains(customFullName, true);
		
		// Verify only 5 records shown = 5 list items + ignore 1 extra list item for " View all results" 
		int searchResultsCount = new VoodooControl("li" , "css", ".navbar .search .dropdown-menu.search-results li").count() - 1;
		Assert.assertEquals("Search Typeahead Results count is not 5 ", 5, searchResultsCount);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}