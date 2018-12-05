package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest; 

public class Tasks_21168 extends SugarTest {
	NoteRecord myNote;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {	
		sugar.tasks.api.create();
		myNote = (NoteRecord) sugar.notes.api.create();
		sugar.login();					
	}

	/**
	 * New action dropdown list in task's subpanel
	 * @throws Exception
	 */	
	@Test
	public void Tasks_21168_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		notesSubpanel = sugar.tasks.recordView.subpanels.get(sugar.notes.moduleNamePlural);

		// Asserting the Actions available in Notes Subpanel and linking a Note.
		notesSubpanel.getControl("expandSubpanelActions").assertVisible(true);
		notesSubpanel.linkExistingRecord(myNote);

		// View all the Actions available with Record in Notes Subpanel
		notesSubpanel.expandSubpanelRowActions(1);
		notesSubpanel.getControl("editActionRow01").assertVisible(true);
		notesSubpanel.getControl("unlinkActionRow01").assertVisible(true);

		// Workaround to close the Action button alongside Record in the Notes Subpanel
		notesSubpanel.collapseSubpanel();
		notesSubpanel.expandSubpanel();

		// Unlink the Record from Notes Subpanel.
		notesSubpanel.unlinkRecord(1);
		
		// Verify the Record has been unlinked from notes subpanel
		notesSubpanel.assertContains(myNote.get("subject"), false);
		 
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}
