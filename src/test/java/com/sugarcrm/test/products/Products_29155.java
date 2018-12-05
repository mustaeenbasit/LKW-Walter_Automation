package com.sugarcrm.test.products;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_29155 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that Teams & Assigned to column at Search and Select Product Types window should not be displayed
	 * 
	 * @throws Exception
	 */
	@Test
	public void Products_29155_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet productData = testData.get(testName).get(0);

		//  Go to Admin -> Product Catalog
		sugar.productCatalog.navToListView();

		// Click on Create button
		sugar.productCatalog.listView.create();

		// Click on down arrow at Type drop down field and click on Search for more link
		VoodooSelect typeFieldCtrl = (VoodooSelect) sugar.productCatalog.createDrawer.getEditField("type");
		typeFieldCtrl.clickSearchForMore();

		// Verify that the Teams & Assigned to column should not be displayed. Product Type & Date Modified column should be displayed only
		// Verify that only 4 "th": 'Select', 'Product Type', 'Date Modified' and 'More Columns' are exist on page shows that Teams & Assigned to column are not be displayed
		// TODO: VOOD-1671
		long columnsCount = (long) new VoodooControl("sapn", "css", ".search-and-select th").count();
		Assert.assertTrue("Columns count is not 4", columnsCount == 4);

		// TODO: VOOD-1671
		new VoodooControl("sapn", "css", ".search-and-select th[data-fieldname='name'] span").assertEquals(productData.get("productType"), true);
		new VoodooControl("sapn", "css", ".search-and-select th[data-fieldname='date_modified'] span").assertEquals(productData.get("dateModified"), true);
		new VoodooControl("sapn", "css", ".search-and-select th[data-fieldname='ProductTypes_select'] .fld_ProductTypes_select").assertEquals(productData.get("select"), true);
		new VoodooControl("sapn", "css", ".search-and-select th.morecol").assertVisible(true);

		// Click Cancel on Product Type Search and Select Drawer
		new VoodooControl("a", "css", "span[data-voodoo-name='close'] a").click();

		// Click Cancel on Product Catalog create drawer
		sugar.productCatalog.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}