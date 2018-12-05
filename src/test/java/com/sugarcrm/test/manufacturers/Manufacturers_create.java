package com.sugarcrm.test.manufacturers;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ManufacturerRecord;
import com.sugarcrm.test.SugarTest;

public class Manufacturers_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Manufacturers_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ManufacturerRecord myManufacturerRecord = (ManufacturerRecord)sugar().manufacturers.create();
		// Verify that record created successfully 
		myManufacturerRecord.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}