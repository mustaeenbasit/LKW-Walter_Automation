package com.sugarcrm.test.leads;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_17969 extends SugarTest {
	TaskRecord task1, task2;
	CallRecord call1, call2;

	public void setup() throws Exception {
		DataSource ds = testData.get(testName);
		DataSource callsData = testData.get(testName+"_calls_data");
		// Create a Lead record
		sugar().leads.api.create();
		// create two task records
		task1 = (TaskRecord) sugar().tasks.api.create(ds.get(0));
		task2 = (TaskRecord) sugar().tasks.api.create(ds.get(1));

		// create two call records
		call1 = (CallRecord) sugar().calls.api.create(callsData.get(0));
		call2 = (CallRecord )sugar().calls.api.create(callsData.get(1));

		// Login as a valid user
		sugar().login();
	}

	/**
	 * Search for records in Leads sub panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_17969_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ArrayList<Record> tasksList = new ArrayList<Record>(); 
		tasksList.add(task1);
		tasksList.add(task2);

		ArrayList<Record> callRecordList = new ArrayList<Record>(); 
		callRecordList.add(call1);
		callRecordList.add(call2);

		// Navigate to Leads record view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Link 2 Tasks records to the Lead record in task sub panel.
		StandardSubpanel taskSubPanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubPanel.linkExistingRecords(tasksList);

		// Link 2 call records to the Lead record in calls sub panel.
		StandardSubpanel callsSubPanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubPanel.linkExistingRecords(callRecordList);

		// Set the Tasks Subpanel list to display Task modules subpanel. 
		sugar().leads.recordView.setRelatedSubpanelFilter(sugar().tasks.moduleNamePlural);

		// Search one of the task records.
		String searchQuery = call2.get("name").substring(0, 6);

		sugar().leads.recordView.setSearchString(searchQuery);
		// Verify that record is hit
		// TODO: VOOD-609 - need lib support for subpanel data list
		// TODO: VOOD-1380 - Need Lib support for separate definitions for subpanels fields in resouce CSV files for proper access and verify
		taskSubPanel.getControl("nameRow01").assertContains(task2.getRecordIdentifier(), true);

		// Set the TasCalls Subpanel list to display Calls modules subpanel. 
		sugar().leads.recordView.setRelatedSubpanelFilter(sugar().calls.moduleNamePlural);
		sugar().leads.recordView.setSearchString(searchQuery);
		// Verify that record is hit
		// TODO: VOOD-609 - need lib support for subpanel data list
		// TODO: VOOD-1380 - Need Lib support for separate definitions for subpanels fields in resouce CSV files for proper access and verify
		callsSubPanel.getControl("nameRow01").assertContains(call2.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}