package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18186 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().productCatalog.api.create(ds);
		FieldSet fs = new FieldSet();
		fs.put("name", ds.get(0).get("productCategory"));
		sugar().productCategories.api.create(fs);		
		fs.put("name", ds.get(1).get("productCategory"));
		sugar().productCategories.api.create(fs);		
		sugar().login();

		// TODO: VOOD-444
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.editRecord(1);
		sugar().productCatalog.listView.getEditField(1, "productCategory").set(ds.get(1).get("productCategory"));
		sugar().productCatalog.listView.saveRecord(1);
		sugar().productCatalog.listView.editRecord(2);
		sugar().productCatalog.listView.getEditField(2, "productCategory").set(ds.get(0).get("productCategory"));
		sugar().productCatalog.listView.saveRecord(2);
	}

	/**
	 * Verify that the existing product category name gets overridden when a product is selected
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18186_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Revenue Line Item module
		sugar().revLineItems.navToListView();
		// Click Create button
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("category").set(ds.get(1).get("productCategory"));

		VoodooUtils.waitForReady();
		
		// Verify that the Category Name field is populated with the selected record.
		sugar().revLineItems.createDrawer.getEditField("category").assertEquals(ds.get(1).get("productCategory"), true);

		// TODO: VOOD-1445
		// Verify that the Category Name field is still editable (not read only).
		new VoodooControl("span", "css", ".fld_category_name.edit").assertAttribute("class", "disabled", false);

		// In the Product field select the product associated with different product category
		sugar().revLineItems.createDrawer.getEditField("product").set(ds.get(0).get("name"));

		VoodooUtils.waitForReady();
		
		// Verify that the Category Name field's value is changed based on the selected product
		sugar().revLineItems.createDrawer.getEditField("category").assertEquals(ds.get(0).get("productCategory"), true);

		// TODO: VOOD-1445
		// Verify that the category name field becomes read only
		new VoodooControl("span", "css", ".fld_category_name.edit").assertAttribute("class", "disabled");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}