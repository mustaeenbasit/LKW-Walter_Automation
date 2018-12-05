package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 
import com.sugarcrm.candybean.datasource.DataSource;

public class Tasks_17049 extends SugarTest {

	public void setup() throws Exception {	
		DataSource taskRecord = testData.get(testName);
		sugar().tasks.api.create(taskRecord);	
		sugar().login();
	}

	/**
	 * TC 17049: Verify that "Close" action of a task record is hidden on list view if the action not applicable for the record
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17049_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().tasks.navToListView();	
		sugar().tasks.listView.sortBy("headerName", false);

		// TODO: VOOD-596.  If it is fixed, please update the following steps. 
		// First task is Completed status
		sugar().tasks.listView.openRowActionDropdown(2);
		new VoodooControl("ul", "css", ".dataTable tbody tr:nth-of-type(2) .dropdown-menu").assertContains("Close", false);

		// Second task is Deferred
		sugar().tasks.listView.openRowActionDropdown(1);
		new VoodooControl("ul", "css", ".dataTable tbody tr:nth-of-type(1) .dropdown-menu").assertContains("Close", true);

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}