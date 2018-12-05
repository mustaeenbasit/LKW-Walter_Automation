package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28766 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().targets.api.create();
		sugar().calls.api.create();
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify Quick search pane should dismiss after click on view all results
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28766_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Inserting a single char 'A' in the Global Search bar
		VoodooControl searchBox = sugar().navbar.getControl("globalSearch");
		searchBox.set(sugar().accounts.getDefaultData().get("name").substring(0,1));
		VoodooUtils.waitForReady();

		// Clicking on 'View All Results' in Quick Search bar
		VoodooControl viewAllResults = sugar().navbar.search.getControl("viewAllResults");
		viewAllResults.click();

		// Verifying the user is navigated to Global search page 
		FieldSet fs = testData.get(testName).get(0);
		sugar().globalSearch.getControl("headerpaneTitle").assertContains(fs.get("searchResultPage"), true);

		// Inserting a single char 'r' in the Global Search bar
		searchBox.set(sugar().accounts.getDefaultData().get("name").substring(6,7));
		VoodooUtils.waitForReady();

		// Clicking on 'View All Results' in Quick Search bar
		viewAllResults.click();

		// Verify the Quick Search pane dismiss after clicking on 'View All Results'
		sugar().navbar.search.getControl("searchResults").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}