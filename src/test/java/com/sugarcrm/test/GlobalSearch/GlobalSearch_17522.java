package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_17522 extends SugarTest {
	DataSource leadsData = new DataSource();

	public void setup() throws Exception {
		// Lead names should start with 4 common chars in csv.
		leadsData = testData.get(testName);
		sugar().leads.api.create(leadsData);
		sugar().login();
	}

	/**
	 * 17522 support typeahead for global search 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_17522_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Type first 1 character, verify both records are shown. 
		sugar().navbar.setGlobalSearch(leadsData.get(0).get("lastName").substring(0, 1));
		VoodooUtils.waitForReady();
		VoodooControl searchResultCtrl = sugar().navbar.search.getControl("searchResults");
		searchResultCtrl.assertContains(leadsData.get(0).get("lastName"), true);
		searchResultCtrl.assertContains(leadsData.get(1).get("lastName"), true);

		// Type unique record name, verify there is only one result is shown
		sugar().navbar.setGlobalSearch(leadsData.get(0).get("lastName"));
		VoodooUtils.waitForReady();
		searchResultCtrl.assertContains(leadsData.get(0).get("lastName"), true);
		searchResultCtrl.assertContains(leadsData.get(1).get("lastName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}