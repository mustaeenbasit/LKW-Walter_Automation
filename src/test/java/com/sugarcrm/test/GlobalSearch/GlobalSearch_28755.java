package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28755 extends SugarTest {
	public void setup() throws Exception {
		// Creating Account,Leads and Calls record
		sugar().accounts.api.create();
		sugar().leads.api.create();
		sugar().calls.api.create();
		sugar().login();
	}

	/**
	 * Verify that searching is working fine while using single character
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28755_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customText = testData.get(testName).get(0);

		// Inserting a single character in the Global Search bar
		sugar().navbar.getControl("globalSearch").set(customText.get("singleCharacter"));

		// Verifying that "Searching..." is shown in the dropdown
		VoodooControl searchResult = sugar().navbar.search.getControl("searchResults");
		searchResult.assertEquals(customText.get("searchingText"), true);

		// Verifying the records displayed in the quick search list
		VoodooUtils.waitForReady();
		searchResult.assertContains(sugar().calls.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}