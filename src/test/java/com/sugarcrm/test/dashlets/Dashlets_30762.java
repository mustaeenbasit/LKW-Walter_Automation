package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_30762 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Home dashboards loads correctly if preferred currency is made inactive.
	 * @throws Exception
	 */
	@Test
	public void Dashlets_30762_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource customData = testData.get(testName);

		// Creating Euro currency
		FieldSet currencyData = new FieldSet();
		currencyData.put("currencyName", customData.get(0).get("currencyName"));
		currencyData.put("ISOcode", customData.get(0).get("ISOcode"));
		currencyData.put("conversionRate", customData.get(0).get("conversionRate"));
		currencyData.put("currencySymbol", customData.get(0).get("currencySymbol"));
		sugar().admin.setCurrency(currencyData);
		currencyData.clear();

		// Change the prefered currency in Locale
		currencyData.put("advanced_preferedCurrency", customData.get(0).get("advanced_preferedCurrency"));
		sugar().users.setPrefs(currencyData);
		currencyData.clear();

		// Navigating to home page
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// Verifying All dashlets are appearing properly with active currency and inactive currency.
		for (int i = 0; i < 2; i++) {

			// Verifying Dashlets in 1st column.
			int j = 0;
			for (int row = 1; row <= Integer.parseInt(customData.get(0).get("totalRowsOfDashlets")); row++, j++) 
				// TODO: VOOD-1376
				new VoodooControl("ul", "css", ".dashlets.row-fluid li .dashlet-row.ui-sortable li:nth-of-type(" + row + ") .dashlet-cell").assertContains(customData.get(j).get("homePageDashlets"), true);

			// Verifying Dashlets in 2nd Column.
			for (int row = 1; row < Integer.parseInt(customData.get(0).get("totalRowsOfDashlets")); row++, j++) 
				// TODO: VOOD-1376
				new VoodooControl("ul", "css", ".dashlets.row-fluid li:nth-child(2) .dashlet-row.ui-sortable li:nth-of-type(" + row + ") .dashlet-cell").assertContains(customData.get(j).get("homePageDashlets"), true);

			if (i == 0) {
				// Inactive the currency
				sugar().admin.inactiveCurrency(customData.get(0).get("currencyName"));

				// Navigate to home page
				sugar().navbar.navToModule(sugar().home.moduleNamePlural);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}