package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20879 extends SugarTest {
	DataSource ds;
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Verify that creating product catalog can be canceled.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20879_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		ds = testData.get(testName);	
		sugar.productCatalog.navToListView();
		sugar.navbar.selectMenuItem(sugar.productCatalog, "createProduct");
		sugar.productCatalog.createDrawer.getEditField("name").set(ds.get(0).get("name"));
		sugar.productCatalog.createDrawer.getEditField("stockQuantity").set(ds.get(0).get("quantity"));
		sugar.productCatalog.createDrawer.getEditField("costPrice").set(ds.get(0).get("cost"));
		sugar.productCatalog.createDrawer.getEditField("unitPrice").set(ds.get(0).get("unit"));
		sugar.productCatalog.createDrawer.getEditField("listPrice").set(ds.get(0).get("list"));		
		sugar.productCatalog.createDrawer.cancel();
		// Verify that The record is not displayed in product catalog list view.
		sugar.productCatalog.listView.assertIsEmpty();
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}