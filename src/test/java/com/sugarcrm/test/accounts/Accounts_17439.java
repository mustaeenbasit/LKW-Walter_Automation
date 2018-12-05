package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.DocumentRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17439 extends SugarTest {
	MeetingRecord myMeeting;
	CallRecord myCall;
	TaskRecord myTask;
	ContactRecord myContact;
	AccountRecord myAccount;
	NoteRecord myNote;
	DocumentRecord myDocument;
	DataSource dsSubPanels;
	LeadRecord myLead;
	RevLineItemRecord myRli;

	public void setup() throws Exception {
		dsSubPanels = testData.get(testName);
		myAccount = (AccountRecord)sugar().accounts.api.create();
		myCall = (CallRecord)sugar().calls.api.create();
		myMeeting = (MeetingRecord)sugar().meetings.api.create();
		myTask = (TaskRecord)sugar().tasks.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create();
		myNote = (NoteRecord)sugar().notes.api.create();
		myDocument = (DocumentRecord)sugar().documents.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		myRli = (RevLineItemRecord)sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Verify using the collapsable arrow to display and hide the specific subpanel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17439_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to account record 
		myAccount.navToRecord();
		sugar().accounts.recordView.getControl("dataViewButton").click();
		// Verify that Data View button clicked
		sugar().accounts.recordView.getControl("dataViewButton").assertAttribute("class", "active");
		sugar().accounts.recordView.getControl("relatedSubpanelFilter").assertContains("Related", true);
		sugar().accounts.recordView.getControl("relatedSubpanelChoice").assertContains("All",true);

		FieldSet fs = new FieldSet();
		for (int i = 0; i < dsSubPanels.size()-2; i++) {
			StandardSubpanel subPanel = sugar().accounts.recordView.subpanels.get(dsSubPanels.get(i).get("sub_panel"));
			String subPanelName = subPanel.getControl("subpanelName").getText();
			subPanel.scrollIntoView();

			// Verify stacked view of all related modules. 
			subPanel.assertVisible(true);
			switch(subPanelName){
			case "Calls":
				subPanel.linkExistingRecord(myCall);
				fs.put("name",sugar().calls.getDefaultData().get("name"));
				break;
			case "Meetings":
				subPanel.linkExistingRecord(myMeeting);
				fs.put("name",sugar().meetings.getDefaultData().get("name"));
				break;
			case "Tasks":
				subPanel.linkExistingRecord(myTask);
				fs.put("name",sugar().tasks.getDefaultData().get("subject"));
				break;
			case "Notes":
				subPanel.linkExistingRecord(myNote);
				fs.put("name",sugar().notes.getDefaultData().get("subject"));
				break;
			case "Contacts":
				subPanel.linkExistingRecord(myContact);
				fs.put("name",sugar().contacts.getDefaultData().get("fullName"));
				break;
			case "Leads":
				subPanel.linkExistingRecord(myLead);
				fs.put("name",sugar().leads.getDefaultData().get("fullName"));
				break;
			case "RevenueLineItems":
				subPanel.linkExistingRecord(myRli);
				fs.put("name",sugar().revLineItems.getDefaultData().get("name"));
				break;
			case "Documents":
				subPanel.linkExistingRecord(myDocument);
				fs.put("name",sugar().documents.getDefaultData().get("documentName"));
				break;
			}
			// Verify that  The collapsable arrow closes and hides the records under this specific subpanel.
			subPanel.collapseSubpanel();
			subPanel.verify(1, fs , false);

			// Verify that The collapsable arrow opens and displays the records under this specific subpanel. 
			subPanel.expandSubpanel();
			subPanel.verify(1, fs , true);
			fs.clear();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}