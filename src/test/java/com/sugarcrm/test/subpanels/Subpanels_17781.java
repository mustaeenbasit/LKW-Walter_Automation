package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17781 extends SugarTest {
	CaseRecord myCase;
	StandardSubpanel casesSubpanel;
	VoodooSelect filterFilterDropdown;

	public void setup() throws Exception {
		sugar.contacts.api.create();
		myCase = (CaseRecord) sugar.cases.api.create();
		sugar.login();

		// Relate a case to a contact
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		casesSubpanel = sugar.contacts.recordView.subpanels.get(sugar.cases.moduleNamePlural);
		casesSubpanel.linkExistingRecord(myCase);
	}

	/**
	 * Verify- able to dynamically filter related records in subpanel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17781_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Related drop down and select the related Cases module
		sugar.contacts.recordView.setRelatedSubpanelFilter(sugar.cases.moduleNamePlural);
		sugar.alerts.waitForLoadingExpiration();

		// Verify, that selected module name is shown as the first tag in the filter bar
		sugar.contacts.recordView.getControl("relatedSubpanelChoice").assertEquals(sugar.cases.moduleNamePlural, true);

		// Initialize the common Control references
		StandardSubpanel callsSubpanel = sugar.contacts.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		StandardSubpanel meetingSubpanel = sugar.contacts.recordView.subpanels.get(sugar.meetings.moduleNamePlural);
		StandardSubpanel tasksSubpanel = sugar.contacts.recordView.subpanels.get(sugar.tasks.moduleNamePlural);
		StandardSubpanel notesSubpanel = sugar.contacts.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		StandardSubpanel leadsSubpanel = sugar.contacts.recordView.subpanels.get(sugar.leads.moduleNamePlural);
		StandardSubpanel opportunitySubpanel = sugar.contacts.recordView.subpanels.get(sugar.opportunities.moduleNamePlural);
		StandardSubpanel emailsSubpanel = sugar.contacts.recordView.subpanels.get(sugar.emails.moduleNamePlural);
		StandardSubpanel documentSubpanel = sugar.contacts.recordView.subpanels.get(sugar.documents.moduleNamePlural);

		// Verify that only Cases module is visible and all other modules in subpanel are not visible
		casesSubpanel.assertVisible(true);
		callsSubpanel.assertVisible(false);
		meetingSubpanel.assertVisible(false);
		notesSubpanel.assertVisible(false);
		tasksSubpanel.assertVisible(false);
		leadsSubpanel.assertVisible(false);
		opportunitySubpanel.assertVisible(false);
		emailsSubpanel.assertVisible(false);
		documentSubpanel.assertVisible(false);

		// Input data in the search box (like 'name')
		sugar.contacts.recordView.setSearchString(myCase.get("name"));

		// Verify that related record listed in the Cases subpanel.   
		casesSubpanel.assertExists(true);
		casesSubpanel.assertContains(myCase.get("name"), true);

		// Revert back the changes of Subpanel Related selector
		sugar.contacts.recordView.setRelatedSubpanelFilter("All");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
