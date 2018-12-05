package com.sugarcrm.test.RevenueLineItems;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_25043 extends SugarTest {
	FieldSet customFS;
	
	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		AccountRecord myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
		
		// Create a quote edit, add a new QLI and save record
		SimpleDateFormat sdFmt = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = sdFmt.format(new Date());
		// TODO: VOOD-1898 Quote + QLI conversion to Opportunity + RLI: Duplicates RLIs created if Quote is API-created
		sugar().navbar.selectMenuItem(sugar().quotes, "create" + sugar().quotes.moduleNameSingular);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("name").set(testName);
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(todaysDate);
		sugar().quotes.editView.getEditField("billingAccountName").set(myAccount.getRecordIdentifier());

		// Add QLI
		// TODO: VOOD-930
		new VoodooControl("input", "id", "add_group").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "input[name='product_name[1]']").set(customFS.get("quotedLineItem"));
		new VoodooControl("input", "css", "input[name='discount_price[1]']").set(customFS.get("unitPrice"));
		new VoodooControl("input", "css", "input[name='discount_amount[1]']").set(customFS.get("discountAmount"));
		new VoodooControl("input", "css", "input[name='checkbox_select[1]']").click();
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
	}

	/**
	 * Verify that opportunity/rli value is correct if opportunity is created from quote with discount rate specified in percentages
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_25043_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on "Create Opportunity from quote" in quoted recordView > action dropdown
		sugar().quotes.detailView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-930
		new VoodooControl("a", "css", "#create_opp_from_quote_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Verify that the new opportunity's amount and the RLI's amount would match the quote's subtotal: $190. 
		sugar().opportunities.recordView.getDetailField("likelyCase").assertContains(customFS.get("oppRliAmount"), true);
		
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.scrollIntoView();
		rliSubPanel.expandSubpanel();
		rliSubPanel.assertContains(customFS.get("oppRliAmount"), true);
		
		// Click on Opportunity > RLI subPanle > title
		rliSubPanel.clickRecord(1);
		
		// Verify that the new opportunity's amount and the RLI's amount would match the quote's subtotal: $190. 
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertEquals(customFS.get("oppRliAmount"), true);
		
		// Verify that the total discount amount is $10 (5% of $200)
		sugar().revLineItems.recordView.getDetailField("discountPrice").assertEquals(customFS.get("discountTotal"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}