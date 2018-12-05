package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26498 extends SugarTest {
	DataSource customDataValues;
	VoodooControl productCatalogCtrl;

	public void setup() throws Exception {
		customDataValues = testData.get(testName);
		// Product Categories
		sugar().productCategories.api.create(customDataValues);
		sugar().login();
	}

	/**
	 * Verify that selected product category is populated in the "Category"
	 * dropdown when creating new RLI
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26498_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to RLI module and click 'Create" button
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Expand "Product Category" dropdown and click Search For More 
		VoodooSelect productCategory = (VoodooSelect) sugar().revLineItems.createDrawer.getEditField("category");
		productCategory.clickSearchForMore();

		// Select any product category. 
		sugar().productCategories.searchSelect.search(customDataValues.get(0).get("name"));
		sugar().productCategories.searchSelect.selectRecord(1);

		// Verify that, this will return you to RLI create drawer with selected product category in Category dropdown
		sugar().revLineItems.createDrawer.assertVisible(true);
		productCategory.assertContains(customDataValues.get(0).get("name"), true);

		// Expand dropdown again and click Search For More
		productCategory.clickSearchForMore();

		// Select another product category from the list
		sugar().productCategories.searchSelect.search(customDataValues.get(1).get("name"));
		sugar().productCategories.searchSelect.selectRecord(1);

		// Verify that, this will return you to RLI create drawer with selected product category in Category dropdown
		sugar().revLineItems.createDrawer.assertVisible(true);
		productCategory.assertContains(customDataValues.get(1).get("name"), true);

		// Cancel the create drawer
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}