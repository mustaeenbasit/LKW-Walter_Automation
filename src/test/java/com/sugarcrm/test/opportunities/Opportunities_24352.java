package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24352 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
	}

	/**
	 * Test Case 24352: In-Line Create Note_Verify that note can be cancelled for in-line creating from Notes sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24352_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOpp.navToRecord();
		notesSubpanel = sugar().opportunities.recordView.subpanels.get("Notes");
		notesSubpanel.addRecord();
		// Cancel creating task record
		sugar().notes.createDrawer.cancel();
		// Verify that creating was correctly cancelled and task was not created
		myOpp.navToRecord();
		notesSubpanel.expandSubpanel();
		new VoodooControl("a", "css", "fld_name.list a").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}