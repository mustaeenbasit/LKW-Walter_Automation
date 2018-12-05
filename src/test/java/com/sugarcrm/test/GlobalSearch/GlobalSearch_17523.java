package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_17523 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Press enter key to perform global search 
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_17523_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Input data in global search box
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		globalSearch.set(sugar().leads.getDefaultData().get("firstName").substring(0, 2));
		
		// Press enter key
		// TODO: CB-252
		globalSearch.append("" + '\uE007');
		VoodooUtils.waitForReady();

		// Verify that it should perform search regardless of how many characters have been provided
		// TODO: VOOD-1853
		new VoodooControl("strong", "css", ".search-result .ellipsis_inline span strong").assertContains(sugar().leads.getDefaultData().get("firstName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}