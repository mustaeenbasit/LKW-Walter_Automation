package com.sugarcrm.test.producttypes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProductTypeRecord;
import com.sugarcrm.test.SugarTest;

public class ProductTypes_create extends SugarTest {
	ProductTypeRecord myProductTypeRecord;
	
	public void setup() throws Exception {
		sugar.login();
	}

	@Test
	public void ProductTypes_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myProductTypeRecord = (ProductTypeRecord)sugar.productTypes.create();
		
		// verify that record created successfully 
		myProductTypeRecord.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}