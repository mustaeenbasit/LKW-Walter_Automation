package com.sugarcrm.test.products;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductCategoriesRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Products_20920 extends SugarTest {
	DataSource ds;
	ProductCategoriesRecord myProd1, myProd2;
	
	public void setup() throws Exception {
		sugar.login();			
		ds= testData.get(testName);
		
		// create product category
		myProd1 = (ProductCategoriesRecord)sugar.productCategories.api.create(ds.get(0));
		myProd2 = (ProductCategoriesRecord)sugar.productCategories.api.create(ds.get(1));
	}

	/**
	 *  Verify that product category can be edited.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20920_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.productCategories.navToListView();
		sugar.productCategories.listView.clickRecord(1);

		// edit product category
		sugar.productCategories.recordView.edit();
		sugar.productCategories.recordView.getEditField("name").set(ds.get(2).get("name"));
		sugar.productCategories.recordView.getEditField("parentCategory").set(ds.get(0).get("name"));
		sugar.productCategories.recordView.getEditField("order").set(ds.get(2).get("order"));
		sugar.productCategories.recordView.getEditField("description").set(ds.get(2).get("description"));
		sugar.productCategories.recordView.save();
		
		// check record on record view
		sugar.productCategories.recordView.getDetailField("name").assertEquals(ds.get(2).get("name"), true);
		sugar.productCategories.recordView.getDetailField("parentCategory").assertEquals(ds.get(0).get("name"), true);
		sugar.productCategories.recordView.getDetailField("description").assertEquals(ds.get(2).get("description"), true);
		sugar.productCategories.recordView.getDetailField("order").assertEquals(ds.get(2).get("order"),true);
		
		// check record in list view
		sugar.productCategories.navToListView();
		sugar.productCategories.listView.verifyField(1, "name", ds.get(2).get("name"));
		sugar.productCategories.listView.verifyField(1, "parentCategory", ds.get(0).get("name"));
		sugar.productCategories.listView.verifyField(1, "description", ds.get(2).get("description"));
		sugar.productCategories.listView.verifyField(1, "order", ds.get(2).get("order"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}