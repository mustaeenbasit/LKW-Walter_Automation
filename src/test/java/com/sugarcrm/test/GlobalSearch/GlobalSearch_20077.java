package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20077 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar().accounts.api.create(customDS);
		sugar().login();
	}

	/**
	 * Reposition 'Show All' Button in Global Search Window
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20077_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet searchFS = testData.get(testName+"_search").get(0);

		// Input string in the global search area
		VoodooControl globalSearchCtrl =  sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.set(customDS.get(0).get("name").substring(0, 4));
		VoodooUtils.waitForReady();

		// "Show All" button is appearing at the Global Search result drop down
		sugar().navbar.search.getControl("viewAllResults").assertContains(searchFS.get("viewAllResult"), true);

		try {
			// Click search
			globalSearchCtrl.append("" + '\uE007');
			VoodooUtils.waitForReady();

			// "Show All" button is appearing at the Global Search result drop down
			// TODO: VOOD-1848
			new VoodooControl("button", "css", ".search-more-results button").assertContains(searchFS.get("moreResult"), true);
		}
		finally {
			// Reset the Global Search bar
			sugar().navbar.search.getControl("cancelSearch").click();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}