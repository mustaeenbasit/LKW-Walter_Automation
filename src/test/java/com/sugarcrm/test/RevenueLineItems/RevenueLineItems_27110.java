package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_27110 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().quotes.api.create();
		sugar().login();
	}

	/**
	 * Verify that calculation is correct when generating RLI from quote with quantity more than 1 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_27110_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet productDataFS = testData.get(testName).get(0);

		// Go to Quote record and edit
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("billingAccountName").set(myAccount.getRecordIdentifier());

		// Add a Product(name = Product 1, Quantity = 1000, Unit price = .13, discount = 10% (remember to check the discount check box), Tax class = non-taxable
		// TODO: VOOD-930
		new VoodooControl("input", "id", "add_group").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "input[name='quantity[1]']").set(productDataFS.get("quantity"));
		new VoodooControl("input", "css", "input[name='product_name[1]']").set(productDataFS.get("product_name"));
		new VoodooControl("input", "css", "input[name='discount_price[1]']").set(productDataFS.get("discount_price"));
		new VoodooControl("input", "css", "input[name='discount_amount[1]']").set(productDataFS.get("discount_amount"));
		new VoodooControl("input", "css", "input[name='checkbox_select[1]']").click();
		new VoodooControl("select", "css", "select[name='tax_class_select_name[1]']").set(productDataFS.get("tax_class_select_name"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Select Create Opportunity from quote
		sugar().quotes.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "#create_opp_from_quote_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// In the new opportunity, check product1 RLI
		StandardSubpanel revLineItemsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		revLineItemsSubpanel.scrollIntoView();
		revLineItemsSubpanel.expandSubpanel();
		revLineItemsSubpanel.clickRecord(1);

		// Verify that the Expect 'Total Discount Amount' is filled with $13 and likely is $117.
		sugar().revLineItems.recordView.getDetailField("discountPrice").assertEquals(productDataFS.get("discountPrice"), true);
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertEquals(productDataFS.get("likelyCase"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}