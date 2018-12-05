package com.sugarcrm.test.producttypes;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductTypeRecord;
import com.sugarcrm.test.SugarTest;

public class ProductTypes_delete extends SugarTest {
	ProductTypeRecord myProductTypeRecord;
	
	public void setup() throws Exception {
		sugar.login();
		myProductTypeRecord = (ProductTypeRecord)sugar.productTypes.api.create();
	}

	@Test
	public void ProductTypes_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete ProductType Record record using the UI.
		myProductTypeRecord.delete();

		sugar.productTypes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify that record deleted successfully 
		assertEquals(VoodooUtils.contains(myProductTypeRecord.getRecordIdentifier(), true), false);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}