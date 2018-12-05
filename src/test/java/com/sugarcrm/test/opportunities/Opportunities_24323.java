package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24323 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Full Form Create Quote_Verify that quote can be created in "Quotes" sub-panel of opportunity.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24323_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel quoteSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
				
		// Create a quote by filling in all the fields and then save it
		quoteSubpanel.addRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("name").set(sugar().quotes.getDefaultData().get("name"));
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(sugar().quotes.getDefaultData().get("date_quote_expected_closed"));
		sugar().quotes.editView.getEditField("billingAccountName").set(sugar().quotes.getDefaultData().get("billingAccountName"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		// TODO: SC-4692
		VoodooUtils.waitForReady();
		quoteSubpanel.toggleSubpanel();
		
		// Verify that created quote is displayed in "Quotes" sub-panel of the opportunity
		Assert.assertTrue("Record is not exist in Quotes subpanel", quoteSubpanel.countRows() == 1);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}