package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24351 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel notesSubpanel;
	DataSource noteDS;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		noteDS = testData.get(testName);
	}

	/**
	 * Test Case 24351: In-Line Create Note_Verify that note without attachment can be in-line created from Notes sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24351_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		notesSubpanel = sugar().opportunities.recordView.subpanels.get("Notes");
		// Open opportunity record view and inline-create a related note without attachment
		myOpp.navToRecord();
		notesSubpanel.addRecord();
		sugar().notes.createDrawer.getEditField("subject").set(noteDS.get(0).get("subject"));
		sugar().notes.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		// Verify the note is successfully created and visible in notes subpanel of the opportunity
		myOpp.navToRecord();
		notesSubpanel.expandSubpanel();
		new VoodooControl("a", "css", ".fld_name.list div").assertContains((noteDS.get(0).get("subject")), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}