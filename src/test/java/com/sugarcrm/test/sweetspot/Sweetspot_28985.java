package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28985 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Sweet Spot Window should not be dismissed while hitting "Enter"
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28985_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Activate Sweetspot
		sugar.sweetspot.show();

		// Nothing type in the search and Hit Enter 
		// TODO: CB-252 and VOOD-1437
		sugar.sweetspot.search("");
		sugar.sweetspot.getControl("searchBox").append("" + '\uE007');

		// Verify that Sweet spot window should not be dismissed
		sugar.sweetspot.getControl("sweetspotBar").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}