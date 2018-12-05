package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.DocumentRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17438 extends SugarTest {
	CallRecord myCall;
	MeetingRecord myMeeting;
	DocumentRecord myDocument;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myCall = (CallRecord) sugar().calls.api.create();
		myMeeting = (MeetingRecord) sugar().meetings.api.create();
		myDocument = (DocumentRecord) sugar().documents.api.create();
		sugar().login();
	}

	/**
	 * Verify the subpanel header info to be shown 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17438_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource relatedData = testData.get(testName);

		// Go to Accounts module and click one account record from list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Associating call, meetings and document created above with the Account record
		StandardSubpanel callsubPanel = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		StandardSubpanel meetingsubPanel = sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		StandardSubpanel documentsubPanel = sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural);
		callsubPanel.linkExistingRecord(myCall);
		meetingsubPanel.linkExistingRecord(myMeeting);
		documentsubPanel.linkExistingRecord(myDocument);

		// Clicking "Data View" button next to the search field
		sugar().accounts.recordView.getControl("dataViewButton").click();

		// Clicking Related arrow key
		sugar().accounts.recordView.getControl("relatedSubpanelFilter").click();
		VoodooUtils.waitForReady();

		// Verifying modules are displayed in the drop down
		// TODO: VOOD- 468
		for(int i = 0 ; i < relatedData.size() ; i++)
			new VoodooControl("li", "css", ".select2-results li:nth-child("+ (i+2) +")").assertEquals(relatedData.get(i).get("moduleName"), true);

		// Clicking option 'All' in the 'Related to' dropdown options
		new VoodooControl("li", "css", "#select2-drop .select2-highlighted").click();

		for(int i = 0 ; i < relatedData.size() ; i++) {

			// Getting subpanel for the Account Record View
			StandardSubpanel subPanel;
			if(relatedData.get(i).get("moduleName").equals("Member Organizations"))
				subPanel = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
			else if(relatedData.get(i).get("moduleName").equals("Revenue Line Items"))
				subPanel = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
			else if(relatedData.get(i).get("moduleName").equals("Campaign Log")) {

				// TODO: VOOD-1344: Need library support for 'Campaign Log' subpanel in Record view of any module
				VoodooControl campaignLogSubPanel = new VoodooControl("div", "css", ".filtered.layout_CampaignLog");
				campaignLogSubPanel.scrollIntoViewIfNeeded(false);

				// Verifying that 'Campaign Log' subpanel displays module name and its respective icon
				new VoodooControl("h4", "css",".filtered.tabbable:nth-child(15) .subpanel-header h4").assertEquals(relatedData.get(i).get("moduleName"), true);
				new VoodooControl("div", "css",".filtered.tabbable:nth-child(15) .label").assertEquals(relatedData.get(i).get("moduleIcon"), true);
				break;
			}
			else {
				subPanel = sugar().accounts.recordView.subpanels.get(relatedData.get(i).get("moduleName"));
			}

			VoodooUtils.waitForReady();
			subPanel.scrollIntoViewIfNeeded(false);

			// Verifying that subpanel displays module name and its respective icon
			subPanel.getControl("subpanelName").assertEquals(relatedData.get(i).get("moduleName"), true);

			// TODO: VOOD-1419 : Provide support for subpanels avatar image
			new VoodooControl("div", "css",".filtered.tabbable:nth-child("+ (i+1) +") .label").assertEquals(relatedData.get(i).get("moduleIcon"), true);

			// Checking as on RLI subpanel the 'Add record' and 'Action dropdowns' are absent
			if(!(relatedData.get(i).get("moduleName").equals("Revenue Line Items"))) {

				// Verifying that subpanel displays '+' button
				if(relatedData.get(i).get("moduleName").equals("Emails"))
					sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural).getControl("composeEmail").assertVisible(true);
				else
					subPanel.getControl("addRecord").assertVisible(true);

				// The corresponding CSV file contains a column flag set as true for modules which have their action dropdowns as enabled and vice versa
				// Verifying that subpanel displays action drop down for "Link Existing Record"
				if(relatedData.get(i).get("flag").equals("true")) {
					subPanel.scrollIntoViewIfNeeded(false);
					subPanel.getControl("expandSubpanelActions").click();
					VoodooUtils.waitForReady();
					subPanel.getControl("linkExistingRecord").assertVisible(true);
					subPanel.getControl("expandSubpanelActions").click();
					subPanel.hover();
					VoodooUtils.waitForReady();
				}
			}
		}

		// Verifying that subpanels display correct number of records in each of them
		sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).getControl("count").assertContains("1", true);
		sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural).getControl("count").assertContains("1", true);
		sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural).getControl("count").assertContains("1", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}