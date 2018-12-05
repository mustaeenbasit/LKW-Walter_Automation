package com.sugarcrm.test.projects;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Projects_27624 extends SugarTest {
	FieldSet currencyAndProductData,currencyData,currencyNewData;

	public void setup() throws Exception {
		currencyAndProductData = testData.get(testName).get(0);
		sugar.login();

		// Custom currency currency is added with rate 0.9 to base currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", currencyAndProductData.get("rate"));
		currencyData.put("currencySymbol", currencyAndProductData.get("symbol"));
		currencyData.put("ISOcode", currencyAndProductData.get("code"));
		sugar.admin.setCurrency(currencyData);
	}

	/**
	 * Verify that Base Currency is being updated correctly in Product record view after currency rate is changed
	 * @throws Exception
	 */
	@Test
	public void Projects_27624_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> Product Catalog
		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.create();
		// Create a product catalog with list price mentioned $100.00
		sugar.productCatalog.createDrawer.getEditField("name").set(currencyAndProductData.get("name"));
		sugar.productCatalog.createDrawer.getEditField("costPrice").set(currencyAndProductData.get("cost"));
		sugar.productCatalog.createDrawer.getEditField("unitPrice").set(currencyAndProductData.get("unitPrice"));
		sugar.productCatalog.createDrawer.getEditField("listPrice").set(currencyAndProductData.get("listPrice"));
		sugar.productCatalog.createDrawer.save();

		// Go to user profile -> Edit -> Advanced Tab and select Custom currency for the preferred currency and Tick "Show Preferred Currency" checkbox. -> Save
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.users.userPref.getControl("edit").click();
		sugar.users.userPref.getControl("tab4").click();
		VoodooUtils.waitForReady();
		sugar.users.userPref.getControl("advanced_preferedCurrency").set(testName+" : "+currencyAndProductData.get("symbol"));
		sugar.users.userPref.getControl("advanced_showpreferedCurrency").click();
		sugar.users.userPref.getControl("save").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Change the Custom currency conversion rate to 0.75 and Save it
		currencyNewData = new FieldSet();
		currencyNewData.put("conversionRate", currencyAndProductData.get("newRate"));
		sugar.admin.editCurrency(currencyData.get("currencyName"), currencyNewData);

		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.clickRecord(1);

		// Verify that The List Price is 0.75 of List_USD i.e. in this case: List Price_USD:$100 and List Price: €75
		sugar.productCatalog.recordView.getDetailField("listPrice").assertContains(currencyAndProductData.get("listPriceE"), true);
		sugar.productCatalog.recordView.getDetailField("listPrice").assertContains(currencyAndProductData.get("listPriceUSD"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}