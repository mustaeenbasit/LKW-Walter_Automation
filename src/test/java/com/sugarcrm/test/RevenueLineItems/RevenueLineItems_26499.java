package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26499 extends SugarTest {
	String productCategory;

	public void setup() throws Exception {
		// Product categories
		sugar().productCategories.api.create();
		// Product Catalog
		sugar().productCatalog.api.create();

		// Login as admin user
		sugar().login();

		// Link product catalog to product category 
		// TODO: VOOD-444
		productCategory = sugar().productCategories.getDefaultData().get("name");
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.editRecord(1);
		sugar().productCatalog.listView.getEditField(1, "productCategory").set(productCategory);

		// Save 
		sugar().productCatalog.listView.saveRecord(1);
	}

	/**
	 * TC 26498: Verify that selected product is populated in the 'Product' dropdown after it was selected when create new RLI
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26499_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to RLI module and click 'Create" button
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		//  Expand Product drop down and click 'Search For More'
		VoodooSelect productCatalogCtrl = (VoodooSelect) sugar().revLineItems.createDrawer.getEditField("product");
		productCatalogCtrl.clickSearchForMore();

		// Select any product
		sugar().productCatalog.searchSelect.selectRecord(1);

		// Verify that the user is returned to create drawer. Selected product is populated in the 'Product' dropdown
		sugar().revLineItems.createDrawer.assertVisible(true);
		productCatalogCtrl.assertContains(sugar().productCatalog.getDefaultData().get("name"), true);

		// Verify that the corresponded product category is populated in 'Product Category' dropdown
		sugar().revLineItems.createDrawer.getEditField("category").assertContains(productCategory, true);

		// Verify that Product Category field becomes read only after product is selected
		// TODO: VOOD-1445
		new VoodooControl("span", "css", ".fld_category_name.edit").assertAttribute("class", "disabled");

		// Cancel the RLI create drawer
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}