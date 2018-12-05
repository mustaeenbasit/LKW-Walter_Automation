package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_30189 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();			
	}

	/**
	 * Verify that correct water-mark is displaying in "Search and Select Manufacturers" drawer of Product Catalog
	 * @throws Exception
	 */
	@Test
	public void Products_30189_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.create();
		VoodooSelect manufacturerName = (VoodooSelect) sugar().productCatalog.createDrawer.getEditField("manufacturerName");
		manufacturerName.clickSearchForMore();
		sugar().manufacturers.searchSelect.getControl("search").assertVisible(true);
		
		// Verifying 'Search by manufacturer.....' is showing in search select drawer of manufacturer.
		sugar().manufacturers.searchSelect.getControl("search").assertAttribute(customData.get("attribute"), customData.get("attributeValue"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}