package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28614 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Sweet Spot theme changes to user's choice
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28614_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Initialize test data
		DataSource testDS = testData.get(testName);

		for (int i = 0; i < testDS.size(); i++) {
			// Modify the theme of Sweetspot panel
			updateTheme(testDS.get(i).get("theme"));

			// Assert Sweetspot color
			assertColor(testDS.get(i).get("color"));
		} 

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	private void updateTheme (String theme) throws Exception {
		// Call Sweetspot
		sugar().sweetspot.show();

		// Click on the "Sweet Spot configuration link"
		sugar().sweetspot.configure();

		// Modify the theme of Sweetspot panel
		sugar().sweetspot.configureTheme(theme);

		// Save Configuration 
		sugar().sweetspot.saveConfiguration();
	}

	private void assertColor (String color) throws Exception {
		// Call Sweetspot
		sugar().sweetspot.show();

		// Verify the Sweetspot bar theme default color
		sugar().sweetspot.getControl("sweetspotBar").assertCssAttribute("background-color", color, true);
	}

	public void cleanup() throws Exception {}
}