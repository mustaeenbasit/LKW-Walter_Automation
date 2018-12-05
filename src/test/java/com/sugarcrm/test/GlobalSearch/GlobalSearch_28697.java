package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28697 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}
	
	/**
	 * Verify Tags module no longer exist in global search module list
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28697_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Expand the global search bar
		sugar().navbar.getControl("globalSearch").click();
		
		// Click the module list
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		
		// Verify that Tags module is not in the Module List
		sugar().navbar.search.getControl("searchModuleList").assertContains(sugar().tags.moduleNamePlural, false);
		
		// Close the module list
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}