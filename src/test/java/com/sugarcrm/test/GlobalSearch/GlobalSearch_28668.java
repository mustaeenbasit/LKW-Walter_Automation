package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28668 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that quick search results displays "Searching..." before results are retrieved.
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28668_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Define Controls
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		VoodooControl searchResultsCtrl = sugar().navbar.search.getControl("searchResults");

		// Type one character in the quick search bar
		globalSearchCtrl.click();
		globalSearchCtrl.set(sugar().accounts.getDefaultData().get("name").substring(0, 1));

		// Type second character in the quick search bar
		globalSearchCtrl.set(sugar().accounts.getDefaultData().get("name").substring(0, 2));

		// The quick search results displays "Searching..." before results are retrieved
		searchResultsCtrl.assertContains(customFS.get("searching"), true);

		// Make sure it does NOT display "No results were found." before results are retrieved
		searchResultsCtrl.assertContains(customFS.get("noResult"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}