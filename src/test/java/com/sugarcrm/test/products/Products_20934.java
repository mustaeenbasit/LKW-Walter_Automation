package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20934 extends SugarTest {
	public void setup() throws Exception {
		sugar.productTypes.api.create();
		sugar.login();			
	}
	/**
	 * Product Type - Edit_Verify that product type can be edited.
	 * @throws Exception
	 */
	@Test
	public void Products_20934_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Go to "Admin -> Product Types" view
		sugar.productTypes.navToListView();

		// Click a product type's name
		sugar.productTypes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		//  Modify the information of the product type
		sugar.productTypes.editView.getEditField("name").set(customData.get("product_Type_Edit"));
		sugar.productTypes.editView.getEditField("description").set(customData.get("product_Description_Edit"));
		VoodooUtils.focusDefault();

		// Save the modification
		sugar.productTypes.editView.save();

		// Verify that the modified product type is displayed in list view
		sugar.productTypes.listView.verifyField(1,"name",customData.get("product_Type_Edit"));
		sugar.productTypes.listView.verifyField(1,"description",customData.get("product_Description_Edit"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}