package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24317 extends SugarTest {
	NoteRecord myNote;
	OpportunityRecord myOpp;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myNote = (NoteRecord) sugar().notes.api.create();

		// Link Note record with the opportunity exists.
		myOpp.navToRecord();
		notesSubpanel = sugar().opportunities.recordView.subpanels.get("Notes");
		notesSubpanel.linkExistingRecord(myNote);
	}

	/**
	 * Unlink Note_Verify that note can be correctly unlinked from opportunity record view using "Unlink" action of notes subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24317_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Unlink the Note
		notesSubpanel.unlinkRecord(1);
		sugar().alerts.confirmAllAlerts();
		VoodooUtils.waitForAlertExpiration();

		// Verify that unlinked Note no longer exists in subpanel
		notesSubpanel.expandSubpanel();
		notesSubpanel.assertContains(myNote.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}