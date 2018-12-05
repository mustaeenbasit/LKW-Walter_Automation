package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_28133 extends SugarTest {
	DataSource productCatalogData;
	
	public void setup() throws Exception {
		productCatalogData = testData.get(testName);
		sugar.productCategories.api.create(productCatalogData);
		sugar.login();
		
		// Go to "Admin -> Product Category" page
		sugar.productCategories.navToListView();
		sugar.productCategories.listView.clickRecord(1);
		sugar.productCategories.recordView.edit();
		sugar.productCategories.recordView.getEditField("parentCategory").set(productCatalogData.get(0).get("name"));
		sugar.productCategories.recordView.save();
	}

	/**
	 * Verify the consistent behaviour of Product Categories
	 * @throws Exception
	 */
	@Test
	public void Products_28133_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to "Admin -> Product Category" page
		sugar.productCategories.navToListView();
		sugar.productCategories.listView.create();
		sugar.productCategories.createDrawer.getEditField("name").set(testName);
		sugar.productCategories.createDrawer.getEditField("parentCategory").set(productCatalogData.get(1).get("name"));
		sugar.productCategories.createDrawer.save();
		
		// Verify that "Product2" should appears in the "Test's" Parent Category column of the list view on first row.
		sugar.productCategories.listView.verifyField(1, "parentCategory", productCatalogData.get(1).get("name"));
		
		// Open the Test to get to the record view. You will see that the Parent Category is present and if you click it, it'll take you to the record.
		sugar.productCategories.listView.clickRecord(1);
		sugar.productCategories.recordView.getDetailField("parentCategory").click();
		
		// Verify that parent category on recordView
		VoodooControl nameFieldCtrl = sugar.productCategories.recordView.getDetailField("name");
		nameFieldCtrl.assertEquals(productCatalogData.get(1).get("name"), true);
		
		// Go back to listView
		sugar.productCategories.navToListView();
		
		// Verify that "Product1" should appears in the "Test's" Parent Category column of the list view on second row.
		sugar.productCategories.listView.verifyField(2, "parentCategory", productCatalogData.get(0).get("name"));
		
		// Assuming you haven't altered the demo data, click the Parent category that says "Product1".
		sugar.productCategories.listView.getDetailField(2, "parentCategory").click();
		
		// Verify that "Product1" record opens.
		nameFieldCtrl.assertEquals(productCatalogData.get(0).get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}