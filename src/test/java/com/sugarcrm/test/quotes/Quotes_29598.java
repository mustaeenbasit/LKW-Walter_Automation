package com.sugarcrm.test.quotes;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Quotes_29598 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify that only one RLI record generated when quote is converted to opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Quotes_29598_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource testDS = testData.get(testName);
		// create quote record with following fields  - Quote Subject, Valid Until, Billing Account Name
		sugar().quotes.create();
		// Edit a newly created quote
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		// Click to add a new group with empty name
		// Click "Add Row" button and fill the following fields with Unit Price: 50.00
		// Add QLI details
		// TODO: VOOD-930 Library support needed for controls on Quote editview
		new VoodooControl("input", "css", "input[name='add_group']").click();
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "input[name='product_name[1]']").set(testDS.get(0).get("product_name"));
		new VoodooControl("input", "css", "input[name='discount_price[1]']").set(testDS.get(0).get("discount_price"));
		// Save the quote record
		VoodooUtils.focusDefault();
		// Save the record
		sugar().quotes.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.detailView.openPrimaryButtonDropdown();
		// Create opportunity from quote by selecting "Create Opportunity from Quote" menu item under quote actions menu
		// TODO: VOOD-2123
		new VoodooControl("a", "id", "create_opp_from_quote_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		FieldSet fs = sugar().quotes.getDefaultData();

		// verify that Opportunity record is created with only one RLI record linked to the opportunity.
		sugar().opportunities.recordView.getDetailField("name").assertEquals(fs.get("name"), true);
		// Verify that The "Worst"/"Likely"/"Best" amount of created Opportunity is $50.00
		sugar().opportunities.recordView.getDetailField("likelyCase").assertEquals(testDS.get(0).get("price_to_assert"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertEquals(testDS.get(0).get("price_to_assert"), true);
		sugar().opportunities.recordView.getDetailField("worstCase").assertEquals(testDS.get(0).get("price_to_assert"), true);

		sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).expandSubpanel();
		// Verify that The "Worst"/"Likely"/"Best" amount of created RLI is $50.00
		// TODO: VOOD-609
		new VoodooControl("span", "css", ".list.fld_name").assertEquals(testDS.get(0).get("product_name"), true);
		new VoodooControl("span", "css", ".list.fld_worst_case").assertEquals(testDS.get(0).get("price_to_assert"), true);
		new VoodooControl("span", "css", ".list.fld_likely_case").assertEquals(testDS.get(0).get("price_to_assert"), true);
		new VoodooControl("span", "css", ".list.fld_best_case").assertEquals(testDS.get(0).get("price_to_assert"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}