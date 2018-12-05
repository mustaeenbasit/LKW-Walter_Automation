package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_18400 extends SugarTest {
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
	 * Verify that for each converted Revenue Line Item, 
	 * a column in the Revenue Line Items sub-panel view should enable to user to navigate to corresponding Quote.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18400_execute() throws Exception {
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

		VoodooUtils.focusFrame("bwc-frame");
		// Verify that new quote record opens up in edit view 
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(sugar().quotes.getDefaultData().get("date_quote_expected_closed"));
		VoodooUtils.focusDefault();
		// save the record
		sugar().quotes.editView.save();

		rliSubPanel.scrollIntoViewIfNeeded(false);
		// return to RLI sub-panel in opportunities module
		rliSubPanel.getDetailField(1, "name").assertEquals(sugar().opportunities.getDefaultData().get("rli_name"), true);
		VoodooControl quoteDetailField = rliSubPanel.getDetailField(1, "quoteName");
		// TODO: VOOD-1464
		new VoodooControl("span", "css", "tbody tr:nth-of-type(1) .fld_quote_name.list").scrollIntoViewIfNeeded(false);

		// Verify that the converted RLI record has a link to the created quote in the Quote (i.e Associated Quote) field of converted RLI. 
		quoteDetailField.assertEquals(sugar().opportunities.getDefaultData().get("name"), true);
		// click the Associated Quote link
		quoteDetailField.click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify that the detail view of the created quote opens up in Quotes module
		sugar().quotes.detailView.getDetailField("name").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}