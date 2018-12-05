package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_29985 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}
	
	/**
	 * Verify that Global serach bar minimizes on clicking outside the search bar
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_29985_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-1849, CB-252
		// Search and hit enter
		sugar().navbar.getControl("globalSearch").set(testName + '\uE007');
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		sugar().navbar.search.getControl("searchLeads").click();
		
		// Assert Search Bar is expanded 
		// TODO: VOOD-1849
		VoodooControl searchBarExpand = new VoodooControl("div", "css", ".search.expanded");
		searchBarExpand.assertVisible(true);
		
		// Navigating to home page
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		
		// Assert Search Bar is collapsed
		searchBarExpand.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}