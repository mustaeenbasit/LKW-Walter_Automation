package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_28740 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Opp ONLY mode: Verify that opportunity created from quote 
	 * correctly populated worst,likely and best fields of opportunity 
	 * @throws Exception
	 */ 
	@Test
	public void Opportunities_28740_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet quoteData = testData.get(testName).get(0);
		String opportunityAmount = String.format("%s%s", quoteData.get("currency"),quoteData.get("unit_price"));

		// Create a quote Record via UI and also add a QLI row with proper price values in the create form
		sugar().navbar.selectMenuItem(sugar().quotes, "createQuote");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("name").set(quoteData.get("name"));
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(quoteData.get("date_quote_expected_closed"));

		// TODO: VOOD-930
		new VoodooControl("input", "css", "#billing_account_name").set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("input", "css", "#add_group").click();		
		new VoodooControl("input", "css", "input[name='Add Row']").click();
		new VoodooControl("input", "css", "#name_1").set(quoteData.get("product_name"));
		new VoodooControl("input", "css", "#cost_price_1").set(quoteData.get("cost_price"));
		new VoodooControl("input", "css", "#list_price_1").set(quoteData.get("list_price"));
		new VoodooControl("input", "css", "#discount_price_1").set(quoteData.get("unit_price"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		VoodooUtils.focusFrame("bwc-frame");

		// Click the Edit drop down i.e on the quote detail view
		new VoodooControl("span", "css", ".actionsContainer .ab").click();

		// From the edit drop down menu, select "Create Opportunity from quote" option
		new VoodooControl("a", "id", "create_opp_from_quote_button").click();
		VoodooUtils.waitForReady();

		// Assert that user has been navigated to the record view of Opportunity created
		sugar().opportunities.recordView.getDetailField("name").assertEquals(quoteData.get("name"), true);

		// Assert that User must see Likely, Best and Worst fields filled up with converted amount of the Quote
		sugar().opportunities.recordView.getDetailField("likelyCase").assertEquals(opportunityAmount, true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertEquals(opportunityAmount, true);
		sugar().opportunities.recordView.getDetailField("worstCase").assertEquals(opportunityAmount, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}