package com.sugarcrm.test.tasks;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21009 extends SugarTest {
	DataSource tasksData = new DataSource();

	public void setup() throws Exception {
		tasksData = testData.get(testName);
		sugar().tasks.api.create(tasksData);
		sugar().login();
	}

	/**
	 * Search Task_Verify that task can be searched by "Only my items" for basic search function.
	 * @throws Exception
	 */
	@Test
	public void Tasks_21009_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Tasks" link in navigation shortcuts.
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);

		// TODO: VOOD-444
		FieldSet fs = new FieldSet();
		fs.put("Assigned to", sugar().users.getQAUser().get("userName"));

		// Assigned 1 record to QAUser
		// TODO: VOOD-1828
		sugar().tasks.listView.setSearchString(tasksData.get(1).get("subject"));
		VoodooUtils.waitForReady();
		sugar().tasks.listView.checkRecord(1);
		sugar().tasks.massUpdate.performMassUpdate(fs);

		// After assigned record to QAUser, clear the search. 
		sugar().tasks.listView.clearSearch();

		// Go to Filter -> 'Select' dropdown and select 'My items'
		sugar().tasks.listView.openFilterDropdown();
		sugar().tasks.listView.selectFilterAssignedToMe();

		// Verify that tasks assigned to current user are displayed. 
		sugar().tasks.listView.getDetailField(1, "subject").assertEquals(tasksData.get(0).get("subject"), true);

		// Verify that the second record should not populate.
		sugar().tasks.listView.getControl("checkbox03").assertExists(false);

		// Reset Select All filter
		sugar().tasks.listView.openFilterDropdown();
		sugar().tasks.listView.selectFilterAll();
		VoodooUtils.waitForReady();

		// Verify that all the tasks are displayed.
		Assert.assertEquals("Total no. of records not equal to 2", 2, sugar().tasks.listView.countRows());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}