package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_26603 extends SugarTest {
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		sugar().quotes.api.create();
		sugar().login();

		// Go to "Admin -> Product Category" page		
		sugar().admin.navToAdminPanelLink("productCategories");
		sugar().productCategories.listView.create();
		sugar().productCategories.createDrawer.getEditField("name").set(customFS.get("name1"));
		VoodooSelect productCategory = (VoodooSelect)sugar().productCategories.recordView.getEditField("parentCategory");
		productCategory.clickSearchForMore();
		sugar().productCategories.searchSelect.create();

		// TODO:VOOD-1530
		// Once this VOOD-1530 is resolved uncomment line#29, #30 and remove line#31, #32
		// sugar().productCategories.createDrawer.getEditField("name").set(testName+"_1");
		// sugar().productCategories.createDrawer.save();
		new VoodooControl("input", "css", ".active .fld_name.edit [name='name']").set(customFS.get("name2"));
		new VoodooControl("a", "css", "#drawers .active .fld_save_button a").click();

		// Save createDrawer
		sugar().productCategories.createDrawer.save();
	}

	/**
	 * Verify Product category folder is created correctly
	 * @throws Exception
	 */
	@Test
	public void Products_26603_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Quoted Module
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Edit any quote record and add extra line items to the quote until there are at least 6 line items.
		// TODO: VOOD-865
		VoodooControl addGroupCtrl = new VoodooControl("input", "id", "add_group");
		VoodooControl addRowCtrl = new VoodooControl("input", "css", "input[name='Add Row']");
		addGroupCtrl.click();
		addRowCtrl.click();
		VoodooUtils.waitForReady();

		// Click the arrow of 'Quoted Line Item' field to select Product Catalog.
		new VoodooControl("button", "id", "product_name_select_1").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// Verify that User would see a pop-up window showing product category (e.g. "newTestCateName") as a folder.
		new VoodooControl("td", "css", "#productcatalog div div div table tbody tr td:nth-child(2)").assertContains(customFS.get("name2"), true);

		// Click to expand folder list view
		new VoodooControl("td", "css", "#productcatalog div div div table tbody tr td:nth-child(1)").click();

		// Verify the name of product category (e.g. "testCatName") is shown after expansion.
		new VoodooControl("td", "css", "#productcatalog div div div div div table tbody tr td:nth-child(3)").assertContains(customFS.get("name1"), true);

		// Close open pop-up window
		VoodooUtils.closeWindow();
		VoodooUtils.focusWindow(0);
		
		// Cancel quote edition
		sugar().quotes.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
