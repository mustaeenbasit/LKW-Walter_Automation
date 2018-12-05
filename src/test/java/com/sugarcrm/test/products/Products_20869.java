package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20869 extends SugarTest {
	public void setup() throws Exception {
		sugar().productCategories.api.create();
		sugar().login();			
	}

	/**
	 * Verify that the created product category is displayed in the pop up window when creating product catalog.
	 * @throws Exception
	 */
	@Test
	public void Products_20869_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to product catalog create page
		sugar().productCategories.navToListView();
		sugar().navbar.selectMenuItem(sugar().productCategories, "createProductCatalog");

		// Verify Product Category created above is listed in Search and Select Product Categories" list.
		VoodooSelect productCategory = (VoodooSelect)sugar().productCatalog.createDrawer.getEditField("productCategory");
		productCategory.click();
		productCategory.selectWidget.getControl("searchForMoreLink").click();
		// TODO: VOOD-1162 - Need lib support for search & select Team drawer
		new VoodooControl("span", "css", ".search-and-select .list.fld_name").assertElementContains(sugar().productCategories.getDefaultData().get("name"),true);
		sugar().productCategories.searchSelect.cancel();
		sugar().productCatalog.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
