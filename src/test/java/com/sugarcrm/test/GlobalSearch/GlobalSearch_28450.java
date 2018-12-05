package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28450 extends SugarTest {

	public void setup() throws Exception {
		sugar().campaigns.api.create();
		sugar().login();
	}

	/**
	 * Verify that OOB Default fields that display for Campaign Search Result
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28450_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Search the created campaign substring in global search 
		sugar().navbar.setGlobalSearch(sugar().campaigns.defaultData.get("name").substring(0, 3));

		// Verifying campaign name is displaying in search result.
		sugar().navbar.search.getControl("searchResults").assertContains(sugar().campaigns.defaultData.get("name"), true);

		// TODO: VOOD-1843
		// Verifying searched substring is highlighted in search result.
		new VoodooControl("strong", "css", ".search-result strong").assertEquals(sugar().campaigns.defaultData.get("name").substring(0, 3), true);

		// TODO: VOOD-1849
		// Verifying appearance of Campaign icon in global search result
		new VoodooControl("span", "css", ".search-result [data-voodoo-name='picture']").assertEquals(sugar().campaigns.moduleNamePlural.substring(0, 2), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}