package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_17519 extends SugarTest {
	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		fs.put("firstName", testName);
		
		// Create records of Leads and Targets module
		sugar().leads.api.create(fs);
		sugar().targets.api.create(fs);
		sugar().login();
	}

	/**
	 * Select all searchable modules for global search by check Search all check box 
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_17519_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet modulesLabel = testData.get(testName).get(0);
		
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		VoodooControl searchAll = sugar().navbar.search.getControl("searchAll");
		String searchResultsHookString = sugar().navbar.search.getControl("searchResults").getHookString();
		
		// Click down arrow in global search box
		globalSearch.click();
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		
		// Show Search all checkbox in the global search enabled modules list
		searchAll.assertAttribute("class", "selected", true);
		
		// Input data in global search box
		globalSearch.set(testName);
		VoodooUtils.waitForReady();
		
		// Should list records in all searchable modules that match the search criteria
		// TODO: VOOD-1843
		sugar().navbar.search.getControl("searchResults").assertContains(testName, true);
		new VoodooControl("span", "css", searchResultsHookString + " .label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		new VoodooControl("span", "css", searchResultsHookString + " .label-" + sugar().targets.moduleNamePlural).assertEquals(modulesLabel.get("targetsLabel"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}