package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22795 extends SugarTest {
	FieldSet noteDefaultData;
	LeadRecord myLead;
	NoteRecord noteFromSubpanel;
	StandardSubpanel subNotes;

	public void setup() throws Exception {
		noteDefaultData = sugar().notes.getDefaultData();
		sugar().login();
		myLead = (LeadRecord) sugar().leads.api.create();
		noteFromSubpanel = new NoteRecord(noteDefaultData);
	}

	/**
	 * Test Case 22795: Create Note By Full Form_Verify that note with attachment which is related to a lead can be created by Full Form mode.
	 */
	@Test
	public void Leads_22795_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Go to the lead record
		myLead.navToRecord();

		// Click Create in Notes subpanel
		subNotes = sugar().leads.recordView.subpanels.get("Notes");
		subNotes.addRecord();
		sugar().alerts.waitForLoadingExpiration();
		// Fill al required fields
		sugar().notes.createDrawer.showMore();
		sugar().notes.createDrawer.setFields(noteDefaultData);
		sugar().notes.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Verify note is created correctly
		noteFromSubpanel.verify();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
