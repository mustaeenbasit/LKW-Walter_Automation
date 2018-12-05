package com.sugarcrm.test.opportunities;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24396 extends SugarTest {
	DataSource recordData = new DataSource();
	ArrayList<Record> meetingRecords = new ArrayList<Record>();
	ArrayList<Record> callsRecords = new ArrayList<Record>();

	public void setup() throws Exception {
		recordData = testData.get(testName);

		// Create an Opportunity record
		sugar().opportunities.api.create();

		// Login as a valid user
		sugar().login();

		// Create Meetings and Calls records and add them into ArrayLists
		// TODO: VOOD-444
		meetingRecords.addAll(sugar().meetings.create(recordData));
		sugar().alerts.getSuccess().closeAlert();
		callsRecords.addAll(sugar().calls.create(recordData));
		sugar().alerts.getSuccess().closeAlert();
	}

	// Link Calls/Meeting to the Opportunity and Sort the columns in Calls/Meetings sub-panel in Opportunity record view
	private void sortingSubpanels(String moduleName, StandardSubpanel subpanelCtrl, ArrayList<Record> records) throws Exception {
		// Link the Meetings and Calls records with the created Opportunity record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Link Meetings/Calls
		sugar().opportunities.recordView.setRelatedSubpanelFilter(moduleName);
		subpanelCtrl.linkExistingRecords(records);
		VoodooUtils.waitForReady(); // Extra time needed

		// Define the Meetings/Calls Sub panel Controls
		ArrayList<String> columnsHeaderName = new ArrayList<String>();
		columnsHeaderName.add("headerName");
		columnsHeaderName.add("headerStatus");
		columnsHeaderName.add("headerAssignedusername");

		// Add all the keys(i.e. fieldName) in ArrayList
		ArrayList<String> keys = new ArrayList<String>();

		for(Map.Entry<String,String> entry : recordData.get(0).entrySet()) {
			keys.add(entry.getKey().toString());
		}

		// Perform sorting on MEETINGS/CALLS sub-panel and verify the results
		int recordsCount = recordData.size();
		for(int i = 0; i < columnsHeaderName.size(); i++) {
			// Sort by Column (Descending)
			subpanelCtrl.sortBy(columnsHeaderName.get(i), false);
			VoodooUtils.waitForReady();

			// Verify that the Meeting/Call records are sorted according to the column titles on Meetings/Calls sub-panel
			for(int j = recordsCount; j > 0; j--) {
				int rowNumber = recordsCount - j + 1;
				int recordsKey = j-1;
				if (i == 0) {
					subpanelCtrl.getDetailField(rowNumber, keys.get(i)).assertContains(recordData.get(recordsKey).get(keys.get(i)), true);
				} else {
					subpanelCtrl.getDetailField(rowNumber, keys.get(i)).assertContains(recordData.get(recordsKey).get(keys.get(i)), true);
					subpanelCtrl.getDetailField(rowNumber, keys.get(0)).assertContains(recordData.get(recordsKey).get(keys.get(0)), true);
				}
			}
			// Sort by Column (Ascending)
			subpanelCtrl.sortBy(columnsHeaderName.get(i), true);
			VoodooUtils.waitForReady();

			// Verify that the Meetings/Calls records are sorted according to the column titles on Meetings/Calls sub-panel
			for(int j = 0; j < recordsCount; j++) {
				int rowNumber = j+1;
				int recordsKey = j;
				if (i == 0) {
					subpanelCtrl.getDetailField(rowNumber, keys.get(i)).assertContains(recordData.get(recordsKey).get(keys.get(i)), true);
				} else {
					subpanelCtrl.getDetailField(rowNumber, keys.get(i)).assertContains(recordData.get(recordsKey).get(keys.get(i)), true);
					subpanelCtrl.getDetailField(rowNumber, keys.get(0)).assertContains(recordData.get(recordsKey).get(keys.get(0)), true);
				}
			}
		}
	}

	/**
	 * Detail View Opportunity_Verify that the list in the sub-panel Activities of "Opportunity" detail view can be sorted.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24396_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define sub-panel controls
		StandardSubpanel meetingSubpanelCtrl = sugar().opportunities.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		StandardSubpanel callsSubpanelCtrl = sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural);

		// Verify sorting for Meetings 
		sortingSubpanels(sugar().meetings.moduleNamePlural, meetingSubpanelCtrl, meetingRecords);

		// Verify sorting for Calls
		sortingSubpanels(sugar().calls.moduleNamePlural, callsSubpanelCtrl, callsRecords);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}