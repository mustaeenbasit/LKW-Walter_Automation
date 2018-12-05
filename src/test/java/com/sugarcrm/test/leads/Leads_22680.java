package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22680 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 *	Verify that call can be canceled for in-line creating from Activities sub-panel for a lead
	 *	@throws Exception
	 */
	@Test
	public void Leads_22680_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Leads" module -> detail view for a lead.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		//  Click "Create" button i.e. "+" icon in Activities Calls sub-panel
		StandardSubpanel callsSubpanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.addRecord();

		// Fill in all required fields, such as enter Call name, click Cancel
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.cancel();

		// verify the Call is not created in Calls sub-panel
		callsSubpanel.expandSubpanel();
		Assert.assertTrue("Number of rows did not equal zero.", callsSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}