package com.sugarcrm.test.productcategories;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCategoriesRecord;
import com.sugarcrm.test.SugarTest;

public class ProductCategories_delete extends SugarTest {
	ProductCategoriesRecord myProductCategory;

	public void setup() throws Exception {
		sugar().login();
		myProductCategory = (ProductCategoriesRecord)sugar().productCategories.create();
	}

	@Test
	public void ProductCategories_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the product category record using the UI.
		myProductCategory.delete();

		// Verify the product category record was deleted.
		sugar().productCategories.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}