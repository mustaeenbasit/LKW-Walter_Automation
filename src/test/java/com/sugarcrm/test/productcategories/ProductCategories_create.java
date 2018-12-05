package com.sugarcrm.test.productcategories;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCategoriesRecord;
import com.sugarcrm.test.SugarTest;

public class ProductCategories_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void ProductCategories_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ProductCategoriesRecord myProductCategory = (ProductCategoriesRecord)sugar().productCategories.create();
		myProductCategory.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}