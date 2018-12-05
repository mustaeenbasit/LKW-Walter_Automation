package com.sugarcrm.test.products;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_30766 extends SugarTest {
	public void setup() throws Exception {
		sugar().productCatalog.api.create();
		sugar().login();			
	}

	/**
	 * Verify that editing product catalog record can be canceled 
	 * @throws Exception
	 */
	@Test
	public void Products_30766_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to product catalog listview
		sugar().productCatalog.navToListView();
		// open product catalog recordview 
		sugar().productCatalog.listView.clickRecord(1);
		// hover on the status field
		sugar().productCatalog.recordView.getDetailField("stockQuantity").hover();
		// click inline edit of the status field
		// TODO: VOOD-854
		new VoodooControl("a", "css", "[data-name='qty_in_stock'] span.record-edit-link-wrapper  a").click();
		// Without making any change click Cancel button which appears next to the Save button
		sugar().productCatalog.recordView.cancel();

		// verify that Any editing is canceled and record view is returned to original state with no field being in the edit mode
		sugar().productCatalog.recordView.getDetailField("name").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("stockQuantity").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}