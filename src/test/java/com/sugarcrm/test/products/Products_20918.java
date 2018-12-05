package com.sugarcrm.test.products;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Products_20918 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds= testData.get(testName);	

		// create parent product category
		FieldSet data = new FieldSet();
		data.put("name", ds.get(0).get("name"));
		sugar().productCategories.api.create(data);
		sugar().login();
	}

	/**
	 * Verify that "Parent Category" field can be cleared by clicking "Clear" button next to it.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20918_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// create another product category
		sugar().productCategories.navToListView();
		sugar().productCategories.listView.create();
		sugar().productCategories.createDrawer.getEditField("name").set(ds.get(1).get("name"));

		// select parent category
		VoodooControl parentCategoryCtrl = sugar().productCategories.createDrawer.getEditField("parentCategory");
		parentCategoryCtrl.set(ds.get(0).get("name"));
		parentCategoryCtrl.assertEquals(ds.get(0).get("name"), true);

		// clear parent category
		// TODO: VOOD-806
		new VoodooControl("abbr", "css", "span.fld_parent_name.edit abbr.select2-search-choice-close").click();
		VoodooUtils.waitForReady();
		parentCategoryCtrl.assertEquals(ds.get(0).get("assert"), true);
		VoodooUtils.waitForReady();

		// Saving Product categories record
		sugar().productCategories.createDrawer.save();
		VoodooUtils.waitForReady();

		//check record in list view
		sugar().productCategories.listView.getDetailField(1, "name").assertEquals(ds.get(1).get("name"), true);
		sugar().productCategories.listView.getDetailField(1, "parentCategory").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}