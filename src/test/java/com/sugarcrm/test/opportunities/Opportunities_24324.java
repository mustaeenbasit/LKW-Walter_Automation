package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24324 extends SugarTest {

	public void setup() throws Exception {
		// Opportunity record(s) exist.
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that quote is not created in "Quotes" sub-panel when using "Cancel" function.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24324_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity recordView
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel quotesSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		quotesSubPanel.scrollIntoViewIfNeeded(false);

		// Click "Create" button in "Quotes" sub-panel.
		quotesSubPanel.addRecord();

		// Cancel creating the quote by clicking "Cancel" button
		sugar().quotes.editView.cancel();

		// TODO: VOOD-1424
		// Verify there is no new quote created in "Quotes" sub-panel.
		Assert.assertTrue("Assert Quotes sub-panel count Rows equals 0 FAILED.", quotesSubPanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}