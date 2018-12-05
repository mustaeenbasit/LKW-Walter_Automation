package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20917 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds= testData.get(testName);	

		// create parent product category
		FieldSet categoryName = new FieldSet();
		categoryName.put("name", ds.get(0).get("name"));
		sugar().productCategories.api.create(categoryName);
		sugar().login();
	}

	/**
	 * Verify that product category can be created with all fields entered.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20917_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// create product category with all fields
		sugar().productCategories.navToListView();
		sugar().productCategories.listView.create();
		sugar().productCategories.createDrawer.getEditField("name").set(ds.get(1).get("name"));
		// select parent category
		sugar().productCategories.createDrawer.getEditField("parentCategory").set(ds.get(0).get("name"));
		sugar().productCategories.createDrawer.getEditField("order").set(ds.get(1).get("order"));
		sugar().productCategories.createDrawer.getEditField("description").set(ds.get(1).get("description"));

		// Saving product categories record
		sugar().productCategories.createDrawer.save();
		VoodooUtils.waitForReady();

		//check record in list view
		sugar().productCategories.listView.getDetailField(1, "name").assertEquals(ds.get(1).get("name"),true);
		sugar().productCategories.listView.getDetailField(1, "parentCategory").assertEquals(ds.get(0).get("name"),true);
		sugar().productCategories.listView.getDetailField(1, "description").assertEquals(ds.get(1).get("description"),true);
		sugar().productCategories.listView.getDetailField(1, "order").assertEquals(ds.get(1).get("order"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}