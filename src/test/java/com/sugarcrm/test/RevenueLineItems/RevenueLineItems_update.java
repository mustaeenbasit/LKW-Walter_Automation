package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_update extends SugarTest {
	RevLineItemRecord myRLI;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create();
		myRLI = (RevLineItemRecord)sugar().revLineItems.create();
	}

	@Test
	public void RevenueLineItems_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Air Safety Inc");
		newData.put("date_closed", "12/31/2015");

		// Edit the revenue line item record using the UI.
		myRLI.edit(newData);

		// Verify the revenue line item record was edited.
		myRLI.verify(newData);  

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 