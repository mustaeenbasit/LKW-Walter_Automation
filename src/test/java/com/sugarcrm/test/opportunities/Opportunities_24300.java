package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24300 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Full Forum Schedule Meeting_Verify that the meeting is not scheduled in full form mode for opportunity 
	 * when using "Cancel" function.
	 * 
	 */
	@Test
	public void Opportunities_24300_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to opportunity recordview
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		StandardSubpanel meetingsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		FieldSet meetingsData = sugar().meetings.getDefaultData();
		
		// Click + button in Meetings Subpanel
		meetingsSubpanel.addRecord();
		
		// Enter required fields in the create drawer and click Cancel
		sugar().meetings.createDrawer.setFields(meetingsData);
		sugar().meetings.createDrawer.cancel();
		
		// Verify that Meetings Subpanel is visible and expand it
		meetingsSubpanel.assertVisible(true);
		meetingsSubpanel.expandSubpanel();
		
		// Verify that there is no new meeting created in "Meetings" sub-panel
		// TODO: VOOD-1424
		// meetingsSubpanel.verify(1, meetingsData, false);
		Assert.assertTrue("The subpanel is not empty", meetingsSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}