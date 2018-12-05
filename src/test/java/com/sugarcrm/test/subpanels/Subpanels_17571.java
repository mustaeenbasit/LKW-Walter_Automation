package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17571 extends SugarTest {
	ContactRecord myContact;
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
	 * Verify subpanel predefined filters - My Favorites
	 *
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17571_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Related drop down and select the related Cases module
		sugar.contacts.recordView.setRelatedSubpanelFilter(sugar.cases.moduleNamePlural);
		sugar.alerts.waitForLoadingExpiration();

		// Marked case record in subpanel as favorite
		casesSubpanel.toggleFavorite(1);

		// Verify, that selected Cases module should be shown next to Related drop down
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

		// Verify that subpanel should only show the Cases module and all other modules sub panel should not be shown
		casesSubpanel.assertVisible(true);
		callsSubpanel.assertVisible(false);
		meetingSubpanel.assertVisible(false);
		notesSubpanel.assertVisible(false);
		tasksSubpanel.assertVisible(false);
		leadsSubpanel.assertVisible(false);
		opportunitySubpanel.assertVisible(false);
		emailsSubpanel.assertVisible(false);
		documentSubpanel.assertVisible(false);

		// Click on Filter drop down and select "My Favorites" from predefined filter list
		// TODO: VOOD-486: need lib support of filter dropdown for subpanels
		filterFilterDropdown = new VoodooSelect("a", "css", "span[data-voodoo-name='filter-filter-dropdown'] div a");
		filterFilterDropdown.set("My Favorites");
		sugar.alerts.waitForLoadingExpiration();

		// Verify, 'My Favorites' should be shown next to Filter drop down after being selected
		// TODO: VOOD-486: need lib support of filter dropdown for subpanels
		new VoodooControl("span", "css", ".choice-filter .choice-filter-label").assertContains("My Favorites", true);

		// Verify that related record listed in the Cases subpanel.   
		casesSubpanel.assertExists(true);
		casesSubpanel.assertContains(myCase.get("name"), true);

		// Revert back the changes of Subpanel Related selector
		sugar.contacts.recordView.setRelatedSubpanelFilter("All");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}