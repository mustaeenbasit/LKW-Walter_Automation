package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_26879 extends SugarTest {
	FieldSet currencyMngt, fs, separators;
	VoodooControl currencyCtrl;

	public void setup() throws Exception {
		currencyMngt = testData.get(testName).get(0);
		fs = testData.get(testName+"_sep").get(0); 
		sugar().login();

		// Change thousands separator to "." and decimal separator to ","  in user preferences
		sugar().users.setPrefs(fs);
	}

	/**
	 * Verify that currencies conversion rate setting is correct when the decimal is set to comma and 1000 separator is dot.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_26879_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add new Currency
		// Create Currency custom currency 'Euro'
		currencyMngt.put("currencyName", testName);
		sugar().admin.setCurrency(currencyMngt);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that the conversion rate is 1,9
		new VoodooControl("a","xpath","//*[@id='contentTable']/tbody/tr/td/table[1]/tbody/tr[contains(.,'"+testName+"')]").assertContains(currencyMngt.get("conversionRate"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}