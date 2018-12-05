package com.sugarcrm.test.accounts;

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

public class Accounts_26992 extends SugarTest {
	FieldSet recordsData;
	NoteRecord myNote;
	MeetingRecord myMeeting;
	CallRecord myCall;
	TaskRecord myTask;

	public void setup() throws Exception {
		recordsData = testData.get(testName).get(0);
		myNote = (NoteRecord) sugar().notes.api.create();
		FieldSet multiPurposeFS = new FieldSet();

		// Create a Meetings having Status 'Held'
		multiPurposeFS.put("status", recordsData.get("status"));
		myMeeting = (MeetingRecord) sugar().meetings.api.create(multiPurposeFS);
		multiPurposeFS.clear();

		// Create a Calls having Status 'Held'
		multiPurposeFS.put("status", recordsData.get("status"));
		myCall = (CallRecord) sugar().calls.api.create(multiPurposeFS);
		multiPurposeFS.clear();

		// Create a Tasks having Status 'Deferred'
		multiPurposeFS.put("status", recordsData.get("taskStatus"));
		myTask = (TaskRecord) sugar().tasks.api.create(multiPurposeFS);
		multiPurposeFS.clear();

		// Login to sugar
		sugar().login();

		// Create Account record from UI as we need email in the record
		multiPurposeFS.put("emailAddress", recordsData.get("fromAndToAddress"));
		sugar().accounts.create(multiPurposeFS);
		multiPurposeFS.clear();

		// Set email settings in admin
		FieldSet emailSetup = new FieldSet();
		emailSetup.put("userName", recordsData.get("fromAndToAddress"));
		emailSetup.put("password", recordsData.get("password"));
		emailSetup.put("allowAllUsers", recordsData.get("allowAllUsers"));
		sugar().admin.setEmailServer(emailSetup);
	}

	/**
	 * Verify email status is not shown empty on the historical summary view
	 * @throws Exception
	 */
	@Test
	public void Accounts_26992_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to an account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Link to some records to the subpanels of Meetings, Calls, Notes, Tasks and Email
		sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural).linkExistingRecord(myMeeting);
		sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).linkExistingRecord(myCall);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).scrollIntoViewIfNeeded(false); // Need to scroll so that link the task records
		sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural).linkExistingRecord(myTask);
		sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural).linkExistingRecord(myNote);

		// Send an Email from subpanel
		/*	
		 * Using control instead of '.composeEmail()' method to compose email use focus on table "#mce_0_tbl" and this is changed
		 * (like "#mce_0_tbl" to "#mce_7_tbl" on second click, "#mce_7_tbl" to "#mce_8_tbl" on third click, "#mce_8_tbl" to "#mce_9_tbl" on forth click and so on)
		 * on every click
		 */
		StandardSubpanel emailSubpanel= sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.getControl("composeEmail").waitForVisible(); //Need to add some extra wait for Jenkins 
		emailSubpanel.getControl("composeEmail").click();
		VoodooUtils.waitForReady();
		sugar().accounts.recordView.composeEmail.getControl("subject").set(recordsData.get("sentMailSubject"));
		sugar().accounts.recordView.composeEmail.getControl("sendButton").click();
		sugar().alerts.confirmAllAlerts();
		VoodooUtils.waitForReady(30000); // Taking time to send the email

		// Choose My Dashboard by name
		String dashboardTitle = sugar().accounts.dashboard.getControl("dashboard").getText().trim();
		if(!dashboardTitle.contains(recordsData.get("myDashboard"))) {
			sugar().accounts.dashboard.chooseDashboard(recordsData.get("myDashboard"));
		}

		// Archived Email
		// TODO: VOOD-798. Lib support in create/verify Archive Email from History Dashlet
		VoodooControl historyDashletCreateCtrl = new VoodooControl("a", "css", "li.row-fluid.sortable:nth-child(5) span.btn-group.dashlet-toolbar a");
		VoodooControl fromFieldCtrl = new VoodooControl("input", "css", ".fld_from_address input");
		historyDashletCreateCtrl.waitForVisible();
		historyDashletCreateCtrl.click();
		new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']").click();
		fromFieldCtrl.waitForElement();

		// TODO: VOOD-797. Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		fromFieldCtrl.waitForVisible();
		fromFieldCtrl.set(recordsData.get("fromAndToAddress"));
		new VoodooControl("input", "name", "subject").set(recordsData.get("archivedMailSubject"));
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-965
		// Click Edit action drop down menu and click "Historical Summary"
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();

		// Verify the status for email should not be missing or shown empty. (Also verify the status for Calls, Meeting and Task)
		// Using xPath to verify the status of that record(s) in the same row.
		new VoodooControl("tr", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div/table/tbody/tr[contains(.,'"+myTask.getRecordIdentifier()+"')]").assertContains(recordsData.get("taskStatus"), true);
		new VoodooControl("tr", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div/table/tbody/tr[contains(.,'"+myCall.getRecordIdentifier()+"')]").assertContains(recordsData.get("status"), true);
		new VoodooControl("tr", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div/table/tbody/tr[contains(.,'"+myMeeting.getRecordIdentifier()+"')]").assertContains(recordsData.get("status"), true);
		new VoodooControl("tr", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div/table/tbody/tr[contains(.,'"+recordsData.get("sentMailSubject")+"')]").assertContains(recordsData.get("sentEmail"), true);
		new VoodooControl("tr", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div/table/tbody/tr[contains(.,'"+recordsData.get("archivedMailSubject")+"')]").assertContains(recordsData.get("archivedEmail"), true);

		// Close Historical summary drawer
		new VoodooControl("a", "css", ".history-summary-headerpane a[name='cancel_button']").click();

		// Set Dashboard to 'Help Dashboard"
		if(dashboardTitle.contains(recordsData.get("myDashboard"))) {
			sugar().accounts.dashboard.chooseDashboard(recordsData.get("helpDashboard"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}