package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_17565 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify sub panel icon and available actions for Notes Sub panel header of Leads
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_17565_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showDataView();

		// Open Related drop down, select All
		StandardSubpanel notes = sugar().leads.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		// Verify module icon
		notes.assertElementContains("Nt", true);
		// Verify module name
		notes.getControl("subpanelName").assertEquals(sugar().notes.moduleNamePlural, true);
		// Verify that + icon is in the header
		notes.getControl("addRecord").assertVisible(true);
		// Verify that drop down icon caret is there
		notes.getControl("expandSubpanelActions").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}