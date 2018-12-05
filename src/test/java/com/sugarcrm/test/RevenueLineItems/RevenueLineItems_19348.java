package com.sugarcrm.test.RevenueLineItems;


import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 * @author updates by Ashish Raina <araina@sugarcrm.com>
 */

public class RevenueLineItems_19348 extends SugarTest {
	AccountRecord myAcc;
	public void setup() throws Exception {
		myAcc = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Test Case 19348: Verify that Total Discount Amount field is correct for new RLI created when a quote is converted to an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_19348_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create a quote edit, add a new QLI and save record
		SimpleDateFormat sdFmt = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = sdFmt.format(new Date());
		// TODO: VOOD-1898 Quote + QLI conversion to Opportunity + RLI: Duplicates RLIs created if Quote is API-created
		sugar().navbar.selectMenuItem(sugar().quotes, "create" + sugar().quotes.moduleNameSingular);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("name").set(testName);
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(todaysDate);
		sugar().quotes.editView.getEditField("billingAccountName").set(myAcc.getRecordIdentifier());
		
		// Add QLI details
		// TODO: VOOD-930 Library support needed for controls on Quote editview 
		DataSource testDS = testData.get(testName);
		new VoodooControl("input", "css", "input[name='add_group']").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "input[name='quantity[1]']").set(testDS.get(0).get("quantity"));
		new VoodooControl("input", "css", "input[name='product_name[1]']").set(testDS.get(0).get("product_name"));
		new VoodooControl("input", "css", "input[name='cost_price[1]']").set(testDS.get(0).get("cost_price"));
		new VoodooControl("input", "css", "input[name='list_price[1]']").set(testDS.get(0).get("list_price"));
		// No entry for "input[name='discount_price[1]']" per Test case 
		new VoodooControl("input", "css", "input[name='discount_amount[1]']").set(testDS.get(0).get("discount_amount"));
		new VoodooControl("input", "css", "input[name='checkbox_select[1]']").set("false");
		
		// Save the quote record
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		
		// From the quote record view click 'Create Opportunity from Quote' button in an actions dropdown TODO: VOOD-930
		sugar().quotes.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "#create_opp_from_quote_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// // Verify likely case after User is redirected to the created Opportunity record view.
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(testDS.get(0).get("likely_case"), true);
		
		// Verify the total discount amount is equal to single QLI discount as we have not select discount % option while creating quote
		StandardSubpanel revLineItemsSub = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		revLineItemsSub.waitForVisible();
		revLineItemsSub.expandSubpanel();
		revLineItemsSub.clickRecord(1);
		sugar().revLineItems.recordView.getDetailField("discountPrice").assertContains(testDS.get(0).get("discount_amount"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}