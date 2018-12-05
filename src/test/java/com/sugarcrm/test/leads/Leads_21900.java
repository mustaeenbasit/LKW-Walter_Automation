package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_21900 extends SugarTest {
	LeadRecord lead;
	StandardSubpanel notesSubpanel;
	String noteName;
		
	public void setup() throws Exception {
		sugar().login();
		
		lead = (LeadRecord)sugar().leads.api.create();
		noteName = sugar().notes.api.create().getRecordIdentifier();
		
		lead.navToRecord();
		notesSubpanel = sugar().leads.recordView.subpanels.get("Notes");
		notesSubpanel.clickLinkExisting();
		
		// Select the first Meeting from the list
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("input", "css", "a[name='link_button']").click();
	}

	/**
	 * 21900 Verify that note can be removed from lead detail view by "Unlink" function
	 * @throws Exception
	 */
	@Test
	public void Leads_21900_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		notesSubpanel.unlinkRecord(1);
		sugar().alerts.confirmAllAlerts();
		
		notesSubpanel.expandSubpanel();
		notesSubpanel.assertContains(noteName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}