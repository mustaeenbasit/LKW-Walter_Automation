package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22654 extends SugarTest{
	
	public void setup() throws Exception {
		// Create Lead and Login to system as valid user.
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Schedule Call_Verify that the call is not scheduled for lead when using "Cancel" function.
	 * @throws Exception
	 */
	@Test
	public void Leads_22654_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click "Leads" tab on navigation bar.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		// Click Create button in Meetings sub-panel.
		StandardSubpanel meetingSubpanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingSubpanel.addRecord();

		// Cancel scheduling a call meeting for the selected lead.
		sugar().meetings.createDrawer.cancel();

		// Verify there is no new meeting created in Meetings sub-panel.
		Assert.assertTrue("Meetings subpanel contains record(s).", meetingSubpanel.countRows() == 0);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
