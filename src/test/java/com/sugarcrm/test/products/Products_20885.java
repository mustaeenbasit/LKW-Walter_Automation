package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20885 extends SugarTest {
	FieldSet fs;
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);	
		sugar.productCatalog.api.create(fs);
		sugar.login();	
	}

	/**
	 * Verify that deleting product catalog can be canceled in detail view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20885_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.clickRecord(1);
		sugar.productCatalog.recordView.delete();
		sugar.alerts.cancelAllAlerts();

		// stay origin record's record view
		sugar.productCatalog.recordView.getDetailField("name").assertEquals(fs.get("name"),true);
		sugar.productCatalog.recordView.getDetailField("webSite").assertEquals(fs.get("webSite"),true);
		sugar.productCatalog.recordView.getDetailField("stockQuantity").assertEquals(fs.get("stockQuantity"),true);
		sugar.productCatalog.recordView.getDetailField("costPrice").assertContains(fs.get("costPrice"),true);
		sugar.productCatalog.recordView.getDetailField("unitPrice").assertContains(fs.get("unitPrice"),true);
		sugar.productCatalog.recordView.getDetailField("listPrice").assertContains(fs.get("listPrice"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}