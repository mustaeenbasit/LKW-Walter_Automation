package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20924 extends SugarTest {
	DataSource productCategoriesData;

	public void setup() throws Exception {
		productCategoriesData = testData.get(testName);

		// Create 2 Product Categories
		sugar.productCategories.api.create(productCategoriesData);

		sugar.login();
		sugar.productCategories.navToListView();

		// Add the value of "Parent Category" Related field in both the "Product Category" Records
		// TODO: VOOD-1519 - Incorrect Selectors for the ListView Edit fields in the Product Categories CSV file
		sugar.productCategories.listView.editRecord(1);
		new VoodooSelect("span", "css", ".fld_parent_name.edit .select2-chosen").set(productCategoriesData.get(1).get("name"));
		sugar.productCategories.listView.saveRecord(1);

		sugar.productCategories.listView.editRecord(2);
		new VoodooSelect("span", "css", ".fld_parent_name.edit .select2-chosen").set(productCategoriesData.get(0).get("name"));
		sugar.productCategories.listView.saveRecord(2);
	}

	/**
	 * Product Categories - Sort_Verify that product categories can be sorted by column titles in list view.
	 * @throws Exception
	 */
	@Test
	public void Products_20924_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sort By Product Category : ASC
		sugar.productCategories.listView.sortBy("headerName", true);
		sugar.productCategories.listView.verifyField(1, "name", productCategoriesData.get(1).get("name"));
		sugar.productCategories.listView.verifyField(2, "name", productCategoriesData.get(0).get("name"));

		// Sort By Product Category : DESC
		sugar.productCategories.listView.sortBy("headerName", false);
		sugar.productCategories.listView.verifyField(1, "name", productCategoriesData.get(0).get("name"));
		sugar.productCategories.listView.verifyField(2, "name", productCategoriesData.get(1).get("name"));

		// Sort By Parent Category : ASC
		sugar.productCategories.listView.sortBy("headerParentname", true);
		sugar.productCategories.listView.verifyField(1, "parentCategory", productCategoriesData.get(1).get("name"));
		sugar.productCategories.listView.verifyField(2, "parentCategory", productCategoriesData.get(0).get("name"));

		// Sort By Parent Category : DESC
		sugar.productCategories.listView.sortBy("headerParentname", false);
		sugar.productCategories.listView.verifyField(1, "parentCategory", productCategoriesData.get(0).get("name"));
		sugar.productCategories.listView.verifyField(2, "parentCategory", productCategoriesData.get(1).get("name"));

		// Sort By Description : ASC
		sugar.productCategories.listView.sortBy("headerDescription", true);
		sugar.productCategories.listView.verifyField(1, "description", productCategoriesData.get(0).get("description"));
		sugar.productCategories.listView.verifyField(2, "description", productCategoriesData.get(1).get("description"));

		// Sort By Description : DESC
		sugar.productCategories.listView.sortBy("headerDescription", false);
		sugar.productCategories.listView.verifyField(1, "description", productCategoriesData.get(1).get("description"));
		sugar.productCategories.listView.verifyField(2, "description", productCategoriesData.get(0).get("description"));

		// Sort By Order : ASC
		sugar.productCategories.listView.sortBy("headerListorder", true);
		sugar.productCategories.listView.verifyField(1, "order", productCategoriesData.get(0).get("order"));
		sugar.productCategories.listView.verifyField(2, "order", productCategoriesData.get(1).get("order"));

		// Sort By Order : DESC
		sugar.productCategories.listView.sortBy("headerListorder", false);
		sugar.productCategories.listView.verifyField(1, "order", productCategoriesData.get(1).get("order"));
		sugar.productCategories.listView.verifyField(2, "order", productCategoriesData.get(0).get("order"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}