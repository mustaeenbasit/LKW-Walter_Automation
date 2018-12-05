package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28735 extends SugarTest {
	DataSource accountNames = new DataSource();

	public void setup() throws Exception {
		accountNames = testData.get(testName);
		sugar().accounts.api.create(accountNames);
		sugar().login();
	}

	/**
	 * Verify Display number of search results found on Global Search Page
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28735_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl globalSearchViewTitle = sugar().globalSearch.getControl("headerpaneTitle");
		// TODO: VOOD-1848
		VoodooControl searchOutputCount = new VoodooControl("span", "css", ".search-headerpane .count");

		FieldSet searchResultCount = testData.get(testName + "_searchResultCount").get(0);

		// Type an account name in the globalSearch field and hit Enter
		// TODO: CB-252
		sugar().navbar.getControl("globalSearch").set(accountNames.get(0).get("name") + '\uE007');
		VoodooUtils.waitForReady();

		// Verify it displays number of search results found
		globalSearchViewTitle.assertContains("\"" + accountNames.get(0).get("name") + "\"", true);
		searchOutputCount.assertEquals(searchResultCount.get("testCount"), true);

		// Clear the search field
		sugar().navbar.search.getControl("cancelSearch").click();

		// Type another account name in the globalSearch field and hit Enter
		// TODO: CB-252
		sugar().navbar.getControl("globalSearch").set(accountNames.get(5).get("name") + '\uE007');
		VoodooUtils.waitForReady();

		// Verify it displays number of search results found
		globalSearchViewTitle.assertContains("\"" + accountNames.get(5).get("name") + "\"", true);
		searchOutputCount.assertEquals(searchResultCount.get("eastCount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}