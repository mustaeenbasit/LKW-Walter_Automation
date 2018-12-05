package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_27260 extends SugarTest {
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI; 
	FieldSet fs;
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord) sugar().revLineItems.api.create(); // TODO: Use creation through UI after "SC-2765" is fixed
	}

	/**
	 * Verify that quantity field is converted to 0.00 in case it is set to 0.001 or less.
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_27260_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit RlI record
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("name").set(fs.get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.createDrawer.getEditField("quantity").set(fs.get("quantity"));
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(fs.get("unit_price"));
		sugar().revLineItems.createDrawer.getEditField("discountPrice").set(fs.get("discount_amount"));
		sugar().revLineItems.recordView.save();

		// Assert that "Error. The value of this field must be greater than 0." alert message is displayed when hover over "i" icon in Quantity field
		sugar().revLineItems.createDrawer.getChildElement("err", "css", ".fld_quantity.edit.error span").assertAttribute("data-original-title", fs.get("assert_string"));

		// Assert the quantity field value defaults to 0.00
		sugar().revLineItems.createDrawer.getEditField("quantity").assertContains("0.00", true);

		// Assert that "Error.  Please resolve any error before proceeding." alert message pop up.
		// TODO:  uncomment line below if brand new RLI is created through UI. The top error is not displayed when editing existing record  
		//sugar().alerts.assertContains(fs.get("assert_string_top"), true);
		
		// Cancel RLI creation
		sugar().revLineItems.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}