package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28749 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user gets improved feedback while searching anything in the Global Search Bar Dropdown
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28749_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customText = testData.get(testName).get(0);

		// Inserting Text in the Global Search bar
		sugar().navbar.getControl("globalSearch").set(testName);

		// Verifying that "Searching.." is shown in the dropdown
		sugar().navbar.search.getControl("searchResults").assertEquals(customText.get("text"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
