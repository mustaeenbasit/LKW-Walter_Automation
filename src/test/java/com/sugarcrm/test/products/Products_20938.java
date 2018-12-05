package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20938 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		sugar.productCatalog.create();
		}

	/**
	 * Product Catalog - Delete_Verify that product catalog can be deleted in detail view.
	 * @throws Exception
	 */
	@Test
	public void Products_20938_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to "Admin -> Product Catalog" page
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.clickRecord(1);
		sugar.productCatalog.recordView.delete();
		sugar.alerts.confirmAllWarning();
		
		// Verify corresponding records in product catalog list view according to the search conditions
		sugar.productCatalog.listView.assertIsEmpty();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}