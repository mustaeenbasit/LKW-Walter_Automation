package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21102 extends SugarTest {
	DataSource customData;

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar.tasks.api.create(customData);
		sugar.login();	
	}

	/**
	 * Paginate Note_Verify that corresponding record is displayed in task detail view when clicking the pagination control link in task detail view.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_21102_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar.tasks.navToListView();
		// Sort Records
		sugar.tasks.listView.sortBy("headerName", true);
		// Click on the first Task record in the listview.
		sugar.tasks.listView.clickRecord(1);

		// Verify Detailview of task record is displayed
		sugar.tasks.recordView.assertVisible(true);

		// Click "Next" pagination control link in tasks detail view
		sugar.tasks.recordView.gotoNextRecord();
		// Verify detail view of the task is displayed for the second task in tasks ListView. 
		sugar.tasks.recordView.assertContains(customData.get(1).get("subject"), true);

		// Click "Previous" pagination control link in task list detail view.
		sugar.tasks.recordView.gotoPreviousRecord();
		// Verify detail view of the task is displayed for the first task in tasks ListView. 
		sugar.tasks.recordView.assertContains(customData.get(0).get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}