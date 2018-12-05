package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_29435 extends SugarTest {
	public void setup() throws Exception {
		// Create Product Catalog record with all mandatory fields
		sugar().productCatalog.api.create();
		sugar.login();			
	}

	/**
	 * Verify that Unit Price Field does not gets blank while Editing a Product Catalog
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_29435_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet productData = testData.get(testName).get(0);
		
		// Navigate to Admin-> Product Catalog
		sugar().productCatalog.navToListView();
		
		// Click the Record just created and click Edit
		sugar().productCatalog.listView.clickRecord(1);
		sugar().productCatalog.recordView.edit();
		
		// Verify that all the Values entered including the unit price remain Intact
		sugar().productCatalog.createDrawer.getEditField("name").assertEquals(sugar().productCatalog.getDefaultData().get("name"), true);
		sugar().productCatalog.createDrawer.getEditField("costPrice").assertEquals(productData.get("price"), true);
		sugar().productCatalog.createDrawer.getEditField("unitPrice").assertEquals(productData.get("price"), true);
		sugar().productCatalog.createDrawer.getEditField("listPrice").assertEquals(productData.get("price"), true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}