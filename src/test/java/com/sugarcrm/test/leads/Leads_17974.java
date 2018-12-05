package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_17974 extends SugarTest {
	DataSource notesData;
	LeadRecord myLead;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		notesData = testData.get(testName);
		sugar().notes.api.create(notesData);
		sugar().login();
	}

	/**
	 * Search and Select module from Leads sub panel
	 * @throws Exception
	 */
	@Test
	public void Leads_17974_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");				

		myLead.navToRecord();
		// Select Notes in Related field. 
		sugar().leads.recordView.setRelatedSubpanelFilter(sugar().notes.moduleNamePlural);

		// Select "Link Existing Record" at Notes subpanel
		notesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.clickLinkExisting();

		// In "Search and Select Notes" page, search any one record by typing in starting string of the Subject.
		sugar().notes.searchSelect.search(notesData.get(0).get("subject").substring(0,2));

		// Verify matching record is returned
		sugar().notes.searchSelect.assertContains(notesData.get(0).get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}