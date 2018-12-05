package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22682 extends SugarTest {
	LeadRecord lead;

	public void setup() throws Exception {
		lead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that note creation can be canceled in Notes sub-panel of Leads record view
	 *	
	 *	@throws Exception
	 */ 
	@Test
	public void Leads_22682_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		lead.navToRecord();
		StandardSubpanel notesSubpanel = sugar().leads.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.addRecord();
		sugar().notes.createDrawer.getEditField("subject").set(testName);
		sugar().notes.createDrawer.cancel();

		// Verify no notes record in subpanel
		Assert.assertTrue("The notes subpanel is not empty", notesSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}