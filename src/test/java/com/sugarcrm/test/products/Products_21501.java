package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_21501 extends SugarTest {
	FieldSet currencyFs, qliFs;
	VoodooControl productCatalogCtrl, currencyCtrl, currencySaveCtrl;
	public void setup() throws Exception {
		currencyFs = testData.get(testName+"_1").get(0);
		qliFs = testData.get(testName).get(0);
		sugar.login();

		// Create test Currency
		currencyFs.put("currencyName", testName);
		sugar.admin.setCurrency(currencyFs);
	}

	/**
	 * Verify that Prices in currencies other than dollars is calculated correctly in Product Catalog listview.
	 * @throws Exception
	 */
	@Test
	public void Products_21501_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new Product Catalog. Use custom currency.Set cost=200, list=500, unit price = 1000
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.create();
		sugar.productCatalog.createDrawer.getEditField("name").set(qliFs.get("name"));
		// TODO: VOOD-983
		VoodooControl priceCtrl = new VoodooControl("div", "css", ".select2-results :nth-child(2) div");
		new VoodooControl("span", "css", "span[data-voodoo-name='cost_price'] span[data-voodoo-name='currency_id']").click();
		priceCtrl.click();
		sugar.productCatalog.createDrawer.getEditField("costPrice").set(qliFs.get("cost"));
		sugar.productCatalog.createDrawer.getEditField("unitPrice").set(qliFs.get("unitPrice"));
		sugar.productCatalog.createDrawer.getEditField("listPrice").set(qliFs.get("listPrice"));
		sugar.productCatalog.createDrawer.save();
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.getDetailField(1, "costPrice").assertContains(qliFs.get("cost"), true);
		sugar.productCatalog.listView.getDetailField(1, "unitPrice").scrollIntoViewIfNeeded(false);
		sugar.productCatalog.listView.getDetailField(1, "unitPrice").assertContains(qliFs.get("verifyUnitPrice"), true);
		sugar.productCatalog.listView.getDetailField(1, "listPrice").assertContains(qliFs.get("listPrice"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}