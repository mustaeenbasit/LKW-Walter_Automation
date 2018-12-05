package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_30153 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().productCatalog.api.create();
		sugar().login();			
	}

	/**
	 * Verify Unit Price value should persist in product catalog edit view.
	 * @throws Exception
	 */
	@Test
	public void Products_30153_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.clickRecord(1);
		
		// Verifying Unit price in record view.
		sugar().productCatalog.recordView.getDetailField("unitPrice").assertEquals(customData.get("unitPriceRecordView"), true);
		sugar().productCatalog.recordView.edit();
		
		// Verifying Unit price in edit view
		sugar().productCatalog.recordView.getEditField("unitPrice").assertEquals(customData.get("unitPriceEditView"), true);
		sugar().productCatalog.recordView.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}