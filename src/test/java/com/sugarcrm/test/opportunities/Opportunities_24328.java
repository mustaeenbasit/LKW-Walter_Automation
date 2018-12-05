package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24328 extends SugarTest {
	QuoteRecord myQuote;

	public void setup() throws Exception {
		// Opportunity records exist and quotes records exist.
		sugar().opportunities.api.create();
		myQuote = (QuoteRecord) sugar().quotes.api.create();
		sugar().login();
	}

	/**
	 * Verify that an existing quotes can be selected for the opportunity by using check box.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24328_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity recordView
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel quotesSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		quotesSubPanel.scrollIntoViewIfNeeded(false);

		// Click "Link existing records" in "Quotes" sub-panel.
		quotesSubPanel.getControl("expandSubpanelActions").click();
		VoodooUtils.waitForReady();
		quotesSubPanel.getControl("linkExistingRecord").click();
		VoodooUtils.waitForReady();

		// Select quotes by checking the check box in the front of quote records in "Quote List" of the popup window.
		sugar().quotes.searchSelect.search(myQuote.getRecordIdentifier());
		sugar().quotes.searchSelect.selectRecord(1);

		// Click "Select" button in "Quote List" sub-panel of the popup window
		sugar().quotes.searchSelect.link();

		// TODO: VOOD-1424
		// Verify that the selected quotes are displayed in "Quotes" sub-panel.
		quotesSubPanel.assertContains(myQuote.getRecordIdentifier(), true);
		Assert.assertTrue("Assert Quotes sub-panel count Rows equals 1 FAILED.", quotesSubPanel.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}