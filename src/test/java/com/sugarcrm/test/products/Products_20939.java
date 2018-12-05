package com.sugarcrm.test.products;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20939 extends SugarTest {
	DataSource productCatalogData;
	
	public void setup() throws Exception {
		productCatalogData = testData.get(testName);
		
		sugar.login();
		sugar.productCatalog.create(productCatalogData);
	}

	/**
	 * Product Catalog - Search_Verify that product catalog can be searched by "Basic Search" function
	 * @throws Exception
	 */
	@Test
	public void Products_20939_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to "Admin -> Product Catalog" page
		sugar.productCatalog.navToListView();
		
		for (int i = 0; i < productCatalogData.size(); i++) {
			// Enter search conditions in search panel
			sugar.productCatalog.listView.setSearchString(productCatalogData.get(i).get("name"));
			VoodooUtils.waitForReady();
			
			// Verify that corresponding records displayed in product catalog list view according to the search conditions
			Assert.assertTrue("Basic Search Error: Only 1 record should be exist in listview", sugar.productCatalog.listView.countRows() == 1);
			sugar.productCatalog.listView.verifyField(1, "name", productCatalogData.get(i).get("name"));
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}