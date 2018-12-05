package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27876 extends SugarTest {
	DataSource ds, coloumnsName;

	public void setup() throws Exception {
		ds = testData.get(testName);
		coloumnsName = testData.get(testName+"_coloumnsName");
		sugar.calls.api.create(ds);
		sugar.login();
	}

	/**
	 * Verify that the default columns are correct in the calls list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_27876_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Observe the default columns in calls list view
		sugar.calls.navToListView();
		for (int i = 0; i < coloumnsName.size(); i++) {
			int j=i+2;
			// TODO: VOOD-845
			VoodooControl coloumn = new VoodooControl("span", "css", ".flex-list-view-content tr:nth-child(1) th:nth-child("+j+") span");
			coloumn.assertContains(coloumnsName.get(i).get("coloumnsName"), true);
		}

		// Click on Status to sort in ascending order
		sugar.calls.listView.sortBy("headerStatus", true);

		// Verify sorting correctly with ascending order
		sugar.calls.listView.verifyField(1, "status", ds.get(0).get("status"));
		sugar.calls.listView.verifyField(2, "status", ds.get(1).get("status"));

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