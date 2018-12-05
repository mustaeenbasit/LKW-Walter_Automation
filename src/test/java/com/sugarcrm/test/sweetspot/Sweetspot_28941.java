package com.sugarcrm.test.sweetspot;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28941 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that "Upgrade Wizard" action shouldn't display in Sweet Spot
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28941_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Activate Sweetspot
		sugar().sweetspot.show();

		// Entering 'Upgrade' in sweetspot search panel
		FieldSet searchString = testData.get(testName).get(0);
		sugar().sweetspot.search(searchString.get("searchParam"));

		// Asserting 'Upgrade Wizard' action is not visible in search result 
		sugar().sweetspot.getControl("searchResultActions").assertContains(searchString.get("searchAction"), false);

		// Hide Sweetspot
		sugar().sweetspot.hide();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}