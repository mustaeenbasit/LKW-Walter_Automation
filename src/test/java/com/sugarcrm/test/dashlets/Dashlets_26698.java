package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_26698 extends SugarTest {
	NoteRecord myNoteRecord;
	MeetingRecord myMeetingRecord;
	TaskRecord myTaskRecord;
	CallRecord myCallRecord;
	FieldSet customFS;

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar.opportunities.api.create();

		// Set status for meetings, calls & tasks
		FieldSet fs = new FieldSet();
		fs.put("status", customFS.get("status"));
		myMeetingRecord = (MeetingRecord) sugar.meetings.api.create(fs);
		myCallRecord = (CallRecord) sugar.calls.api.create(fs);
		fs.clear();
		fs.put("status", customFS.get("statusForTask"));
		myTaskRecord = (TaskRecord) sugar.tasks.api.create(fs);
		myNoteRecord = (NoteRecord) sugar.notes.api.create();
		sugar.login();

		// Configure Email settings, In bound, out bound mail settings
		sugar.admin.setEmailServer(emailSetup);
		sugar.inboundEmail.create();

		// To make relationship between calls,meetings,tasks and notes with Opportunity
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.clickRecord(1);
		sugar.alerts.getAlert().closeAlert();
		sugar.opportunities.recordView.subpanels.get(sugar.calls.moduleNamePlural).linkExistingRecord(myCallRecord);
		sugar.opportunities.recordView.subpanels.get(sugar.meetings.moduleNamePlural).linkExistingRecord(myMeetingRecord);
		sugar.opportunities.recordView.subpanels.get(sugar.tasks.moduleNamePlural).linkExistingRecord(myTaskRecord);
		StandardSubpanel notesSubpanel = sugar.opportunities.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		notesSubpanel.scrollIntoView();
		notesSubpanel.linkExistingRecord(myNoteRecord);
		StandardSubpanel emailSubpanel = sugar.opportunities.recordView.subpanels.get(sugar.emails.moduleNamePlural);
		emailSubpanel.scrollIntoView();
		emailSubpanel.composeEmail();
		sugar.accounts.recordView.composeEmail.getControl("addressBook").click();

		// TODO: VOOD-1423 -Need lib support for Accounts > recordView > composeEmail > ToAddress > AddressBook	 
		VoodooControl checkbox = new VoodooControl("input", "css", ".flex-list-view tr:nth-child(1) td:nth-child(1) input[type='checkbox']");
		checkbox.waitForVisible();
		checkbox.click();
		new VoodooControl("a", "css", "a[name='done_button']").click();
		VoodooUtils.waitForReady();
		sugar.opportunities.recordView.composeEmail.getControl("subject").set(customFS.get("subject"));
		sugar.opportunities.recordView.composeEmail.addBodyMessage(customFS.get("emailBody"));

		// Send Email
		sugar.opportunities.recordView.composeEmail.getControl("sendButton").click();
		VoodooUtils.waitForReady(25000); // Required more wait to complete above "Send mail" Action
	}

	/**
	 * Verify that default set of columns in Historical Summary page is correct
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_26698_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open Historical Summary page by click Historical Summary menu item under Actions dropdown
		sugar.opportunities.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-965 : Support for Historical Summary page
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();

		// Verify that The following attributes are displayed for each record: Subject, Type, Status, Related Contact, Description, Email To, Email From,Date Created, Date Modified
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable th[data-fieldname='picture'] div span").assertExists(true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable th[data-fieldname='name'] div span").assertExists(true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable th[data-fieldname='status'] div span").assertExists(true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable th[data-fieldname='description'] div span").assertExists(true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable th[data-fieldname='to_addrs'] div span").assertExists(true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable th[data-fieldname='from_addr'] div span").assertExists(true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable th[data-fieldname='related_contact'] div span").assertExists(true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable th[data-fieldname='date_modified'] div span").assertExists(true);

		// Verify that If a record does not have certain attributes, (for example, Notes don't have status, emails don't have related contacts), value of those attributes will be empty.
		// Using xPath to verify the status of that record(s) in the same row.
		new VoodooControl("tr", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div/table/tbody/tr[contains(.,'"+myNoteRecord.getRecordIdentifier()+"')]/td[3]/span/div");
		new VoodooControl("tr", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div/table/tbody/tr[contains(.,'"+customFS.get("subject")+"')]/td[7]/span/div").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}