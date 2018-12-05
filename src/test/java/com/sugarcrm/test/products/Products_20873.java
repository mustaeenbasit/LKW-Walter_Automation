package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20873 extends SugarTest {

	public void setup() throws Exception {
		sugar.productCatalog.api.create();
		sugar.login();			
	}
	/**
	 * Product Catalog_Verify that pricing formula is calculated correctly, including decimal
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20873_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.clickRecord(1);

		// Each of 5 data rows have Default Pricing Formula type and price values (some have Pricing Factor also)
		DataSource productData = testData.get(testName);
		int count =  productData.size();
		for (int i = 0; i < count; i++) {
			sugar.productCatalog.recordView.edit();
			sugar.productCatalog.recordView.getEditField("defaultPricingFormula").set(productData.get(i).get("defaultPricingFormula"));

			if (productData.get(i).get("pricingFactor").isEmpty())			
				sugar.productCatalog.recordView.getEditField("pricingFactor").assertVisible(false);
			else
				sugar.productCatalog.recordView.getEditField("pricingFactor").set(productData.get(i).get("pricingFactor"));

			// Verify the values before save
			sugar.productCatalog.recordView.getEditField("description").click();
			sugar.productCatalog.recordView.getEditField("unitPrice").assertContains(productData.get(i).get("unitPrice"), true);
			sugar.productCatalog.recordView.getEditField("listPrice").assertContains(productData.get(i).get("listPrice"), true);
			sugar.productCatalog.recordView.getEditField("discountPriceUSD").assertContains(productData.get(i).get("discountPrice"), true);

			// Verify the values after save
			sugar.productCatalog.recordView.save();
			sugar.productCatalog.recordView.getDetailField("unitPrice").assertContains(productData.get(i).get("unitPrice"), true);
			sugar.productCatalog.recordView.getDetailField("listPrice").assertContains(productData.get(i).get("listPrice"), true);
			sugar.productCatalog.recordView.getDetailField("discountPriceUSD").assertContains(productData.get(i).get("discountPrice"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}