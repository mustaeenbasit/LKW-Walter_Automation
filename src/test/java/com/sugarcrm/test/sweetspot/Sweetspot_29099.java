package com.sugarcrm.test.sweetspot;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Sweetspot_29099 extends SugarTest {
	public void setup() throws Exception {
		// Login to sugar instance
		sugar().login();
	}

	/**
	 * Verify that help link is working as expected using sweet spot
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_29099_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet helpText = testData.get(testName).get(0);
		
		// Open Sweetspot
		sugar().sweetspot.show();
		sugar().sweetspot.search(helpText.get("helpText"));

		// Help link should not appear, No search result should appear.
		sugar().sweetspot.assertResultNotVisible();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}