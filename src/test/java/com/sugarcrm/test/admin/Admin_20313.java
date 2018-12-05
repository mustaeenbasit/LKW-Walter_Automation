package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20313 extends SugarTest {
	FieldSet currencyData;
	
	public void setup() throws Exception {
		currencyData = testData.get(testName).get(0);
		sugar().login();
		
		// Create currency
		currencyData.put("currencyName", testName);
		sugar().admin.setCurrency(currencyData);
	}

	/**
	 * Verify that inactive currency will not appear in My Profile currency option
	 * @throws Exception
	 */
	@Test
	public void Admin_20313_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Show the new currency in user profile
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		sugar().users.userPref.getControl("advanced_preferedCurrency").assertContains(testName, true);
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();

		// Set currency inactive
		sugar().admin.inactiveCurrency(currencyData.get("currencyName"));

		// check the inactive currency doesn't show in user profile		
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		sugar().users.userPref.getControl("advanced_preferedCurrency").assertContains(testName, false);
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}