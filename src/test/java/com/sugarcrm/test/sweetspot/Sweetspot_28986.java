package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28986 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that configuration panel is clickable on sweetspot
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28986_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Activate Sweetspot
		sugar.sweetspot.show();

		// Enter "Sweet" in the search bar
		sugar.sweetspot.search(customData.get("sweet"));

		// Click on the "Sweet Spot configuration link"
		sugar.sweetspot.configure();

		// Verify that Sweet spot configuration panel should be opened
		sugar.sweetspot.getControl("titleConfigurationPage").assertEquals(customData.get("configurationPageTitle"), true);
		sugar.sweetspot.getControl("saveConfiguration").assertExists(true);

		// Cancel the Sweet spot configuration panel
		sugar.sweetspot.cancelConfiguration();

		// Activate Sweetspot
		sugar.sweetspot.show();

		// Enter "Sweet" in the search bar and Press on Enter
		// TODO: CB-252 and VOOD-1437
		sugar.sweetspot.search(customData.get("sweet"));
		sugar.sweetspot.getControl("searchBox").append("" + '\uE007');

		VoodooUtils.waitForReady();
		
		// Verify that Sweet spot configuration panel should be opened
		sugar.sweetspot.getControl("titleConfigurationPage").assertEquals(customData.get("configurationPageTitle"), true);
		sugar.sweetspot.getControl("saveConfiguration").assertExists(true);

		// Cancel the Sweet spot configuration panel
		sugar.sweetspot.cancelConfiguration();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}