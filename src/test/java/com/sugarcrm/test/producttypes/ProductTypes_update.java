package com.sugarcrm.test.producttypes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductTypeRecord;
import com.sugarcrm.test.SugarTest;

public class ProductTypes_update extends SugarTest {
	ProductTypeRecord myProductTypeRecord;
	
	public void setup() throws Exception {
		sugar.login();
		myProductTypeRecord = (ProductTypeRecord)sugar.productTypes.api.create();
	}

	@Test
	public void ProductTypes_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = new FieldSet();
		fs.put("name", "Disk Drive");
		fs.put("description", "Test description");
		fs.put("order", "4");
		
		// Edit the record using the UI.
		myProductTypeRecord.edit(fs);
		
		// verify that record updated successfully 
		myProductTypeRecord.verify(fs);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}