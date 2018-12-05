package com.sugarcrm.test.tasks;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21151  extends SugarTest {
	DataSource taskList;

	public void setup() throws Exception {
		taskList = testData.get(testName);

		// Creating 3 Tasks
		sugar.tasks.api.create(taskList);
		sugar.login();
	}

	/**
	 * Search Tasks: Verify that tasks can be searched by "Subject" in the basic search panel.
	 * @throws Exception
	 */
	@Test
	public void Tasks_21151_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);

		// Entering the task subject string to be searched
		sugar.tasks.listView.setSearchString(taskList.get(0).get("subject"));
		VoodooUtils.waitForReady();

		// Assert that only one record is displayed matching the search
		Assert.assertTrue("More than one record is displayed!!", sugar.tasks.listView.countRows()==1);

		// Verify the subject of the record with the subject entered for search
		sugar.tasks.listView.verifyField(1, "subject",taskList.get(0).get("subject"));

		// Clear the search string in the filters panel
		sugar.tasks.listView.clearSearch();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}