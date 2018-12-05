package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22046 extends SugarTest {
	DataSource customDS = new DataSource();
	ArrayList<Record> meetingRecords, callRecords = new ArrayList<Record>();

	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar().accounts.api.create();
		callRecords = sugar().calls.api.create(customDS);
		meetingRecords = sugar().meetings.api.create(customDS);
		sugar().login();
	}

	/**
	 * User sort subpanel column of detail view and navigate to a special page of subpanel of detail view, leave then go back directly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22046_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts recordView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		StandardSubpanel callsSubPanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubPanel.linkExistingRecords(callRecords);
		StandardSubpanel meetingsSubPanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		meetingsSubPanel.linkExistingRecords(meetingRecords);

		// Click "Subject" column title in "Calls" sub-panel to sort the records
		callsSubPanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1424
		// Verify that the Calls Sort status is retained as previous
		callsSubPanel.getDetailField(1, "name").assertContains(customDS.get(4).get("name"), true);
		callsSubPanel.getDetailField(2, "name").assertContains(customDS.get(3).get("name"), true);
		callsSubPanel.getDetailField(3, "name").assertContains(customDS.get(2).get("name"), true);
		callsSubPanel.getDetailField(4, "name").assertContains(customDS.get(1).get("name"), true);
		callsSubPanel.getDetailField(5, "name").assertContains(customDS.get(0).get("name"), true);

		// Click "Subject" column title in "Meetings" sub-panel to sort the records
		meetingsSubPanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();

		// Verify that the Meetings Sort status is retained as previous
		meetingsSubPanel.getDetailField(1, "name").assertContains(customDS.get(4).get("name"), true);
		meetingsSubPanel.getDetailField(2, "name").assertContains(customDS.get(3).get("name"), true);
		meetingsSubPanel.getDetailField(3, "name").assertContains(customDS.get(2).get("name"), true);
		meetingsSubPanel.getDetailField(4, "name").assertContains(customDS.get(1).get("name"), true);
		meetingsSubPanel.getDetailField(5, "name").assertContains(customDS.get(0).get("name"), true);

		// Go to contacts listView (Leave that page then go back directly)
		sugar().contacts.navToListView();

		// Go back to Accounts detailView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify that the Calls Sort status is retained as previous
		callsSubPanel.getDetailField(1, "name").assertContains(customDS.get(4).get("name"), true);
		callsSubPanel.getDetailField(2, "name").assertContains(customDS.get(3).get("name"), true);
		callsSubPanel.getDetailField(3, "name").assertContains(customDS.get(2).get("name"), true);
		callsSubPanel.getDetailField(4, "name").assertContains(customDS.get(1).get("name"), true);
		callsSubPanel.getDetailField(5, "name").assertContains(customDS.get(0).get("name"), true);

		// Verify that the Meetings Sort status is retained as previous
		meetingsSubPanel.getDetailField(1, "name").assertContains(customDS.get(4).get("name"), true);
		meetingsSubPanel.getDetailField(2, "name").assertContains(customDS.get(3).get("name"), true);
		meetingsSubPanel.getDetailField(3, "name").assertContains(customDS.get(2).get("name"), true);
		meetingsSubPanel.getDetailField(4, "name").assertContains(customDS.get(1).get("name"), true);
		meetingsSubPanel.getDetailField(5, "name").assertContains(customDS.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}