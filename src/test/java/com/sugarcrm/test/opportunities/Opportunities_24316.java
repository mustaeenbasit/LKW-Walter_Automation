package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24316 extends SugarTest {
	NoteRecord myNote;
	OpportunityRecord myOpp;
	StandardSubpanel notesSubpanel;
	DataSource notesDS;

	public void setup() throws Exception {
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myNote = (NoteRecord) sugar().notes.api.create();

		sugar().login();
		notesDS = testData.get(testName);
	}

	/**
	 * Test Case 24316: Edit Note_Verify that editing note related to an opportunity can be canceled
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24316_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOpp.navToRecord();
		notesSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.clickLinkExisting();
		sugar().notes.searchSelect.selectRecord(1);
		sugar().notes.searchSelect.getControl("link").click();
		sugar().alerts.waitForLoadingExpiration();

		notesSubpanel.scrollIntoView();

		// Inline-edit note of related opportunity
		notesSubpanel.editRecord(1);
		new VoodooControl("input", "css", "input[name='name']").set(notesDS.get(0).get("subject"));

		// Cancel modifying note
		notesSubpanel.cancelAction(1);

		// Verify that inline-editing was correctly cancelled and "subject" value of the note related to the opportunity is not changed
		notesSubpanel.verify(1, notesDS.get(0), false);
		notesSubpanel.assertContains(myNote.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}