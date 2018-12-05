package com.sugarcrm.test.products;

import org.junit.Test; 
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCatalogRecord;
import com.sugarcrm.test.SugarTest;

public class Products_delete extends SugarTest {
	ProductCatalogRecord myProduct;

	public void setup() throws Exception {
		sugar.login();
		myProduct = (ProductCatalogRecord)sugar.productCatalog.create();
	}

	@Test
	public void Products_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the product catalog record using the UI.
		myProduct.delete();

	 	// Verify the product catalog record was deleted.
		sugar.productCatalog.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}