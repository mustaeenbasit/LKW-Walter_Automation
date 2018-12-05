package com.sugarcrm.test.subpanels;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_26298 extends SugarTest{
	StandardSubpanel callsSubpanel;
	DataSource calls = new DataSource();

	public void setup() throws Exception {
		// Create a lead Record
		sugar().leads.api.create();

		// Create 2 call records where status!="Held" (Here its Scheduled)
		calls = testData.get(testName);
		ArrayList<Record> callRecords = sugar().calls.api.create(calls);
		sugar().login();

		// Linking the call records with the lead record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		callsSubpanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.linkExistingRecords(callRecords);
		
		// Sorting the Calls as calls are listed in random order
		callsSubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();
	}

	/**
	 * A Call can be closed in sub panel
	 * @throws Exception
	 */
	@Test
	public void Subpanels_26298_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet callStatus = testData.get(testName + "_status").get(0);

		// Verify that the call status="Scheduled"
		callsSubpanel.getDetailField(1, "status").assertEquals(callStatus.get("statusScheduled"), true);
		callsSubpanel.getDetailField(1, "name").assertEquals(calls.get(0).get("name"),true);

		// Click the "Close" option for the Call in the first row in the sub-panel 
		callsSubpanel.expandSubpanelRowActions(1);
		callsSubpanel.getControl("closeActionRow01").click();

		// Assert that the Call status changed to "Held"
		callsSubpanel.getDetailField(1, "status").assertEquals(callStatus.get("statusHeld"), true);

		// Navigating to the record view of the closed Call
		callsSubpanel.clickRecord(1);

		// Assert the name and the status of the closed Call on the record view
		sugar().calls.recordView.getDetailField("name").assertEquals(calls.get(0).get("name"), true);
		sugar().calls.recordView.getDetailField("status").assertEquals(callStatus.get("statusHeld"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}