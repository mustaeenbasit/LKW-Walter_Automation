package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22666 extends SugarTest {
	FieldSet myNote;
	NoteRecord firstNote;

	public void setup() throws Exception {	
		myNote = testData.get(testName).get(0);
		sugar().login();
		sugar().leads.api.create();
		firstNote = (NoteRecord)sugar().notes.api.create();
	}

	/**
	 * Edit Note_Verify that the information of note related to a lead can be modified when using "Edit" function in sub-panel.
	 * @throws Exception
	 */	
	@Test
	public void Leads_22666_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		// Navigate to Leads module's listview
		sugar().leads.navToListView();
		
		// Navigate to lead record
		sugar().leads.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		
		// Link existing notes record to Lead's record in sub-panel of lead.
		StandardSubpanel noteSubPanel = sugar().leads.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		noteSubPanel.linkExistingRecord(firstNote);
		sugar().alerts.waitForLoadingExpiration();
		
		// Click on note record in "Notes" sub-panel. 
		noteSubPanel.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		
		// Edit and save the note record.
		sugar().notes.recordView.edit();
		sugar().notes.createDrawer.setFields(myNote);
		sugar().notes.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		//Go back to Leads module's listview
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		
		//Verify the Note record is updated in Notes sub panel
		noteSubPanel.expandSubpanel();
		noteSubPanel.verify(1, myNote, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}
		
	public void cleanup() throws Exception {}
}
