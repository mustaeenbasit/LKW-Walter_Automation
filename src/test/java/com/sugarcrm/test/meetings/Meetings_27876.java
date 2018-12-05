package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27876 extends SugarTest {
	DataSource ds, coloumnsName;

	public void setup() throws Exception {
		ds = testData.get(testName);
		coloumnsName = testData.get(testName+"_coloumnsName");
		sugar().meetings.api.create(ds);
		sugar().login();
	}

	/**
	 * Verify that the default columns are correct in the Meetings list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27876_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Observe the default columns in Meetings list view
		sugar().meetings.navToListView();
		for (int i = 0; i < coloumnsName.size(); i++) {
			int j=i+2;
			// TODO: VOOD-1383
			VoodooControl coloumn = new VoodooControl("span", "css", ".flex-list-view-content tr:nth-child(1) th:nth-child("+j+") span");
			coloumn.assertContains(coloumnsName.get(i).get("coloumnsName"), true);
		}

		// Click on Status to sort in ascending order
		sugar().meetings.listView.sortBy("headerStatus", true);

		// Verify sorting correctly with ascending order
		sugar().meetings.listView.verifyField(1, "status", ds.get(0).get("status"));
		sugar().meetings.listView.verifyField(2, "status", ds.get(1).get("status"));

		// Click on "Columns" gear to de-select 'Status'
		// TODO: VOOD-467
		VoodooControl fldToggle = new VoodooControl ("button", "css", "table.table.table-striped.dataTable button[data-action='fields-toggle']");
		fldToggle.click();
		VoodooControl statusCoulmn = new VoodooControl ("button", "css", "[data-field-toggle='status']");
		statusCoulmn.click();
		fldToggle.click();

		// Verify 'Status' column is disappearing in the list view
		VoodooControl statusColumn = new VoodooControl("th", "css", "[data-fieldname='status']");
		statusColumn.assertExists(false);

		// Click on "Columns" gear to re-select 'Status'
		fldToggle.click();
		statusCoulmn.click();
		fldToggle.click();
		
		// Verify 'Status' column is appearing in the list view
		statusColumn.assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}