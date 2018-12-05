package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest; 

public class Leads_22667 extends SugarTest {
	LeadRecord myLead;
	NoteRecord myNote;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {	
		myLead = (LeadRecord)sugar().leads.api.create();
		myNote = (NoteRecord)sugar().notes.api.create();
		sugar().login();

		// Link Note with Lead record
		myLead.navToRecord();
		notesSubpanel = sugar().leads.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.linkExistingRecord(myNote);
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Edit Note_Verify that editing note related to a lead can be canceled.
	 * @throws Exception
	 */	
	@Test
	public void Leads_22667_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Click on note record, edit record and cancel
		notesSubpanel.clickRecord(1);
		sugar().notes.recordView.edit();
		sugar().notes.recordView.getEditField("subject").set(testName);
		sugar().notes.recordView.cancel();

		// Go back to Leads listview
		myLead.navToRecord();

		// TODO: VOOD-1424
		// Verify the information of the note related to the lead is not changed.
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.getDetailField(1, "subject").assertEquals(myNote.get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}