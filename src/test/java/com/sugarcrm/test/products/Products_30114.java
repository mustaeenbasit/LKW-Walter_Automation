package com.sugarcrm.test.products;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_30114 extends SugarTest {
	DataSource manufacturersData = new DataSource();
	
	public void setup() throws Exception {
		manufacturersData = testData.get(testName + "_manufacturers");
		sugar().manufacturers.api.create(manufacturersData);
		sugar().login();
	}

	/**
	 * Verify search for manufacturer name works correctly in Product Catalog create drawer
	 * @throws Exception
	 */
	@Test
	public void Products_30114_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		sugar().productCatalog.navToListView();
		sugar().navbar.selectMenuItem(sugar().productCatalog, "createProduct");
		
		// TODO: VOOD-629 : Add support for accessing individual components of VoodooSelect
		VoodooControl manufacturerInputBox = new VoodooControl("input", "css", "#select2-drop input");
		VoodooControl manufacturerSearchResult = new VoodooControl("li", "css", "#select2-drop ul li");
		
		// Searching for a non-existent manufacturer in Manufacturer field
		sugar().productCatalog.createDrawer.getEditField("manufacturerName").click();
		manufacturerInputBox.set(sugar().manufacturers.getDefaultData().get("name"));
		VoodooUtils.waitForReady();
		manufacturerSearchResult.assertEquals(customData.get("noMatchText"), true);
		
		// Searching for an existing manufacturer in Manufacturer field
		manufacturerInputBox.set(manufacturersData.get(1).get("name"));
		VoodooUtils.waitForReady();
		manufacturerSearchResult.assertEquals(manufacturersData.get(1).get("name"), true);  
		
		// Click on "Search and Select ..." in Manufacturer field 
		new VoodooControl("input", "css", "#select2-drop ul:nth-child(3) li").click();
		
		// Searching for a non-existent manufacturer on Manufacturer SSV
		sugar().manufacturers.searchSelect.search(sugar().manufacturers.getDefaultData().get("name"));
		Assert.assertEquals("Search and Select Drawer displays records when it should not", 0, 
				sugar().manufacturers.searchSelect.countRows());
		
		// Searching for an existing manufacturer on Manufacturer SSV
		sugar().manufacturers.searchSelect.search(manufacturersData.get(1).get("name"));
		Assert.assertEquals("Search and Select Drawer doesn't display one record when it should"
				, 1, sugar().manufacturers.searchSelect.countRows());
		
		// Verifying that the only record displayed on SSV is the one searched for
		// TODO: VOOD-1487 : Need lib support for verification of sugar-fields on SSV
		new VoodooControl("div", "css", ".layout_Manufacturers .list.fld_name div").assertEquals
			(manufacturersData.get(1).get("name"), true);

		sugar().manufacturers.searchSelect.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}