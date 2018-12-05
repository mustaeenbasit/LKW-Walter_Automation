package com.sugarcrm.test.quotedLineItems;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class QuotedLineItems_21488 extends SugarTest {
	FieldSet productData;

	public void setup() throws Exception {
		productData = testData.get(testName).get(0);

		sugar().login();

		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);

		sugar().productCatalog.create(productData);
	}

	/**
	 * Verify Quoted Line Item can be created using nav menu
	 *
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_21488_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().quotedLineItems.navToListView();
		sugar().navbar.selectMenuItem(sugar().quotedLineItems, "createQuotedLineItem");

		// Creating the Quoted Line Item
		sugar().quotedLineItems.createDrawer.getEditField("productTemplate").set(productData.get("name"));

		// Verifying that the Quoted Line Item name is auto-populated, with name same as the product catalog
		sugar().quotedLineItems.createDrawer.getEditField("name").assertEquals(productData.get("name"), true);
		sugar().quotedLineItems.createDrawer.save();

		// Verify that a QLI is saved
		sugar().quotedLineItems.listView.verifyField(1, "name", productData.get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}