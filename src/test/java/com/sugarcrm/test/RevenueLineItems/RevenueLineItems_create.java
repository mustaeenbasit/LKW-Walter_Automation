package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_create extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create();
	}

	@Test
	public void RevenueLineItems_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet myTestData = testData.get(testName).get(0);
		RevLineItemRecord myRLI = (RevLineItemRecord)sugar().revLineItems.create();
		// Verification is using fieldset from none-default data location
		// CSV location is "RevenueLineItems_create.csv"
		// TODO: When currency field verification is implemented in library
		// update verification to use default data.
		myRLI.verify(myTestData);  

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}