package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20308 extends SugarTest {
	FieldSet currencyData;
	VoodooControl currencyNameCtrl;

	public void setup() throws Exception {
		currencyData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Create currency.
	 * @throws Exception
	 */
	@Test
	public void Admin_20308_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create test Currency
		currencyData.put("currencyName", testName);
		sugar().admin.setCurrency(currencyData);
		VoodooUtils.focusFrame("bwc-frame");
				
		// XPath used to find currency by name
		// Verify created currency is displayed
		// TODO: VOOD- 1456
		currencyNameCtrl = new VoodooControl("a", "xpath","//*[@id='contentTable']/tbody/tr/td/table[1]/tbody/tr[contains(.,'"+testName+"')]");
		currencyNameCtrl.assertExists(true);
		currencyNameCtrl.assertElementContains(currencyData.get("ISOcode"), true);
		currencyNameCtrl.assertElementContains(currencyData.get("currencySymbol"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}