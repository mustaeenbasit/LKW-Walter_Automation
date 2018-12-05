package com.sugarcrm.test.manufacturers;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ManufacturerRecord;
import com.sugarcrm.test.SugarTest;

public class Manufacturers_delete extends SugarTest {
	ManufacturerRecord myManufacturerRecord ;

	public void setup() throws Exception {
		myManufacturerRecord = (ManufacturerRecord)sugar().manufacturers.api.create();
		sugar().login();
	}

	@Test
	public void Manufacturers_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete Manufacturer record using the UI.
		myManufacturerRecord.delete();

		sugar().manufacturers.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify that Manufacturer deleted successfully 
		assertEquals(VoodooUtils.contains(myManufacturerRecord.getRecordIdentifier(), true), false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}