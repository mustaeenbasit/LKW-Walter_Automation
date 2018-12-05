package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;

public class Bugs_18574 extends SugarTest {

	public void setup() throws Exception {             
		sugar.login();
		sugar.bugs.api.create();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * TC 18574: Verify that the record is deleted from list view using record view delete  
	 * 
	 * @author jenniferxia
	 */
	@Test
	public void Bugs_18574_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs record view
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);
		sugar.bugs.recordView.delete();
		//TODO:  JIRA story VOOD-655 about need wait to click confirm in confirmation alert dialog box.
		VoodooUtils.pause(5000);
		sugar.alerts.getAlert().confirmAlert();
		
		//Navigate back to list view to verify No records are present
		sugar.bugs.navToListView();
		sugar.bugs.listView.getControl("emptyListViewMsg").assertContains("No data available.", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}