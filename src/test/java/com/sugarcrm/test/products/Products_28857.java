package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_28857 extends SugarTest {
	public void setup() throws Exception {
		sugar().productCatalog.api.create();
		sugar().login();
	}

	/**
	 * Verify that "View Change Log" option is removed from Edit action menu in ProductCatalog
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_28857_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Product Catalog module
		sugar().productCatalog.navToListView();
		
		// Open an existing record and click "Edit" action drop down
		sugar().productCatalog.listView.clickRecord(1);
		sugar().productCatalog.recordView.openPrimaryButtonDropdown();
		
		// Verify that "View Change Log" option is not present in action menu
		// TODO: VOOD-1463
		new VoodooControl("ul", "css", ".fld_main_dropdown .dropdown-menu").assertContains(customData.get("viewChangeLog"), false);
		
		// Close action drop down
		sugar().productCatalog.recordView.openPrimaryButtonDropdown();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}