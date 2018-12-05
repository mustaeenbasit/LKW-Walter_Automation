package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.ArrayList;

public class Accounts_26991 extends SugarTest {
	DataSource recordsData;
	NoteRecord myNote;
	ArrayList<Record> meetingsRecords, callsRecords, tasksRecords;

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		recordsData = testData.get(testName);
		sugar().accounts.api.create();
		myNote = (NoteRecord) sugar().notes.api.create();
		FieldSet values = new FieldSet();

		// Create 3 Meetings having Status 'Held', 'Scheduled' and 'Canceled'
		meetingsRecords = new ArrayList<Record>();
		for (int i = 0; i < recordsData.size(); i++) {
			values.put("name", recordsData.get(i).get("meetingsName"));
			values.put("status", recordsData.get(i).get("status"));
			meetingsRecords.add(sugar().meetings.api.create(values));
			values.clear();
		}

		// Create 3 Calls having Status 'Held', 'Scheduled' and 'Canceled'
		callsRecords = new ArrayList<Record>();
		for (int i = 0; i < recordsData.size(); i++) {
			values.put("name", recordsData.get(i).get("callsName"));
			values.put("status", recordsData.get(i).get("status"));
			callsRecords.add(sugar().calls.api.create(values));
			values.clear();
		}

		// Create 3 Tasks having Status 'Not Started', 'Deferred' and 'Completed'
		tasksRecords = new ArrayList<Record>();
		for (int i = 0; i < recordsData.size(); i++) {
			values.put("subject", recordsData.get(i).get("tasksName"));
			values.put("status", recordsData.get(i).get("taskStatus"));
			tasksRecords.add(sugar().tasks.api.create(values));
			values.clear();
		}

		// Login to sugar
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);
	}

	/**
	 * Verify historical summary view: Show past events ONLY
	 * @throws Exception
	 */
	@Test
	public void Accounts_26991_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to an account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Link to some records to the subpanels of Meetings, Calls, Notes, Tasks and Email
		sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural).linkExistingRecords(meetingsRecords);
		sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).linkExistingRecords(callsRecords);
		sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural).linkExistingRecords(tasksRecords);
		sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural).linkExistingRecord(myNote);

		// Choose deshboard by name
		String dashboardTitle = sugar().accounts.dashboard.getControl("dashboard").getText().trim();
		if(!dashboardTitle.contains(recordsData.get(0).get("myDashboard"))) {
			sugar().accounts.dashboard.chooseDashboard(recordsData.get(0).get("myDashboard"));
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
		fromFieldCtrl.set(recordsData.get(0).get("fromAndToAddress"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(recordsData.get(0).get("fromAndToAddress"));
		new VoodooControl("input", "css", "div.select2-result-label").click();
		new VoodooControl("input", "name", "subject").set(recordsData.get(0).get("archivedMailSubject"));
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		VoodooUtils.waitForReady();

		// Set email field in Account record
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("emailAddress").set(recordsData.get(0).get("fromAndToAddress"));
		sugar().accounts.recordView.save();

		// Send an Email from subpanel
		StandardSubpanel emailSubpanel= sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.getControl("composeEmail").click();
		VoodooUtils.waitForReady();
		sugar().accounts.recordView.composeEmail.getControl("subject").set(recordsData.get(0).get("sentMailSubject"));
		sugar().accounts.recordView.composeEmail.getControl("sendButton").click();
		sugar().alerts.confirmAllAlerts();
		VoodooUtils.waitForReady(30000); // Taking time to send the email

		// TODO: VOOD-965
		// Click Edit action drop down menu and click "Historical Summary"
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();
		VoodooControl historicalSummary = new VoodooControl("table", "css", ".reorderable-columns tbody");

		// Verify the only meetings where status in (Held, Not Held)
		historicalSummary.assertContains(recordsData.get(0).get("meetingsName"), true);
		historicalSummary.assertContains(recordsData.get(1).get("meetingsName"), false);
		historicalSummary.assertContains(recordsData.get(2).get("meetingsName"), true);

		// Verify the only calls where status in (Held, Not Held)
		historicalSummary.assertContains(recordsData.get(0).get("callsName"), true);
		historicalSummary.assertContains(recordsData.get(1).get("callsName"), false);
		historicalSummary.assertContains(recordsData.get(2).get("callsName"), true);

		// Verify the only tasks where status in (Deferred, Completed)
		historicalSummary.assertContains(recordsData.get(0).get("tasksName"), true);
		historicalSummary.assertContains(recordsData.get(1).get("tasksName"), false);
		historicalSummary.assertContains(recordsData.get(2).get("tasksName"), true);

		// Verify the all related notes
		historicalSummary.assertContains(myNote.getRecordIdentifier(), true);

		// Verify that all sent and archived email
		historicalSummary.assertContains(recordsData.get(0).get("archivedMailSubject"), true);
		historicalSummary.assertContains(recordsData.get(0).get("sentMailSubject"), true);

		// Close Historical summary drawer
		new VoodooControl("a", "css", ".history-summary-headerpane a[name='cancel_button']").click();

		// Set Dashboard to 'Help Dashboard"
		if(dashboardTitle.contains(recordsData.get(0).get("myDashboard"))) {
			sugar().accounts.dashboard.chooseDashboard(recordsData.get(0).get("helpDashboard"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}