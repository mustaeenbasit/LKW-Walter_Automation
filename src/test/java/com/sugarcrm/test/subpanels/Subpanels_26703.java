package com.sugarcrm.test.subpanels;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;
import java.util.ArrayList;

public class Subpanels_26703 extends SugarTest {
	FieldSet emailSetup = new FieldSet();
	DataSource recordsData = new DataSource();
	CaseRecord caseRecord;
	NoteRecord noteRecord;
	String dashboardTitle = "";
	ArrayList<Record> meetingsRecords = new ArrayList<Record>();
	ArrayList<Record> callsRecords  = new ArrayList<Record>();
	ArrayList<Record> tasksRecords  = new ArrayList<Record>();

	public void setup() throws Exception {
		emailSetup = testData.get("env_email_setup").get(0);
		caseRecord = (CaseRecord) sugar.cases.api.create();
		noteRecord = (NoteRecord) sugar.notes.api.create();
		FieldSet values = new FieldSet();
		recordsData = testData.get(testName);

		// Create 2 Meetings having Status 'Held' and 'Canceled'
		meetingsRecords = new ArrayList<Record>();
		for (int i = 0; i < recordsData.size(); i++) {
			values.put("name", recordsData.get(i).get("meetingsName"));
			values.put("status", recordsData.get(i).get("status"));
			meetingsRecords.add(sugar.meetings.api.create(values));
			values.clear();
		}

		// Create 2 Calls having Status 'Held' and 'Canceled'
		callsRecords = new ArrayList<Record>();
		for (int i = 0; i < recordsData.size(); i++) {
			values.put("name", recordsData.get(i).get("callsName"));
			values.put("status", recordsData.get(i).get("status"));
			callsRecords.add(sugar.calls.api.create(values));
			values.clear();
		}

		// Create 2 Tasks having Status 'Deferred' and 'Completed'
		tasksRecords = new ArrayList<Record>();
		for (int i = 0; i < recordsData.size(); i++) {
			values.put("subject", recordsData.get(i).get("tasksName"));
			values.put("status", recordsData.get(i).get("taskStatus"));
			tasksRecords.add(sugar.tasks.api.create(values));
			values.clear();
		}
		sugar.login();

		// Set email settings in admin
		sugar.admin.setEmailServer(emailSetup);
	}
	/**
	 * Verify that record subject is a link to corresponding record in the Historical Summary page 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_26703_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Navigate to an cases record
		sugar.cases.navToListView();
		sugar.cases.listView.clickRecord(1);

		// Link to some records to the subpanels of Meetings, Calls, Notes, Tasks and Email
		StandardSubpanel callsSubpanel  = sugar.cases.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		callsSubpanel.linkExistingRecords(callsRecords);
		
		StandardSubpanel meetingsSubpanel = sugar.cases.recordView.subpanels.get(sugar.meetings.moduleNamePlural);
		meetingsSubpanel.scrollIntoViewIfNeeded(true);
		meetingsSubpanel.linkExistingRecords(meetingsRecords);
		
		StandardSubpanel tasksSubpanel = sugar.cases.recordView.subpanels.get(sugar.tasks.moduleNamePlural);
		tasksSubpanel.scrollIntoViewIfNeeded(true);
		tasksSubpanel.linkExistingRecords(tasksRecords);
		
		StandardSubpanel notesSubpanel = sugar.cases.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(true);
		notesSubpanel.linkExistingRecord(noteRecord);

		// Choose deshboard by name
		dashboardTitle = sugar.cases.dashboard.getControl("dashboard").getText().trim(); // TODO: VOOD-1502
		if(!dashboardTitle.contains(recordsData.get(0).get("myDashboard"))) {
			sugar.cases.dashboard.chooseDashboard(recordsData.get(0).get("myDashboard"));  
		}

		// Archived Email
		// TODO: VOOD-798. Lib support to create/verify Archive Email from History Dashlet
		new VoodooControl("a", "css", ".sortable:nth-child(2) span.btn-group a").click();;
		new VoodooControl("a", "css", "[data-dashletaction='archiveEmail']").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-797. Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", ".fld_date_sent i").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		new VoodooControl("input", "css", ".fld_from_address input").set(recordsData.get(0).get("fromAndToAddress"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(recordsData.get(0).get("fromAndToAddress"));
		new VoodooControl("input", "css", "div.select2-result-label").click();
		new VoodooControl("input", "name", "subject").set(recordsData.get(0).get("archivedMailSubject"));
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		VoodooUtils.waitForReady();
		sugar.cases.navToListView();

		// TODO: VOOD-965
		// Click Edit action drop down menu and click "Historical Summary"
		sugar.cases.listView.clickRecord(1);
		sugar.cases.recordView.openPrimaryButtonDropdown();
		VoodooControl historySummary = new VoodooControl("a", "css", ".fld_historical_summary_button.detail a");
		historySummary.click();
		new VoodooControl("th", "css", ".sorting.orderByname.cell-large").click();
		VoodooUtils.waitForReady();
		// Asserting that record's names in the subject column's are links and on-click takes you to corresponding record view
		for (int i = 1; i <= 8; i++) {
			sugar.cases.navToListView();
			sugar.cases.listView.clickRecord(1);
			sugar.cases.recordView.openPrimaryButtonDropdown();
			historySummary.click();
			VoodooUtils.waitForReady();
			VoodooControl recordName = new VoodooControl("a", "css", ".layout_Cases.drawer.active div.flex-list-view.right-actions tr:nth-child(" + i + ") td:nth-child(2) a");
			recordName.click();
			VoodooUtils.waitForReady();

			if (i == 1){
				sugar.tasks.recordView.assertVisible(true);
				sugar.tasks.recordView.getDetailField("subject").assertEquals(recordsData.get(1).get("tasksName"), true);
			}
			if (i == 2){
				sugar.tasks.recordView.assertVisible(true);
				sugar.tasks.recordView.getDetailField("subject").assertEquals(recordsData.get(0).get("tasksName"), true);
			}
			if (i == 3){
				sugar.meetings.recordView.assertVisible(true);
				sugar.meetings.recordView.getDetailField("name").assertEquals(recordsData.get(1).get("meetingsName"), true);
			}
			if (i == 4){
				sugar.meetings.recordView.assertVisible(true);
				sugar.meetings.recordView.getDetailField("name").assertEquals(recordsData.get(0).get("meetingsName"), true);
			}
			if (i == 5){
				sugar.calls.recordView.assertVisible(true);
				sugar.calls.recordView.getDetailField("name").assertEquals(recordsData.get(1).get("callsName"), true);
			}
			if (i == 6){
				sugar.calls.recordView.assertVisible(true);
				sugar.calls.recordView.getDetailField("name").assertEquals(recordsData.get(0).get("callsName"), true);
			}
			if (i == 7){
				VoodooUtils.focusFrame("bwc-frame");
				sugar.emails.detailView.assertVisible(true);

				// TODO: VOOD-1469
				// Missing hook values in EmailsModuleFields.csv
				new VoodooControl("div", "css", ".moduleTitle h2").assertEquals(recordsData.get(0).get("archivedMailSubject"), true);
				VoodooUtils.focusDefault();
			}
			if (i == 8){
				sugar.notes.recordView.assertVisible(true);
				sugar.notes.recordView.getDetailField("subject").assertEquals(sugar.notes.getDefaultData().get("subject"), true);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}