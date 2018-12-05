package com.sugarcrm.test.sweetspot;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Sweetspot_29077 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify employees should be searchable through sweet spot
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_29077_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().sweetspot.show();
		sugar().sweetspot.search("qauser");
		sugar().sweetspot.getRecordsResult().assertContains("qauser", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}