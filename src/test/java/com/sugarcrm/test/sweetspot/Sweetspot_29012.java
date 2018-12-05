package com.sugarcrm.test.sweetspot;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Sweetspot_29012 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that searched records are not displayed outside the sweet spot search window
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_29012_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.sweetspot.show();
		sugar.sweetspot.search("R");

		VoodooControl mainDiv = new VoodooControl("div", "css", "#content");

		// Fetch random first result. One of the rarest cases where we need to use getText()
		String firstResult = sugar.sweetspot.getActionsResult(3).getText();

		// Verify that this text is not available in main content Div
		mainDiv.assertContains(firstResult, false);		

		// Fetch random second result. One of the rarest cases where we need to use getText()
		String secondResult = sugar.sweetspot.getActionsResult(4).getText();

		// Verify that this text is not available in main content Div
		mainDiv.assertContains(secondResult, false);		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}