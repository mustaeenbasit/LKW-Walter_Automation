package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_29430 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that 'Quantity in Stock' and 'Weight' fields can accept negative amounts
	 * 
	 * @throws Exception
	 */
	@Test
	public void Products_29430_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Product Category
		sugar().productCategories.navToListView();

		// Go to Product Category, Click Create 'Product for catalog'.
		sugar().navbar.selectMenuItem(sugar().productCategories, "createProductCatalog");

		FieldSet fs = sugar().productCatalog.getDefaultData();
		// Enter product name
		sugar().productCatalog.createDrawer.getEditField("name").set(fs.get("name"));

		// Enter 'weight' and 'Quantity in Stock' as some negative values.
		FieldSet customData = testData.get(testName).get(0);
		sugar().productCatalog.createDrawer.getEditField("stockQuantity").set(customData.get("qtyInStock"));
		sugar().productCatalog.createDrawer.getEditField("weight").set(customData.get("weight"));

		// Enter values in all required field
		sugar().productCatalog.createDrawer.getEditField("costPrice").set(fs.get("costPrice"));
		sugar().productCatalog.createDrawer.getEditField("unitPrice").set(fs.get("unitPrice"));
		sugar().productCatalog.createDrawer.getEditField("listPrice").set(fs.get("listPrice"));

		// Click on Save button
		sugar().productCatalog.createDrawer.save();

		// Verify Product is successfully created with negative 'Quantity in stock' and negative 'weight'
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.getDetailField(1, "name").assertEquals(fs.get("name"), true);
		sugar().productCatalog.listView.clickRecord(1);
		sugar().productCatalog.recordView.getDetailField("stockQuantity").assertEquals(customData.get("qtyInStock"), true);
		sugar().productCatalog.recordView.getDetailField("weight").assertEquals(customData.get("weight"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}