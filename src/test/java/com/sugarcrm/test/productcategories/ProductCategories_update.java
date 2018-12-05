package com.sugarcrm.test.productcategories;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCategoriesRecord;
import com.sugarcrm.test.SugarTest;

public class ProductCategories_update extends SugarTest {
	ProductCategoriesRecord myProductCategory;

	public void setup() throws Exception {
		sugar().login();
		myProductCategory = (ProductCategoriesRecord)sugar().productCategories.api.create();
	}

	@Test
	public void ProductCategories_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Marion Gadget");
		newData.put("order", "10");
		newData.put("description", "This is the demo category");

		// Edit the product category record using the UI.
		myProductCategory.edit(newData);

		// Verify the product category record was edited.
		myProductCategory.verify(newData);  

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}