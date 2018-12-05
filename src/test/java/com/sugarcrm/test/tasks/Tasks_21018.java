package com.sugarcrm.test.tasks;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21018 extends SugarTest {
	DataSource tasksData = new DataSource();

	public void setup() throws Exception {
		tasksData = testData.get(testName + "_" + sugar().tasks.moduleNamePlural);
		sugar().tasks.api.create(tasksData);
		sugar().login();
	}

	/**
	 * Search Tasks_Verify that tasks can be searched by status.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21018_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Tasks" link in navigation shortcuts.
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);

		FieldSet filterData = testData.get(testName).get(0);
		VoodooControl taskRecordCtrl = sugar().tasks.listView.getDetailField(1, "subject");

		for(int i = 0; i < tasksData.size(); i++) {
			// Go to Filter -> Create Filter
			sugar().tasks.listView.openFilterDropdown();
			sugar().tasks.listView.selectFilterCreateNew();

			// Select 'Status' from 'Select' drop down -> Select a option for "Status"
			sugar().tasks.listView.filterCreate.setFilterFields(filterData.get("fieldName"), filterData.get("filterFor"), filterData.get("operator"), tasksData.get(i).get("status"), 1);
			VoodooUtils.waitForReady();

			// Verify that the Tasks matching the search conditions are displayed
			taskRecordCtrl.assertEquals(tasksData.get(i).get("subject"), true);
			Assert.assertTrue("Records are not searched accordingly", sugar().tasks.listView.countRows() == 1);

			// Cancel the filter pane from the list view
			sugar().tasks.listView.filterCreate.cancel();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}