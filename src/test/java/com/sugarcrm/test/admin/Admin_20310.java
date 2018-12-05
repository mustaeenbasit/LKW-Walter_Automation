package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20310 extends SugarTest {
	FieldSet currencyData;
	
	public void setup() throws Exception {
		currencyData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Ensure double characters currency can be created
	 * 
	 * @throws Exception
	 */
	@Test	
	public void Admin_20310_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Create custom currency with entering double characters in 'Currency Symbol' field
		currencyData.put("currencyName", testName);
		sugar().admin.setCurrency(currencyData);
		VoodooUtils.focusFrame("bwc-frame");
		
		// XPath used to find currency by name
		// Verify that created currency is displayed as inputted
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[1]/tbody/tr[contains(.,'"+currencyData.get("currencySymbol")+"')]").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}