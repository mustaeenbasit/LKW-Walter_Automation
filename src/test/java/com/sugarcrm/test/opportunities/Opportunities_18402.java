package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_18402 extends SugarTest {
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
	 * Verify that the Revenue Line Item details page (Record View) for a converted RLI clearly indicates that it is converted and provides a link to the corresponding Quote.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18402_execute() throws Exception {
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

		// Go to RLIs module list view 
		sugar().revLineItems.navToListView();
		// Open quoted RLI record in the record view 
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();
		VoodooControl quoteName = sugar().revLineItems.recordView.getDetailField("quoteName");
		quoteName.scrollIntoViewIfNeeded(false);
		// Verify that the converted RLI record has a link to the created quote in the Quote field of converted RLI. 
		quoteName.assertEquals(sugar().opportunities.getDefaultData().get("name"), true);

		// Click on the quote link
		quoteName.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify the detail view of the created quote opens up in Quotes module
		sugar().quotes.detailView.getDetailField("name").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}