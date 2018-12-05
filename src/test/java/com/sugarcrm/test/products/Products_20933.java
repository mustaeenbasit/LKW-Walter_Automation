package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20933 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();			
	}

	/**
	 * Product Type - Create_Verify that product type can be saved by clicking "Save & Create New" button.
	 * @throws Exception
	 */
	@Test
	public void Products_20933_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData =testData.get(testName).get(0);

		// Go to "Admin -> Product Types" view
		sugar.productTypes.navToListView();

		// Create a product type
		sugar.productTypes.listView.clickCreate();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.productTypes.editView.getEditField("name").set(customData.get("name"));
		sugar.productTypes.editView.getEditField("description").set(customData.get("description"));
		sugar.productTypes.editView.getEditField("order").set(customData.get("order"));
		VoodooUtils.focusDefault();

		// Click "Save & Create New" button
		sugar.productTypes.editView.saveAndCreateNew();

		// Verify that the newly created record is displayed in product type list view
		sugar.productTypes.listView.verifyField(1,"name",customData.get("name"));
		sugar.productTypes.listView.verifyField(1,"description",customData.get("description"));
		sugar.productTypes.listView.verifyField(1,"order",customData.get("order"));

		// Verify that All the fields in create view are empty
		VoodooUtils.focusFrame("bwc-frame");
		sugar.productTypes.editView.getEditField("name").assertEquals("", true);
		sugar.productTypes.editView.getEditField("description").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}