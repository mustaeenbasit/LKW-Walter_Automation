package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26500 extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().quotes.api.create();
		sugar().login();
	}

	/**
	 * Verify that calculated RLI amount is calculated correctly when quote is converted to opportunity
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26500_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Edit the Quote to add a Line Item
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("billingAccountName").set(myAccount.getRecordIdentifier());
		
		// TODO: VOOD-930
		FieldSet fs = testData.get(testName).get(0);
		new VoodooControl("input", "css", "input#billing_account_name").set(myAccount.getRecordIdentifier());
		new VoodooControl("input", "css", "input#add_group").click();
		new VoodooControl("input", "css", "input#bundle_name_group_0").set(fs.get("groupName"));
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "input#quantity_1").set(fs.get("quantity"));
		new VoodooControl("input", "css", "input#name_1").set(fs.get("name"));
		new VoodooControl("input", "css", "input#discount_price_1").set(fs.get("unitPrice"));
		new VoodooControl("input", "css", "input#discount_amount_1").set(fs.get("discountAmount"));
		new VoodooControl("select", "css", "select#tax_class_select_name_1").set(fs.get("ifTaxable"));
		new VoodooControl("input", "css", "input#checkbox_select_1").set("false");
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		
		// Create an Opportunity from the Quote
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "#create_opp_from_quote_button").click();
		VoodooUtils.focusDefault();
		
		// Upon successful creation of an Opportunity from a Quote, system transfers control to the newly 
		// created Opportunity
		StandardSubpanel revLineItemsSubpanel = sugar().opportunities.recordView.subpanels.get("RevenueLineItems");
		revLineItemsSubpanel.scrollIntoView();
		revLineItemsSubpanel.expandSubpanel();
		revLineItemsSubpanel.clickRecord(1);

		// Calculated Revenue Line Item Amount is $999 
		sugar().revLineItems.recordView.getEditField("calcRLIAmount").assertContains(fs.get("expectedResult"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}