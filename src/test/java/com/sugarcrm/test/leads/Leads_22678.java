package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Leads_22678 extends SugarTest {
	public void setup() throws Exception {
		// Create a leads record
		sugar().leads.api.create();
		sugar().login();	
	}

	/**
	 * In-Line Create Meeting_Verify that meeting can be canceled for in-line creating from sub-panel for a lead.
	 * @throws Exception
	 */
	@Test
	public void Leads_22678_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Leads" tab on navigation bar and click on existing lead in "Leads" list view.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Click "Create Meeting" button in "Meetings" sub-panel. Fill the Name and click on "Cancel"
		StandardSubpanel meetingsSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubpanel.addRecord();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.cancel();

		// Verify that meeting sub-panel is empty
		meetingsSubpanel.expandSubpanel();
		Assert.assertTrue("The subpanel is not empty", meetingsSubpanel.isEmpty());
			
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}