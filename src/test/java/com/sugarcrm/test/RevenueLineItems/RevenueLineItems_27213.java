package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_27213 extends SugarTest {	
	FieldSet rliData = new FieldSet();

	public void setup() throws Exception {
		rliData = testData.get(testName).get(0);
		sugar().opportunities.api.create();
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Verify that quantity field on RLI record view accepts decimals in the form like .12 as valid number
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_27213_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("quantity").set(rliData.get("quantity"));
		sugar().revLineItems.recordView.getEditField("unitPrice").set(rliData.get("unit_price"));

		// TODO: VOOD-1437, CB-252 - Need CB support to simulate input of any keyboard key
		// Verify Likely to update with $12.00, as per the formula for likely field given in studio  
		VoodooControl likelyCase = sugar().revLineItems.recordView.getEditField("likelyCase");
		likelyCase.set("" + '\uE007');
		likelyCase.assertEquals(rliData.get("likely_case"), true);
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.recordView.save();

		// Verify that record view displays quantity as 0.12 and likely amount is displayed correctly on record view
		sugar().revLineItems.recordView.getDetailField("quantity").assertContains(rliData.get("quantity"), true);
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertContains(rliData.get("likely_case"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}