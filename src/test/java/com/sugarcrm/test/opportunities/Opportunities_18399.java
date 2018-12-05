package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_18399 extends SugarTest {
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
		sugar().logout();
	}

	/**
	 * Verify that RLI cannot be converted into Quote in case the RLI was converted to quote before
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18399_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// navigate to opportunity record 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.scrollIntoView();
		// TODO: SC-4692
		rliSubPanel.toggleSubpanel();
		// In the Revenue Line Items sub-panel select revenue line item (RLI) attached to the opportunity
		rliSubPanel.checkRecord(1);
		rliSubPanel.openActionDropdown();
		// click "Generate Quote" in RLI sub-panel actions dropdown
		rliSubPanel.generateQuote();

		VoodooUtils.focusFrame("bwc-frame");
		// Verify that new quote record opens up in edit view 
		sugar().quotes.editView.assertVisible(true);
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(sugar().quotes.getDefaultData().get("date_quote_expected_closed"));
		VoodooUtils.focusDefault();
		// save the record
		sugar().quotes.editView.save();
		
		// Return to RLI sub-panel in opportunities module
		// TODO: VOOD-1424
		rliSubPanel.scrollIntoViewIfNeeded(false);
		rliSubPanel.getDetailField(1, "name").assertEquals(sugar().opportunities.getDefaultData().get("rli_name"), true);
		rliSubPanel.checkRecord(1);
		rliSubPanel.openActionDropdown();
		// again click "Generate Quote" in RLI sub-panel actions dropdown
		rliSubPanel.generateQuote();

		// verify that error message displayed
		sugar().alerts.getError().assertVisible(true);

		// Verify that following error message is displayed
		sugar().alerts.getError().assertElementContains(fs.get("errorMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}