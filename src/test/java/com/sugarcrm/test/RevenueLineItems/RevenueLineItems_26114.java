package com.sugarcrm.test.RevenueLineItems;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26114 extends SugarTest {
	FieldSet decimalSepFS;

	public void setup() throws Exception {
		FieldSet currencyData = testData.get(testName+"_currency").get(0);
		sugar().accounts.api.create();
		sugar().productCatalog.api.create();
		sugar().login();

		// Add new Currency
		sugar().admin.setCurrency(currencyData);

		// Change thousands separator to "." and decimal separator to ","  in user preferences
		decimalSepFS = testData.get(testName+"_sep").get(0); 
		sugar().users.setPrefs(decimalSepFS);
	}	

	/**
	 * Verify that Unit Price is calculated properly in case decimal separator is comma and thousands separator is dot.
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26114_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Select created product
		sugar().revLineItems.createDrawer.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));

		// Verify that the unit price number is populated. The number is displayed in base currency and could not be edited 
		VoodooControl unitPriceCtrl = sugar().revLineItems.createDrawer.getEditField("unitPrice");
		assertTrue("The unitPrice input field is still enabled.",unitPriceCtrl.isDisabled());
		FieldSet unitPrice = testData.get(testName).get(0);
		unitPriceCtrl.assertContains(unitPrice.get("unitPrice"), true);

		// Change currency US Dollar to EURO
		// TODO: VOOD-983
		new VoodooControl("span", "css", "div[data-name='likely_case'] .currency.edit.fld_currency_id").click();
		VoodooControl currencySelect = new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(2)");
		currencySelect.waitForVisible();
		currencySelect.click();

		// Verify that unit price number does not change after currency is changed  
		unitPriceCtrl.assertContains(unitPrice.get("unitPrice"), true);

		// Click on Cancel button
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}