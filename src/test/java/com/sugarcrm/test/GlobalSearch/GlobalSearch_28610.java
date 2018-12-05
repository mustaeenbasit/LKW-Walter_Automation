package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28610 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that [X] should not display untill there is text entered
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28610_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");

		// Verify, Make sure The "X" should only display if there is text typed in by the user in the search bar
		VoodooControl closeIconCtrl = new VoodooControl("i", "css", "[data-voodoo-name='quicksearch-button'] .fa.fa-times");
		closeIconCtrl.assertExists(false);

		// Set value in global search and verify "X" should display
		globalSearchCtrl.set(testName);
		closeIconCtrl.assertExists(true);
		
		// Clear global search text
		closeIconCtrl.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}