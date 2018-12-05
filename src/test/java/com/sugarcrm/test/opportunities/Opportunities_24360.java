package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24360 extends SugarTest {

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that lead can be canceled full form creating in "Leads" sub-panel when using "Cancel" function.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24360_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel leadsSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubPanel.scrollIntoViewIfNeeded(false);
		
		// Click "Create" button in "Leads" sub-panel.
		leadsSubPanel.addRecord();
		sugar().leads.createDrawer.showMore();

		// Cancel creating the lead by clicking "Cancel" button.
		sugar().leads.createDrawer.cancel();

		// Verify that there is no new lead created in "Leads" sub-panel.
		Assert.assertTrue("Assert Leads sub-panel count Rows equals 0 FAILED.", leadsSubPanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}