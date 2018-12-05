package com.sugarcrm.test.products;

import com.sugarcrm.candybean.datasource.DataSource;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_28864 extends SugarTest {
	FieldSet customData;
	DataSource manuData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		manuData = testData.get(testName + "_1");
		// Create Manufacturer
		sugar().manufacturers.api.create(manuData);
		sugar().login();
	}

	/**
	 * Verify that inactive manufacturer cannot be selected while create a product
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_28864_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Go to Admin -> Product Catalog
		sugar().productCatalog.navToListView();

		// Click on Create button
		sugar().productCatalog.listView.create();

		// Click on down arrow at Type drop down field and click on Search for more link
		VoodooSelect typeFieldCtrl = (VoodooSelect) sugar().productCatalog.createDrawer.getEditField("manufacturerName");

		// Verify that Select Manufacturer... is displayed
		typeFieldCtrl.assertContains(customData.get("placeholder"), true);

		// Click on Manufacturer drop-down > Search for more link
		typeFieldCtrl.clickSearchForMore();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1162
		for(int i = 1; i <= 2; i++) {
			if(i < 2)
				// Verify that active manufacturer is now present in the list
				new VoodooControl("div", "css", ".dataTable.search-and-select tr:nth-child("+i+") td:nth-child(2) span div").assertEquals(manuData.get(1).get("name"), true);
			else
				// Verify that Inactive manufacturer is not display in search list
				new VoodooControl("div", "css", ".dataTable.search-and-select tr:nth-child("+i+") td:nth-child(2) span div").assertExists(false);
		}
		// Click Cancel on Product manufacturer Search and Select Drawer
		new VoodooControl("a", "css", "span[data-voodoo-name='close'] a").click();

		// Click Cancel on Product Catalog create drawer
		sugar().productCatalog.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}