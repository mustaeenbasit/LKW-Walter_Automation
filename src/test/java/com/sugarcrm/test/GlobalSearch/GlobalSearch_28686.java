package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class GlobalSearch_28686 extends SugarTest {
	public void setup() throws Exception {
		// Nav to RLI
		sugar().login();
		sugar().navbar.navToModule(sugar().revLineItems.moduleNamePlural);
	}

	/**
	 * Verify quicksearch bar does not expands too far if 1st module has long name.
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28686_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Search to expand the Search bar
		sugar().navbar.getControl("globalSearch").set(testName);
		VoodooUtils.waitForReady();
		
		// Assert Search Bar is expanded 
		// TODO: VOOD-1849
		VoodooControl searchBarExpand = new VoodooControl("div", "css", ".search.expanded");
		searchBarExpand.assertVisible(true);
		
		// Assert Navbar Items on Left side are visible
		sugar().navbar.getControl("showAllModules").assertVisible(true);
		// TODO:VOOD-1866 Need access to certain Controls used in Lib Methods
		new VoodooControl("li", "css", "ul.nav.megamenu li[data-module='" 
				+ sugar().revLineItems.moduleNamePlural + "'] a").assertVisible(true);
		new VoodooControl("button", "css", "li[data-module='" + sugar().revLineItems.moduleNamePlural 
				+ "'] button[data-toggle='dropdown']").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}