package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCatalogRecord;
import com.sugarcrm.test.SugarTest;

public class Products_20872 extends SugarTest {
	ProductCatalogRecord product;
	public void setup() throws Exception {
		FieldSet fs = testData.get(testName).get(0);	
		product = (ProductCatalogRecord)sugar.productCatalog.api.create(fs);
		sugar.login();
	}

	/**
	 * Verify that user can go to the web site by clicking URL in "Product URL" field of a product record view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20872_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		product.navToRecord();
		// click product url on record view
		sugar.productCatalog.recordView.getDetailField("webSite").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("div", "css", ".site_logo").assertVisible(true);
		VoodooUtils.closeWindow();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}