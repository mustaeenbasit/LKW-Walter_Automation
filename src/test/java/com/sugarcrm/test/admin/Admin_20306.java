package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20306 extends SugarTest {
	FieldSet currencyData, separators, rliData, setDefault;
	
	public void setup() throws Exception {
		currencyData = testData.get(testName + "_" + "currency").get(0);	
		sugar().login();

		// Create custom Currency
		currencyData.put("currencyName", testName);
		sugar().admin.setCurrency(currencyData);
		
		// Go to user profile and change currency, 1000s and decimal separators
		separators = testData.get(testName + "_" + "separators").get(0);
		separators.put("advanced_preferedCurrency", testName + " : E");
		sugar().users.setPrefs(separators);
	}

	/**
	 * Verify currency is set to profile currency in the create view
	 * 
	 * @throws Exception
	 */
	@Test	
	public void Admin_20306_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		rliData = testData.get(testName + "_" + "rli").get(0); 
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		
		// Create a new RLI, check that in create view, the currency is default to the user profile currency symbol
		// TODO: VOOD-983
		new VoodooControl("span", "css", ".currency.edit.fld_currency_id").assertContains(rliData.get("toVerify"), true);		
		sugar().revLineItems.createDrawer.showMore();
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(rliData.get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(rliData.get("bestCase"));
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(rliData.get("worstCase"));
		sugar().revLineItems.createDrawer.getEditField("description").set(rliData.get("description"));
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		
		// Verify 1000s separator and decimal symbol should displayed correctly - as previously modified
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertEquals(rliData.get("likelyCase_exp"), true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertEquals(rliData.get("bestCase_exp"), true);
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertEquals(rliData.get("worstCase_exp"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}