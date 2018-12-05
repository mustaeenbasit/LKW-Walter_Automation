package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_28566 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that user is NOT able to save a record while field is required in Product Catalog module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Products_28566_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Product Catalog module
		sugar.productCatalog.navToListView();
		// Click on Create button
		sugar.productCatalog.listView.create();
		// Enter values in all required field
		sugar.productCatalog.createDrawer.getEditField("name").set(sugar.productCatalog.getDefaultData().get("name"));
		sugar.productCatalog.createDrawer.getEditField("costPrice").set(sugar.productCatalog.getDefaultData().get("costPrice"));
		sugar.productCatalog.createDrawer.getEditField("unitPrice").set(sugar.productCatalog.getDefaultData().get("unitPrice"));
		sugar.productCatalog.createDrawer.getEditField("listPrice").set(sugar.productCatalog.getDefaultData().get("listPrice"));
		// Select the "Profit Margin" option in Default Pricing Formula field
		sugar.productCatalog.createDrawer.getEditField("defaultPricingFormula").set("Profit Margin");
		// Change back the "Fixed Price" option in Default Pricing Formula field
		sugar.productCatalog.createDrawer.getEditField("defaultPricingFormula").set(sugar.productCatalog.getDefaultData().get("defaultPricingFormula"));
		// Click on Save button
		sugar.productCatalog.createDrawer.save();
		// Verify that Alert message is displayed about missing value in the required Unit Price field. 
		sugar.alerts.getError().assertVisible(true);
		// Verify that Unit Price field is highlighted 
		sugar.productCatalog.createDrawer.getEditField("unitPrice").assertAttribute("class", "required");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}