package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28605 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		
		// Login as a non-admin user (qauser)
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Global Search bar works for non-admin user
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28605_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// In the global search bar, type a record name that will yield results
		sugar().navbar.getControl("globalSearch").set(sugar().leads.getDefaultData().get("firstName"));
		
		// Verify that it returns correct result
		// TODO: VOOD-1848
		new VoodooControl("h3", "css", ".search-result h3").assertContains(sugar().leads.getDefaultData().get("firstName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}