package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20880 extends SugarTest {
	public void setup() throws Exception {
		// Product catalog record 
		sugar.productCatalog.api.create();

		// Create Product category
		sugar.productCategories.api.create();

		// Create Product Type
		sugar.productTypes.api.create();

		// Create Manufacturer
		sugar.manufacturers.api.create();

		// Login
		sugar.login();
	}

	/**
	 * Verify edit all fields of product catalog record.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20880_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Admin -> Product Catalog" page. 
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.clickRecord(1);

		// Click Edit
		sugar.productCatalog.recordView.edit();

		String productCategoryName = sugar.productCategories.getDefaultData().get("name");
		String productTypeName = sugar.productTypes.getDefaultData().get("name");
		String manufacturerName = sugar.manufacturers.getDefaultData().get("name");

		// Modify the information of the product catalog record, then save the modification.
		sugar.productCatalog.recordView.getEditField("name").set(testName);
		sugar.productCatalog.recordView.getEditField("productCategory").set(productCategoryName);
		sugar.productCatalog.recordView.getEditField("type").set(productTypeName);
		sugar.productCatalog.recordView.getEditField("manufacturerName").set(manufacturerName);
		sugar.productCatalog.recordView.save();

		// Verify that the Product catalog Record view is displayed. after saving
		sugar.productCatalog.recordView.assertVisible(true);

		// Verify product catalog's information is updated in record view
		sugar.productCatalog.recordView.getDetailField("name").assertContains(testName, true);
		sugar.productCatalog.recordView.getDetailField("productCategory").assertContains(productCategoryName, true);
		sugar.productCatalog.recordView.getDetailField("type").assertContains(productTypeName, true);
		sugar.productCatalog.recordView.getDetailField("manufacturerName").assertContains(manufacturerName, true);

		sugar.productCatalog.navToListView();
		// Verify product catalog's information is updated in list view
		sugar.productCatalog.listView.getDetailField(1, "name").assertContains(testName, true);
		sugar.productCatalog.listView.getDetailField(1, "productCategory").assertContains(productCategoryName, true);
		sugar.productCatalog.listView.getDetailField(1, "type").assertContains(productTypeName, true);

		// Verify product catalog's information is updated in preview
		sugar.productCatalog.listView.previewRecord(1);
		sugar.previewPane.getPreviewPaneField("name").assertContains(testName, true);
		sugar.previewPane.getPreviewPaneField("productCategory").assertContains(productCategoryName, true);
		sugar.previewPane.getPreviewPaneField("type").assertContains(productTypeName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}