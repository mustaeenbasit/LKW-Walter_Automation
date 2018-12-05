package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_21500 extends SugarTest{
	DataSource productCatalogData;
	VoodooControl moduleCtrl, layoutCtrl, listViewCtrl, historyDefault, saveBtnCtrl;

	public void setup() throws Exception {
		productCatalogData = testData.get(testName);
		sugar.productCatalog.api.create(productCatalogData);
		sugar.login();

		// Navigate to Admin > Studio
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_ProductTemplates");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listViewCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		historyDefault = new VoodooControl("input", "id", "historyRestoreDefaultLayout");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");

		// Go to Product Catalog > Layout > List View.
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listViewCtrl.click();
		VoodooUtils.waitForReady();

		// Drag and drop Default Pricing formula field into the Default column
		new VoodooControl("a", "css" ,"li[data-name=pricing_formula]").dragNDrop(new VoodooControl("td", "id", "Default"));

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/** Verify that pricing formula calculation (sugar logic) works on list view once it sets in record view
	 */
	@Test
	public void Products_21500_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCatalog.navToListView();
		sugar.productCatalog.listView.clickRecord(1);
		sugar.productCatalog.recordView.edit();
		sugar.productCatalog.recordView.getEditField("defaultPricingFormula").set(productCatalogData.get(0).get("defaultPricingFormula"));
		sugar.productCatalog.recordView.getEditField("pricingFactor").set(productCatalogData.get(0).get("pricingFactor"));
		sugar.productCatalog.recordView.save();

		sugar.navbar.selectMenuItem(sugar.productCatalog, "viewProduct");

		sugar.productCatalog.listView.editRecord(1);
		sugar.productCatalog.listView.getEditField(1, "costPrice").scrollIntoViewIfNeeded(false);
		sugar.productCatalog.listView.getEditField(1, "costPrice").set(productCatalogData.get(0).get("listPrice"));
		sugar.productCatalog.listView.saveRecord(1);
		// Verify Unit Price updated
		sugar.productCatalog.listView.getDetailField(1, "unitPrice").assertContains("60", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}