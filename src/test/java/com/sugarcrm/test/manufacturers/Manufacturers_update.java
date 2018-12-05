package com.sugarcrm.test.manufacturers;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ManufacturerRecord;
import com.sugarcrm.test.SugarTest;

public class Manufacturers_update extends SugarTest {
	ManufacturerRecord myManufacturerRecord;

	public void setup() throws Exception {
		myManufacturerRecord = (ManufacturerRecord)sugar().manufacturers.api.create();
		sugar().login();
	}

	@Test
	public void Manufacturers_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = new FieldSet();
		fs.put("name", "Demo Reocrd");
		fs.put("status", "Inactive");
		fs.put("order", "10");
		// Edit the record using the UI.
		myManufacturerRecord.edit(fs);
		// verify that record updated successfully 
		myManufacturerRecord.verify(fs);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}