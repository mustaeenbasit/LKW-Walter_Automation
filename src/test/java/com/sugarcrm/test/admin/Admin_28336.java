package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_28336 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that currency name and symbol are populated based on the currency ISO-4217 code
	 * @throws Exception
	 */
	@Test
	public void Admin_28336_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource currencyData = testData.get(testName);

		// Go to Admin -> Currencies
		sugar().admin.navToCurrencySettings();
		VoodooUtils.focusFrame("bwc-frame");

		for (int i = 0; i < currencyData.size(); i++) {
			// Type valid ISO 4217 Code
			sugar().admin.currencySettings.getControl("ISOcode").set(currencyData.get(i).get("ISOcode"));
			VoodooUtils.waitForReady();

			// Verify that Currency name and symbol populated in the corresponded fields based on the typed code
			sugar().admin.currencySettings.getControl("currencyName").assertContains(currencyData.get(i).get("currencyName"), true);
			sugar().admin.currencySettings.getControl("currencySymbol").assertContains(currencyData.get(i).get("currencySymbol"), true);
			sugar().admin.currencySettings.getControl("cancel").click();
			VoodooUtils.waitForReady();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}