package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24299 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Full Forum Schedule Meeting_Verify that meeting with "Scheduled" status can be scheduled in full form mode 
	 * for an opportunity in "Activities" sub-panel.
	 * 
	 */
	@Test
	public void Opportunities_24299_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to opportunity recordview
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		StandardSubpanel meetingsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		FieldSet meetingsData = sugar().meetings.getDefaultData();
		
		// Click + button in Meetings and Schedule a meeting with "Scheduled" status
		meetingsSubpanel.create(meetingsData);
		
		// Verify that Meetings Subpanel is visible and expand it
		meetingsSubpanel.assertVisible(true);
		meetingsSubpanel.expandSubpanel();
		
		// Verify that scheduled meeting is displayed in "Activities" sub-panel correctly
		// TODO: VOOD-1424
		// meetingsSubpanel.verify(1, meetingsData, true);
		meetingsSubpanel.assertContains(sugar().meetings.getDefaultData().get("name"), true);
		meetingsSubpanel.assertContains(sugar().meetings.getDefaultData().get("status"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}