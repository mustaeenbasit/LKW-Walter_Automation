package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_18404 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().productCatalog.api.create();
		sugar().login();

		// TODO: VOOD:444
		// create an opportunity via UI linked to the account and RLI record
		sugar().opportunities.create();

		// filled-in product field in the revenue line item 
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("product").set(sugar().productCatalog.getDefaultData().get("name"));
		sugar().revLineItems.recordView.save();

		// Logout from admin user
		sugar().logout();
	}

	/**
	 * Verify that the quote created from revenue line item(s) is listed as related record to the Opportunity.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18404_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// navigate to opportunity record 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.scrollIntoViewIfNeeded(false);
		// TODO: SC-4692
		rliSubPanel.toggleSubpanel();
		// In the Revenue Line Items sub-panel select revenue line item (RLI) attached to the opportunity
		rliSubPanel.checkRecord(1);
		rliSubPanel.openActionDropdown();
		// click "Generate Quote" in RLI sub-panel actions dropdown
		rliSubPanel.generateQuote();

		// Fill out all the required fields and save the quote record
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(sugar().quotes.getDefaultData().get("date_quote_expected_closed"));
		VoodooUtils.focusDefault();
		// save the record
		sugar().quotes.editView.save();

		StandardSubpanel quoteSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		quoteSubPanel.scrollIntoViewIfNeeded(false);
		FieldSet quoteRecord = new FieldSet();
		quoteRecord.put("name", sugar().opportunities.getDefaultData().get("name"));
		// TODO: SC-4692
		quoteSubPanel.toggleSubpanel();
		// Verify the quote created from revenue line item(s) is listed as related record to the Opportunity under Quotes sub-panel
		// TODO: VOOD-1424, VOOD-1000
		quoteSubPanel.verify(1, quoteRecord,true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}